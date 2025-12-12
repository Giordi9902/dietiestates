package it.unina.ingsw.dietiestates_backend.listings.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("RENT")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentListing extends Listing {
	@Column(name="monthly_rent", precision = 10, scale=2, nullable = false)
	private BigDecimal monthlyRent;
}