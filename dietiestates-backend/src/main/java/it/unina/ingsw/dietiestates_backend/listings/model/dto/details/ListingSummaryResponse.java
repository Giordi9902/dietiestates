package it.unina.ingsw.dietiestates_backend.listings.model.dto.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListingSummaryResponse {
	private Integer listing_id;
	private String title;
	private String description;
	private String address;
	private Integer rooms;
	private Integer bathrooms;
	private BigDecimal price;
	private String listingOperation;
	private Double area;
	private String energyClass;
	private Double longitude;
	private Double latitude;
	private String thumbnailUrl;

}