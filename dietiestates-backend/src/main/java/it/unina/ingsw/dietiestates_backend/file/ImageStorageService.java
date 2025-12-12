package it.unina.ingsw.dietiestates_backend.file;


import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
	String storeImage(MultipartFile file, Integer listingId);
	boolean deleteImage(String imageUrl);
	int deleteAllImagesForListing(Integer listingId);
	boolean isValidImage(MultipartFile file);
}