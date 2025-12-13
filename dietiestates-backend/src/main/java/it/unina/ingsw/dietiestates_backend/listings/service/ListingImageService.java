package it.unina.ingsw.dietiestates_backend.listings.service;


import it.unina.ingsw.dietiestates_backend.file.S3ImageStorageService;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.ListingImage;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingImageService {

	private final S3ImageStorageService s3ImageStorageService;
	private final ListingImageRepository listingImageRepository;

	@Transactional
	public ListingImage saveImage(MultipartFile file, Listing listing) {
		String url = s3ImageStorageService.storeImage(file, listing.getId());

		// Find the max sort order and add 1
		Integer maxSortOrder = listingImageRepository.findMaxSortOrderByListingId(listing.getId());
		int newSortOrder = (maxSortOrder != null) ? maxSortOrder + 1 : 1;

		ListingImage image = new ListingImage();
		image.setUrl(url);
		image.setListing(listing);
		image.setSortOrder(newSortOrder);

		listing.getImages().add(image);
		return listingImageRepository.save(image);
	}

	public void deleteListingImages(Integer listing_id){
		s3ImageStorageService.deleteAllImagesForListing(listing_id);
	}

	@Transactional
	public void deleteImageByUrl(String presignedUrl) {
		if (presignedUrl == null || presignedUrl.isBlank()) {
			return;
		}

		// Prima elimina l'oggetto da S3
		s3ImageStorageService.deleteImage(presignedUrl);

		// Poi, recupera l'URL originale e usalo per eliminare dal DB
		String key = s3ImageStorageService.extractKeyFromUrl(presignedUrl);
		if (key != null && !key.isBlank()) {
			String originalUrl = s3ImageStorageService.getOriginalUrl(key);
			listingImageRepository.deleteByUrl(originalUrl);
		}
	}

	public String presign(String imageUrl) {
		return s3ImageStorageService.presignUrl(imageUrl);
	}

	public List<ListingImage> findListingImages(Integer id) {
		return listingImageRepository.findByListingId(id);
	}
}