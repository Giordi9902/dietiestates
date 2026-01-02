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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

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
		if (searchRequest.getRadiusKm() == null) {
			searchRequest.setRadiusKm(5.0);
		}
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
		updateTitle(listing, request);
		updateDescription(listing, request);
		updateAddress(listing, request);
		updateArea(listing, request);
		updatePrice(listing, request);
		updateOperation(listing, request);
		updateFloor(listing, request);
		updateRooms(listing, request);
		updateBathrooms(listing, request);
		updateEnergyClass(listing, request);
		updateHasElevator(listing, request);
		updateHasParking(listing, request);
		updateHasGarden(listing, request);
		updateHasBalcony(listing, request);
		updateCoordinates(listing, request);
	}

	private void updateTitle(Listing listing, UpdateListingRequest request) {
		if (request.getTitle() != null && !Objects.equals(listing.getTitle(), request.getTitle())) {
			log.info("Updating title: '{}' -> '{}'", listing.getTitle(), request.getTitle());
			listing.setTitle(request.getTitle());
		}
	}

	private void updateDescription(Listing listing, UpdateListingRequest request) {
		if (request.getDescription() != null && !Objects.equals(listing.getDescription(), request.getDescription())) {
			log.info("Updating description: '{}' -> '{}'", listing.getDescription(), request.getDescription());
			listing.setDescription(request.getDescription());
		}
	}

	private void updateAddress(Listing listing, UpdateListingRequest request) {
		if (request.getAddress() != null && !Objects.equals(listing.getAddress(), request.getAddress())) {
			log.info("Updating address: '{}' -> '{}'", listing.getAddress(), request.getAddress());
			listing.setAddress(request.getAddress());
		}
	}

	private void updateArea(Listing listing, UpdateListingRequest request) {
		if (request.getArea() != null && !Objects.equals(listing.getArea(), request.getArea())) {
			log.info("Updating area: {} -> {}", listing.getArea(), request.getArea());
			listing.setArea(request.getArea());
		}
	}

	private void updatePrice(Listing listing, UpdateListingRequest request) {
		if (request.getPrice() != null && !Objects.equals(listing.getPrice(), request.getPrice())) {
			log.info("Updating price: {} -> {}", listing.getPrice(), request.getPrice());
			listing.setPrice(request.getPrice());
		}
	}

	private void updateOperation(Listing listing, UpdateListingRequest request) {
		if (request.getListingOperation() != null && !Objects.equals(listing.getListingOperation(), request.getListingOperation())) {
			log.info("Updating operation: {} -> {}", listing.getListingOperation(), request.getListingOperation());
			listing.setListingOperation(request.getListingOperation());
		}
	}

	private void updateFloor(Listing listing, UpdateListingRequest request) {
		if (request.getFloor() != null && !Objects.equals(listing.getFloor(), request.getFloor())) {
			log.info("Updating floor: {} -> {}", listing.getFloor(), request.getFloor());
			listing.setFloor(request.getFloor());
		}
	}

	private void updateRooms(Listing listing, UpdateListingRequest request) {
		if (request.getRooms() != null && !Objects.equals(listing.getRooms(), request.getRooms())) {
			log.info("Updating rooms: {} -> {}", listing.getRooms(), request.getRooms());
			listing.setRooms(request.getRooms());
		}
	}

	private void updateBathrooms(Listing listing, UpdateListingRequest request) {
		if (request.getBathrooms() != null && !Objects.equals(listing.getBathrooms(), request.getBathrooms())) {
			log.info("Updating bathrooms: {} -> {}", listing.getBathrooms(), request.getBathrooms());
			listing.setBathrooms(request.getBathrooms());
		}
	}

	private void updateEnergyClass(Listing listing, UpdateListingRequest request) {
		if (request.getEnergyClass() != null && !Objects.equals(listing.getEnergyClass(), request.getEnergyClass())) {
			log.info("Updating energyClass: {} -> {}", listing.getEnergyClass(), request.getEnergyClass());
			listing.setEnergyClass(request.getEnergyClass());
		}
	}

	private void updateHasElevator(Listing listing, UpdateListingRequest request) {
		if (request.getHasElevator() != null && !Objects.equals(listing.getHasElevator(), request.getHasElevator())) {
			log.info("Updating hasElevator: {} -> {}", listing.getHasElevator(), request.getHasElevator());
			listing.setHasElevator(request.getHasElevator());
		}
	}

	private void updateHasParking(Listing listing, UpdateListingRequest request) {
		if (request.getHasParking() != null && !Objects.equals(listing.getHasParking(), request.getHasParking())) {
			log.info("Updating hasParking: {} -> {}", listing.getHasParking(), request.getHasParking());
			listing.setHasParking(request.getHasParking());
		}
	}

	private void updateHasGarden(Listing listing, UpdateListingRequest request) {
		if (request.getHasGarden() != null && !Objects.equals(listing.getHasGarden(), request.getHasGarden())) {
			log.info("Updating hasGarden: {} -> {}", listing.getHasGarden(), request.getHasGarden());
			listing.setHasGarden(request.getHasGarden());
		}
	}

	private void updateHasBalcony(Listing listing, UpdateListingRequest request) {
		if (request.getHasBalcony() != null && !Objects.equals(listing.getHasBalcony(), request.getHasBalcony())) {
			log.info("Updating hasBalcony: {} -> {}", listing.getHasBalcony(), request.getHasBalcony());
			listing.setHasBalcony(request.getHasBalcony());
		}
	}

	private void updateCoordinates(Listing listing, UpdateListingRequest request) {
		if (request.getLng() != null && request.getLat() != null) {
			Point prev = listing.getCoordinate();
			boolean changed = prev == null
				|| Double.compare(prev.getX(), request.getLng()) != 0
				|| Double.compare(prev.getY(), request.getLat()) != 0;
			if (changed) {
				String prevCoord = prev != null ? ("lat=" + prev.getY() + ", lng=" + prev.getX()) : "null";
				log.info("Updating coordinate: {} -> lat={}, lng={}", prevCoord, request.getLat(), request.getLng());
				Point point = geometryFactory.createPoint(new Coordinate(request.getLng(), request.getLat()));
				point.setSRID(4326);
				listing.setCoordinate(point);
			}
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

	@Async
	void uploadImages(Listing listing, List<MultipartFile> images){
		if (images == null || images.isEmpty()) throw new IllegalArgumentException("images cannot be null or empty");
		log.info("Uploading {} images for listing id={}", images.size(), listing.getId());
		for (MultipartFile image : images) {
			log.debug("Uploading image name='{}', size={}", image.getOriginalFilename(), image.getSize());
			listingImageService.saveImage(image, listing);
		}
		log.info("Images upload completed for listing id={}", listing.getId());
	}
}