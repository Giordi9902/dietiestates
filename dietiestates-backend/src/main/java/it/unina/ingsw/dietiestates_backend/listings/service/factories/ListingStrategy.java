package it.unina.ingsw.dietiestates_backend.listings.service.factories;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingStrategy<T> {
	Listing createListing(T request, User agent);
	Class<T> getRequestType();
	Class<? extends Listing> getListingType();
	void uploadImages(Listing listing, List<MultipartFile> images);
	ListingSummaryResponse mapToSummaryDTO(Listing listing);
}