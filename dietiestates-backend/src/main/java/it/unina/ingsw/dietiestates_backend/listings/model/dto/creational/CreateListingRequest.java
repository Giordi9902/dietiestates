package it.unina.ingsw.dietiestates_backend.listings.model.dto.creational;

import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateListingRequest {
	@NotEmpty
	private String title;
	@NotEmpty
	private String description;
	private Double lng;
	private Double lat;
	private Integer floor;
	private Integer rooms;
	private Integer bathrooms;
	private String address;
	@NotNull
	private Double area;
	@NotNull
	private EnergyClass energyClass;
	private Boolean hasElevator;
	private Boolean hasParking;
	private Boolean hasBalcony;
	private Boolean hasGarden;
}