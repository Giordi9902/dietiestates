package it.unina.ingsw.dietiestates_backend.listings.service;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.file.S3ImageStorageService;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingDetailsResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.searching.ListingSearchRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.update.UpdateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingRepository;
import it.unina.ingsw.dietiestates_backend.listings.service.factories.ListingStrategy;
import it.unina.ingsw.dietiestates_backend.listings.service.factories.ListingStrategyFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingService {
	private final ListingRepository listingRepository;
	private final ListingImageService listingImageService;
	private final UserRepository userRepository;
	private final S3ImageStorageService s3ImageStorageService;
	private final ListingStrategyFactory strategyFactory;

	@Transactional
	public <T extends CreateListingRequest> void createListing(
			T request,
			List<MultipartFile> images)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails user)) {
			throw new IllegalStateException("No authenticated user found");
		}
		User agent = fetchAgent(user);
		ListingStrategy<T> strategy = strategyFactory.getStrategy((Class<T>)request.getClass());
		Listing listing = strategy.createListing(request,agent);
		Integer listingId = listingRepository.save(listing).getId();
		listing.setId(listingId);
		strategy.uploadImages(listing,images);
	}

	private User fetchAgent(UserDetails principal) {
		String email = principal.getUsername();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
	}

	public ListingDetailsResponse retrieveListingById(Integer id){
//		Listing listing = listingRepository.findById(id)
//				.orElseThrow(() -> new IllegalArgumentException("Listing not found for id=" + id));
//
//		List<String> imageUrls = listing.getImages() == null ? List.of() :
//				listing.getImages().stream()
//						.map(img -> listingImageService.presign(img.getUrl()))
//						.collect(Collectors.toList());
//
//		return ListingDetailsResponse.builder()
//				.id(listing.getId())
//				.title(listing.getTitle())
//				.listingOperation(listing.getOperation())
//				.description(listing.getDescription())
//				.lng(listing.getCoordinate().getCoordinate().x)
//				.lat(listing.getCoordinate().getCoordinate().y)
//				.price(listing.getPrice())
//				.rooms(listing.getRooms())
//				.bathrooms(listing.getBathrooms())
//				.area(listing.getArea())
//				.energyClass(listing.getEnergyClass())
//				.imageUrls(imageUrls)
//				.build();
		return null;
	}

	public Page<ListingSummaryResponse> searchListingsByFilters(ListingSearchRequest searchRequest, Pageable pageable) {
		return null;
	}

	public void updateListingById(UpdateListingRequest request, Integer id) {
	}

	public void deleteListingById(Integer id) {
		s3ImageStorageService.deleteAllImagesForListing(id);
		listingRepository.deleteById(id);
	}

	public Page<ListingSummaryResponse> getListingsByAgent(UserDetails userDetails, Pageable pageable) {
		User agent = fetchAgent(userDetails);
		Page<Listing> agentListings = listingRepository.findListingsByAgentId(agent.getId(), pageable);
		return agentListings.map(this::getListingSummary);
	}

	public ListingSummaryResponse getListingSummary(Listing listing){
		ListingStrategy<?> strategy = strategyFactory.getStrategy(listing.getClass());
		return strategy.mapToSummaryDTO(listing);
	}
}