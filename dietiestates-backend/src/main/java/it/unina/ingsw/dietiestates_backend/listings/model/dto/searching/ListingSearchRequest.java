package it.unina.ingsw.dietiestates_backend.listings.model.dto.searching;

import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.ListingOperation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ListingSearchRequest {
	@NotNull(message = "Il tipo di operazione è obbligatorio")
	private ListingOperation operation;
	private EnergyClass energyClass;
	private Double minPrice;
	private Double maxPrice;
	private Integer minRooms;
	private Integer maxRooms;
	private Double minArea;
	private Double maxArea;
    @NotNull(message = "La longitudine è obbligatoria")
	private Double lng;
    @NotNull(message = "La latitudine è obbligatoria")
	private Double lat;
	private Double radiusKm;
}