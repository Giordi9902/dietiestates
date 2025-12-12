package it.unina.ingsw.dietiestates_backend.listings.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SALE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleListing extends Listing {
	@Column(name="sale_price", precision = 10, scale=2, nullable = false)
	private BigDecimal price;
}