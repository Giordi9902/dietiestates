package it.unina.ingsw.dietiestates_backend.listings.model.dto.details;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class RentListingSummaryResponse extends ListingSummaryResponse {
	private BigDecimal monthlyRent;
}