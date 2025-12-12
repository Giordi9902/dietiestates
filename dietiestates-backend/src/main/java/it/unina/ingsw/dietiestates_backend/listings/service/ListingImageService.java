package it.unina.ingsw.dietiestates_backend.listings.service;


import it.unina.ingsw.dietiestates_backend.file.S3ImageStorageService;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.ListingImage;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

	public void deleteListingImages(Integer listing_id){
		s3ImageStorageService.deleteAllImagesForListing(listing_id);
	}

	public String presign(String imageUrl) {
		return s3ImageStorageService.presignUrl(imageUrl);
	}

	public List<ListingImage> findListingImages(Integer id) {
		return listingImageRepository.findByListingId(id);
	}
}