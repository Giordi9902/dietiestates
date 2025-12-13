package it.unina.ingsw.dietiestates_backend.file;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageStorageService implements ImageStorageService {

	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;


	@Override
	public String storeImage(MultipartFile file, Integer listingId) {
		if (listingId == null) throw new IllegalArgumentException("listingId cannot be null");
		if (file == null || file.isEmpty()) throw new IllegalArgumentException("file cannot be null or empty");
		if (!isValidImage(file)) throw new IllegalArgumentException("Unsupported image type: " + file.getContentType());

		String key = buildObjectKey(listingId, file.getOriginalFilename(), file.getContentType());
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		if (file.getContentType() != null) metadata.setContentType(file.getContentType());

		try {
			amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
			return amazonS3.getUrl(bucketName, key).toString();
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload image to S3", e);
		}
	}

	@Override
	public boolean deleteImage(String imageUrl) {
		if (imageUrl == null || imageUrl.isBlank()) return false;
		String key = extractKeyFromUrl(imageUrl);
		if (key == null || key.isBlank()) return false;
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
			return true;
		} catch (AmazonS3Exception ex) {
			return false;
		}
	}

	@Override
	public int deleteAllImagesForListing(Integer listingId) {
		if (listingId == null) return 0;
		String prefix = listingPrefix(listingId);

		int deleted = 0;
		ListObjectsV2Request listReq = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(prefix);
		ListObjectsV2Result res;
		do {
			res = amazonS3.listObjectsV2(listReq);
			if (res.getObjectSummaries().isEmpty()) break;

			List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
			for (S3ObjectSummary s : res.getObjectSummaries()) {
				keys.add(new DeleteObjectsRequest.KeyVersion(s.getKey()));
			}
			DeleteObjectsRequest delReq = new DeleteObjectsRequest(bucketName).withKeys(keys);
			try {
				DeleteObjectsResult delRes = amazonS3.deleteObjects(delReq);
				deleted += delRes.getDeletedObjects().size();
			} catch (MultiObjectDeleteException e) {
				deleted += e.getDeletedObjects().size();
			}

			listReq.setContinuationToken(res.getNextContinuationToken());
		} while (res.isTruncated());

		return deleted;
	}

	@Override
	public boolean isValidImage(MultipartFile file) {
		if (file == null || file.isEmpty()) return false;
		String ct = file.getContentType();
		if (ct == null) return false;
		return ct.equalsIgnoreCase("image/jpeg") || ct.equalsIgnoreCase("image/png") ||
				ct.equalsIgnoreCase("image/webp") || ct.equalsIgnoreCase("image/gif");
	}

	private String listingPrefix(Integer listingId) {
		return "listings/" + listingId + "/";
	}

	private String buildObjectKey(Integer listingId, String originalFilename, String contentType) {
		String prefix = listingPrefix(listingId);
		String ext = safeExtension(originalFilename, contentType);
		return prefix + UUID.randomUUID() + (ext.isBlank() ? "" : "." + ext);
	}

	private String safeExtension(String originalFilename, String contentType) {
		String ext = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			String cand = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
			if (cand.length() <= 8) ext = cand;
		}
		if (ext.isBlank() && contentType != null) {
			if (contentType.equalsIgnoreCase("image/jpeg")) ext = "jpg";
			else if (contentType.equalsIgnoreCase("image/png")) ext = "png";
			else if (contentType.equalsIgnoreCase("image/webp")) ext = "webp";
			else if (contentType.equalsIgnoreCase("image/gif")) ext = "gif";
		}
		return ext.toLowerCase();
	}

	public String extractKeyFromUrl(String imageUrl) {
		try {
			URI uri = URI.create(imageUrl);
			String path = uri.getPath();
			if (path == null) return null;
			String key = path.startsWith("/") ? path.substring(1) : path;
			if (key.startsWith(bucketName + "/")) {
				key = key.substring(bucketName.length() + 1);
			}
			return key;
		} catch (Exception e) {
			return null;
		}
	}

	public String getOriginalUrl(String key) {
		return amazonS3.getUrl(bucketName, key).toString();
	}

	public URL generatePresignedUrl(String key) {
		Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10);
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key)
				.withMethod(HttpMethod.GET)
				.withExpiration(expiration);
		return amazonS3.generatePresignedUrl(request);
	}

	public String presignUrl(String imageUrl) {
		if (imageUrl == null || imageUrl.isBlank()) return imageUrl;
		String key = extractKeyFromUrl(imageUrl);
		if (key == null || key.isBlank()) return imageUrl;
		return generatePresignedUrl(key).toString();
	}

}