package it.unina.ingsw.dietiestates_backend.listings.model.dto.details;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "listingOperation"
)

@JsonSubTypes({
		@JsonSubTypes.Type(value = SaleListingSummaryResponse.class, name = "SALE"),
		@JsonSubTypes.Type(value = RentListingSummaryResponse.class, name = "RENT")
})
@Getter
@Setter
public abstract class ListingSummaryResponse {
	private Integer listing_id;
	private String title;
	private String address;
	private String description;
	private Integer rooms;
	private Integer bathrooms;
	private String listingOperation;
	private Double area;
	private String energyClass;
	private Double longitude;
	private Double latitude;
	private String thumbnailUrl;

}