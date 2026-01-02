package it.unina.ingsw.dietiestates_backend.listings.repository;

import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing,Integer>{
	@Query(value="select l.listed_by_agent_id from listing l where l.listing_id=:listingId",nativeQuery = true)
	Optional<Integer> findAgentIdByListingId(@Param("listingId") Integer listingId);


	@Query(value = "SELECT * FROM get_listings_by_agent(:agentId)",
			countQuery = "SELECT count(*) FROM get_listings_by_agent(:agentId)",nativeQuery = true)
	Page<ListingSummaryResponse> findListingsByAgentId(@Param("agentId") Integer listed_by_agentId, Pageable pageable);
}