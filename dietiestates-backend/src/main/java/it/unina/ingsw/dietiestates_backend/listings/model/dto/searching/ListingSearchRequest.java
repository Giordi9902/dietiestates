package it.unina.ingsw.dietiestates_backend.listings.model.dto.searching;

import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.ListingOperation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ListingSearchRequest {
	@NotNull
	private ListingOperation operation;
	private EnergyClass energyClass;
	private Double minPrice;
	private Double maxPrice;
	private Integer minRooms;
	private Integer maxRooms;
	private Double minArea;
	private Double maxArea;
	private Double lng;
	private Double lat;
	private Double radiusKm;
}