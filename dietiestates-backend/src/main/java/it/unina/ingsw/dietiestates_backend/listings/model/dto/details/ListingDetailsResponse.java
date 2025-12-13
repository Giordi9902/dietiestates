package it.unina.ingsw.dietiestates_backend.listings.model.dto.details;

import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.ListingOperation;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListingDetailsResponse {
	private Integer id;
	private String title;
	private String description;
	private String address;
	private Double lng;
	private Double lat;
	private BigDecimal price;
	private Integer rooms;
	private Integer bathrooms;
	private Double area;
	private EnergyClass energyClass;
	private ListingOperation listingOperation;
	private Boolean hasElevator;
	private Boolean hasParking;
	private Boolean hasGarden;
	private Boolean hasBalcony;
	@Builder.Default
	private List<String> imageUrls = new ArrayList<>();
}