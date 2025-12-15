package it.unina.ingsw.dietiestates_backend.listings.service;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.errors.exception.ListingNotFoundException;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.ListingImage;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingDetailsResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.searching.ListingSearchRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.update.UpdateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingRepository;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingSearchRepositoryImpl;
import it.unina.ingsw.dietiestates_backend.listings.service.mapper.ListingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
@Slf4j
public class ListingService {
	private final ListingRepository listingRepository;
	private final ListingImageService listingImageService;
	private final UserRepository userRepository;
	private final ListingMapper listingMapper;
	private final GeometryFactory geometryFactory;
	private final ListingSearchRepositoryImpl listingSearchRepository;


	@Transactional
	public void createListing(
			CreateListingRequest request,
			List<MultipartFile> images)
	{
		log.info("Persisting listing: title='{}', price={}, area={}, lat={}, lng={}, imagesCount={}",
			request.getTitle(), request.getPrice(), request.getArea(), request.getLat(), request.getLng(), images != null ? images.size() : 0);
		User agent = getAgent();
		Listing listing = listingMapper.toEntity(request);
		listing.setListedBy(agent);
		Integer id = listingRepository.save(listing).getId();
		listing.setId(id);
		log.info("Listing persisted with id={}", id);
		uploadImages(listing,images);
	}

	private User getAgent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails user)) {
			throw new IllegalStateException("No authenticated user found");
		}
		return fetchAgent(user);
	}

	private User fetchAgent(UserDetails principal) {
		String email = principal.getUsername();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
	}

	public ListingDetailsResponse retrieveListingById(Integer id){
		Listing listing = listingRepository.findById(id).orElseThrow(()->new ListingNotFoundException("Listing not " +
				"found with id:"+id));
		List<ListingImage> listingImages = listingImageService.findListingImages(id);
		listing.setImages(listingImages);
		return listingMapper.createListingDetailResponse(listing);
	}

	public Page<ListingSummaryResponse> searchListingsByFilters(ListingSearchRequest searchRequest,
																Pageable pageable) {
		return listingSearchRepository.searchListingsByFilters(searchRequest,pageable);
	}

	@Transactional
	public void updateListingById(UpdateListingRequest request, Integer id, List<MultipartFile> newImages) {
		Listing listing = listingRepository.findById(id)
				.orElseThrow(() -> new ListingNotFoundException("Listing not found with id:" + id));

		if (request.getImagesToRemove() != null && !request.getImagesToRemove().isEmpty()) {
			log.info("Removing {} images for listing id={}", request.getImagesToRemove().size(), id);
			for (String imageUrl : request.getImagesToRemove()) {
				listingImageService.deleteImageByUrl(imageUrl);
			}
			log.info("Images removal completed for listing id={}", id);
		}

		if (newImages != null && !newImages.isEmpty()) {
			uploadImages(listing, newImages);
		}

		applyUpdateFields(listing, request);
		listingRepository.save(listing);
	}

	private void applyUpdateFields(Listing listing, UpdateListingRequest request) {
		if (request.getTitle() != null) listing.setTitle(request.getTitle());
		if (request.getDescription() != null) listing.setDescription(request.getDescription());
		if (request.getAddress() != null) listing.setAddress(request.getAddress());
		if (request.getArea() != null) listing.setArea(request.getArea());
		if (request.getFloor() != null) listing.setFloor(request.getFloor());
		if (request.getRooms() != null) listing.setRooms(request.getRooms());
		if (request.getBathrooms() != null) listing.setBathrooms(request.getBathrooms());
		if (request.getEnergyClass() != null) listing.setEnergyClass(request.getEnergyClass());
		if (request.getHasElevator() != null) listing.setHasElevator(request.getHasElevator());
		if (request.getHasParking() != null) listing.setHasParking(request.getHasParking());
		if (request.getHasGarden() != null) listing.setHasGarden(request.getHasGarden());
		if (request.getHasBalcony() != null) listing.setHasBalcony(request.getHasBalcony());

		if (request.getLng() != null && request.getLat() != null) {
			Point point = geometryFactory.createPoint(new Coordinate(request.getLng(), request.getLat()));
			point.setSRID(4326);
			listing.setCoordinate(point);
		}
	}

	@Transactional
	public void deleteListingById(Integer id) {
		listingRepository.deleteById(id);
		listingImageService.deleteListingImages(id);
	}

	public Page<ListingSummaryResponse> getListingsByAgent(Pageable pageable) {
		User agent = getAgent();
		Page<ListingSummaryResponse> page = listingRepository.findListingsByAgentId(agent.getId(),pageable);
		return page.map(summary -> {
			String url = summary.getThumbnailUrl();
			if (url != null && !url.isBlank()) {
				summary.setThumbnailUrl(listingImageService.presign(url));
			}
			return summary;
		});
	}

	private void uploadImages(Listing listing, List<MultipartFile> images){
		if (images == null || images.isEmpty()) throw new IllegalArgumentException("images cannot be null or empty");
		log.info("Uploading {} images for listing id={}", images.size(), listing.getId());
		for (MultipartFile image : images) {
			log.debug("Uploading image name='{}', size={}", image.getOriginalFilename(), image.getSize());
			listingImageService.saveImage(image, listing);
		}
		log.info("Images upload completed for listing id={}", listing.getId());
	}
}