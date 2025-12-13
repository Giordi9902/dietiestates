package it.unina.ingsw.dietiestates_backend.listings.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingDetailsResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.searching.ListingSearchRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.update.UpdateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.service.ListingService;
import it.unina.ingsw.dietiestates_backend.listings.service.security.IsAgentOfListing;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("listings")
@RequiredArgsConstructor
@Tag(name="listings")
@Slf4j
public class ListingController {
	private final ListingService listingService;

	@GetMapping("/{id}")
	public ResponseEntity<ListingDetailsResponse> getListingById(@PathVariable Integer id){
		ListingDetailsResponse listingDetailsResponse = listingService.retrieveListingById(id);
		return ResponseEntity.ok(listingDetailsResponse);
	}

	@GetMapping("/agent")
	@PreAuthorize("hasAnyRole('AGENT','ADMIN')")
	public ResponseEntity<Page<ListingSummaryResponse>> getAgentsListings(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	){
		Pageable pageable = PageRequest.of(page,size);
		Page<ListingSummaryResponse> results = listingService.getListingsByAgent(pageable);
		return ResponseEntity.ok(results);
	}

	@PostMapping("/search")
	public ResponseEntity<Page<ListingSummaryResponse>> search(
			@RequestBody ListingSearchRequest searchRequest,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
			){
		Pageable pageable = PageRequest.of(page,size);
		Page<ListingSummaryResponse> results = listingService.searchListingsByFilters(searchRequest,pageable);
		return ResponseEntity.ok(results);
	}

	    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
	    public ResponseEntity<String> create(
			@Valid @RequestPart CreateListingRequest request,
			@RequestPart(value="images") List<MultipartFile> images
	){
		log.info("Create listing request received: title='{}', operation={}, price={}, area={}, lat={}, lng={}, energyClass={}, imagesCount={}",
			request.getTitle(), request.getListingOperation(), request.getPrice(), request.getArea(), request.getLat(), request.getLng(), request.getEnergyClass(), images != null ? images.size() : 0);
		listingService.createListing(request,images);
		log.info("Listing created successfully");
		return ResponseEntity.status(CREATED).body("\"Listing created successfully\"");
	}

	@PostMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@IsAgentOfListing
	public ResponseEntity<Void> update(
			@PathVariable Integer id,
			@RequestPart UpdateListingRequest request,
			@RequestPart(required = false) List<MultipartFile> newImages
	){
		listingService.updateListingById(request, id, newImages);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	@IsAgentOfListing
	public ResponseEntity<Void> delete(
			@PathVariable Integer id
	){
		listingService.deleteListingById(id);
		return ResponseEntity.noContent().build();
	}
}