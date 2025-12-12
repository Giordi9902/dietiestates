package it.unina.ingsw.dietiestates_backend.listings.repository;

import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.searching.ListingSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListingSearchRepository {
    Page<ListingSummaryResponse> searchListingsByFilters(ListingSearchRequest searchRequest, Pageable pageable);
}