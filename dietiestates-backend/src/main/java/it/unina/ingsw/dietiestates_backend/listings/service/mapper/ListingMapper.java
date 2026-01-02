package it.unina.ingsw.dietiestates_backend.listings.service.mapper;

import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingDetailsResponse;
import it.unina.ingsw.dietiestates_backend.listings.service.GeometryService;
import it.unina.ingsw.dietiestates_backend.listings.service.ListingImageService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListingMapper {
	private final GeometryService geometryService;
	private final ListingImageService listingImageService;

	public Listing toEntity(CreateListingRequest request) {
		Point point = geometryService.createPoint(request.getLng(), request.getLat());
		Listing listing = Listing.builder()
				.title(request.getTitle())
				.description(request.getDescription())
				.coordinate(point)
				.price(request.getPrice())
				.area(request.getArea())
				.address(request.getAddress())
				.energyClass(request.getEnergyClass())
				.listingOperation(request.getListingOperation())
				.build();
		setOptionalFields(request, listing);
		return listing;
	}

	public ListingDetailsResponse createListingDetailResponse(Listing listing){
		return ListingDetailsResponse.builder()
				.id(listing.getId())
				.title(listing.getTitle())
				.description(listing.getDescription())
				.floor(listing.getFloor())
				.address(listing.getAddress())
				.lng(listing.getCoordinate().getX())
				.lat(listing.getCoordinate().getY())
				.price(listing.getPrice())
				.rooms(listing.getRooms())
				.area(listing.getArea())
				.energyClass(listing.getEnergyClass())
				.bathrooms(listing.getBathrooms())
				.listingOperation(listing.getListingOperation())
				.hasBalcony(listing.getHasBalcony())
				.hasElevator(listing.getHasElevator())
				.hasGarden(listing.getHasGarden())
				.hasParking(listing.getHasParking())
				.imageUrls(
						listing.getImages().stream()
								.map(i -> listingImageService.presign(i.getUrl()))
								.toList()
				)
				.build();
	}

	private static void setOptionalFields(CreateListingRequest request, Listing listing) {
		if(request.getFloor()!=null) listing.setFloor(request.getFloor());
		if(request.getRooms()!=null) listing.setRooms(request.getRooms());
		if(request.getBathrooms()!=null) listing.setBathrooms(request.getBathrooms());
		if(request.getHasBalcony()!=null) listing.setHasBalcony(request.getHasBalcony());
		if(request.getHasElevator()!=null) listing.setHasElevator(request.getHasElevator());
		if(request.getHasParking()!=null) listing.setHasParking(request.getHasParking());
		if(request.getHasGarden()!=null) listing.setHasGarden(request.getHasGarden());
	}
}