package it.unina.ingsw.dietiestates_backend.listings.model.dto.details;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SaleListingSummaryResponse extends ListingSummaryResponse {
	private BigDecimal price;
}