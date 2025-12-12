package it.unina.ingsw.dietiestates_backend.listings.model.dto.creational;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSaleListingRequest extends CreateListingRequest {
    @NotNull
    private BigDecimal price;
}