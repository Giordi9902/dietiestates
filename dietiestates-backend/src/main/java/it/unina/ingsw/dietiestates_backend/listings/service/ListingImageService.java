package it.unina.ingsw.dietiestates_backend.listings.service;


import it.unina.ingsw.dietiestates_backend.file.S3ImageStorageService;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.ListingImage;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ListingImageService {

	private final S3ImageStorageService s3ImageStorageService;
	private final ListingImageRepository listingImageRepository;

	public ListingImage saveImage(MultipartFile file, Listing listing) {
		String url = s3ImageStorageService.storeImage(file, listing.getId());
		ListingImage image = new ListingImage();
		image.setUrl(url);
		image.setListing(listing);
		listing.getImages().add(image);
		return listingImageRepository.save(image);
	}

	public String presign(String imageUrl) {
		return s3ImageStorageService.presignUrl(imageUrl);
	}
}