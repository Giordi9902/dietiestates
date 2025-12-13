package it.unina.ingsw.dietiestates_backend.listings.model.dto.update;

import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.ListingOperation;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateListingRequest {
	private Integer id;
    private String title;
    private String description;
	private Double lng;
	private Double lat;
	private Integer floor;
	@Positive(message = "Il prezzo non può essere negativo")
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
    private List<String> imagesToRemove;
}