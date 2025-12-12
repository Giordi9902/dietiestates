package it.unina.ingsw.dietiestates_backend.listings.service.factories;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.RentListing;
import it.unina.ingsw.dietiestates_backend.listings.model.SaleListing;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateRentListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.RentListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.service.ListingImageService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RentListingStrategy implements ListingStrategy<CreateRentListingRequest> {
	private final ListingImageService listingImageService;
	private final GeometryFactory geometryFactory;

	@Override
	public Listing createListing(CreateRentListingRequest request, User agent){
		Point coord = createPointFromDTO(request.getLng(), request.getLat());
		RentListing listing = new RentListing();
		listing.setTitle(request.getTitle());
		listing.setDescription(request.getDescription());
		listing.setCoordinate(coord);
		listing.setAddress(request.getAddress());
		listing.setArea(request.getArea());
		listing.setMonthlyRent(request.getMonthlyRent());
		listing.setListedBy(agent);
		listing.setRooms(request.getRooms());
		listing.setBathrooms(request.getBathrooms());
		listing.setEnergyClass(request.getEnergyClass());
		if(request.getHasBalcony()!=null) listing.setHasBalcony(request.getHasBalcony());
		if(request.getHasElevator()!=null) listing.setHasElevator(request.getHasElevator());
		if(request.getHasGarden()!=null) listing.setHasGarden(request.getHasGarden());
		if(request.getHasParking()!=null)listing.setHasParking(request.getHasParking());
		return listing;
	}

	private Point createPointFromDTO(Double lng, Double lat) {
		if(lng==null || lat == null) return null;
		Coordinate jts = new Coordinate(lng,lat);
		Point point = geometryFactory.createPoint(jts);
		point.setSRID(4326);
		return point;
	}

	@Override
	public Class<CreateRentListingRequest> getRequestType(){
		return CreateRentListingRequest.class;
	}

	@Override
	public Class<? extends Listing> getListingType() {
		return RentListing.class;
	}

	@Override
	public void uploadImages(Listing listing, List<MultipartFile> images) {
		if (images == null || images.isEmpty()) throw new IllegalArgumentException("images cannot be null or empty");
		for (MultipartFile image : images) {
			listingImageService.saveImage(image, listing);
		}
	}

	@Override
	public ListingSummaryResponse mapToSummaryDTO(Listing listing){
		if(!(listing instanceof RentListing rentListing)){
			throw new IllegalArgumentException("Listing provided is not a RentListing");
		}

		String thumbnailUrl = listing.getImages().isEmpty() ? null :
				listingImageService.presign(listing.getImages().get(0).getUrl());

		RentListingSummaryResponse rentListingSummaryResponse = new RentListingSummaryResponse();
		rentListingSummaryResponse.setListing_id(listing.getId());
		rentListingSummaryResponse.setListingOperation("RENT");
		rentListingSummaryResponse.setTitle(listing.getTitle());
		rentListingSummaryResponse.setAddress(listing.getAddress());
		rentListingSummaryResponse.setDescription(listing.getDescription());
		rentListingSummaryResponse.setArea(listing.getArea());
		rentListingSummaryResponse.setEnergyClass(listing.getEnergyClass().toString());
		rentListingSummaryResponse.setThumbnailUrl(thumbnailUrl);
		rentListingSummaryResponse.setMonthlyRent(rentListing.getMonthlyRent());

		return rentListingSummaryResponse;
	}


}