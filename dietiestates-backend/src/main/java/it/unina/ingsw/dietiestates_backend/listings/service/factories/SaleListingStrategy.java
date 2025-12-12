package it.unina.ingsw.dietiestates_backend.listings.service.factories;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.SaleListing;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateSaleListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.SaleListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.service.ListingImageService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaleListingStrategy implements ListingStrategy<CreateSaleListingRequest> {
	private final ListingImageService listingImageService;
	private final GeometryFactory geometryFactory;

	@Override
	public Listing createListing(CreateSaleListingRequest request, User agent){
		Point coord = createPointFromDTO(request.getLng(), request.getLat());
		SaleListing listing = new SaleListing();
		listing.setTitle(request.getTitle());
		listing.setDescription(request.getDescription());
		listing.setCoordinate(coord);
		listing.setAddress(request.getAddress());
		listing.setArea(request.getArea());
		listing.setPrice(request.getPrice());
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

	@Override
	public Class<CreateSaleListingRequest> getRequestType(){
		return CreateSaleListingRequest.class;
	}

	@Override
	public Class<? extends Listing> getListingType() {
		return SaleListing.class;
	}

	private Point createPointFromDTO(Double lng, Double lat) {
		if(lng==null || lat == null) return null;
		Coordinate jts = new Coordinate(lng,lat);
		Point point = geometryFactory.createPoint(jts);
		point.setSRID(4326);
		return point;
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
		if(!(listing instanceof SaleListing saleListing)){
			throw new IllegalArgumentException("Listing provided is not a SaleListing");
		}
		String thumbnailUrl = listing.getImages().isEmpty() ? null :
				listingImageService.presign(listing.getImages().get(0).getUrl());

		SaleListingSummaryResponse saleListingSummaryResponse = new SaleListingSummaryResponse();
		saleListingSummaryResponse.setListing_id(listing.getId());
		saleListingSummaryResponse.setListingOperation("SALE");
		saleListingSummaryResponse.setTitle(listing.getTitle());
		saleListingSummaryResponse.setAddress(listing.getAddress());
		saleListingSummaryResponse.setDescription(listing.getDescription());
		saleListingSummaryResponse.setArea(listing.getArea());
		saleListingSummaryResponse.setEnergyClass(listing.getEnergyClass().toString());
		saleListingSummaryResponse.setThumbnailUrl(thumbnailUrl);
		saleListingSummaryResponse.setPrice(saleListing.getPrice());

		return saleListingSummaryResponse;
	}

}