package it.unina.ingsw.dietiestates_backend.listings.model.dto.update;

import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.ListingOperation;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateListingRequest {
    private String title;
    private String description;
	private Double lng;
	private Double lat;
	private Integer floor;
    private BigDecimal price;
	private Integer rooms;
	private Integer bathrooms;
	private String address;
	private Double area;
    private ListingOperation operation;
	private EnergyClass energyClass;
    private Boolean hasElevator;
    private Boolean hasParking;
    private Boolean hasGarden;
    private Boolean hasBalcony;


}