package it.unina.ingsw.dietiestates_backend.listings.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateRentListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateSaleListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingDetailsResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.searching.ListingSearchRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.update.UpdateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.service.security.IsAgentOfListing;
import it.unina.ingsw.dietiestates_backend.listings.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("listings")
@RequiredArgsConstructor
@Tag(name="listings")
public class ListingController {
	private final ListingService listingService;

	@GetMapping("/{id}")
	public ResponseEntity<ListingDetailsResponse> getListingById(@PathVariable Integer id){
		ListingDetailsResponse listingDetailsResponse = listingService.retrieveListingById(id);
		return ResponseEntity.ok(listingDetailsResponse);
	}

	@GetMapping("/agent")
	public ResponseEntity<Page<ListingSummaryResponse>> getAgentsListings(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@AuthenticationPrincipal UserDetails userDetails
	){
		Pageable pageable = PageRequest.of(page,size);
		Page<ListingSummaryResponse> results = listingService.getListingsByAgent(userDetails, pageable);
		return ResponseEntity.ok(results);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<ListingSummaryResponse>> search(
			@RequestBody ListingSearchRequest searchRequest,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
			){
		Pageable pageable = PageRequest.of(page,size);
		Page<ListingSummaryResponse> results = listingService.searchListingsByFilters(searchRequest,pageable);
		return ResponseEntity.ok(results);
	}

	@PostMapping(path="/rent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('AGENT','ADMIN')")
	public ResponseEntity<String> createRent(
			@Valid @RequestPart CreateRentListingRequest request,
			@RequestPart(value="images") List<MultipartFile> images
	){
		listingService.createListing(request,images);
		return ResponseEntity.status(CREATED).body("Sale listing created succesfully");
	}

	@PostMapping(path="/sale",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAnyRole('AGENT','ADMIN')")
	public ResponseEntity<String> createSale(
			@Valid @RequestPart CreateSaleListingRequest request,
			@RequestPart(value="images") List<MultipartFile> images
	){
		listingService.createListing(request,images);
		return ResponseEntity.status(CREATED).body("Rent listing created succesfully");
	}

	@PatchMapping("/{id}")
	@IsAgentOfListing
	public ResponseEntity<String> update(
			@PathVariable Integer id,
			@RequestBody UpdateListingRequest request){
		listingService.updateListingById(request,id);
		return ResponseEntity.ok().body("Listing updated succesfully");
	}

	@DeleteMapping("/{id}")
	@IsAgentOfListing
	public ResponseEntity<String> delete(
			@PathVariable Integer id
	){
		listingService.deleteListingById(id);
		return ResponseEntity.ok().body("Listing deleted succesfully");
	}
}