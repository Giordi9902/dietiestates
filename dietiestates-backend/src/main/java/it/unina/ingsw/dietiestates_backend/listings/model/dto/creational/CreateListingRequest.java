package it.unina.ingsw.dietiestates_backend.listings.model.dto.creational;

import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.ListingOperation;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

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
	@NotNull
	private Double lng;
	@NotNull
	private Double lat;
	private String address;
	@NotEmpty
	@NotNull(message="Il prezzo è obbligatorio")
	@Positive(message= "Il prezzo deve essere un valore positivo")
	@Digits(integer = 8, fraction = 2, message = "Formato prezzo non valido")
	private BigDecimal price;
	@NotNull
	private Double area;
	@NotNull
	private EnergyClass energyClass;
	@NotNull
	private ListingOperation listingOperation;

	private Integer floor;
	private Integer rooms;
	private Integer bathrooms;
	private Boolean hasElevator;
	private Boolean hasParking;
	private Boolean hasBalcony;
	private Boolean hasGarden;
}