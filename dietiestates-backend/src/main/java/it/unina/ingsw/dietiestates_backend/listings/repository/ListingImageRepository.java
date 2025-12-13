package it.unina.ingsw.dietiestates_backend.listings.repository;

import it.unina.ingsw.dietiestates_backend.listings.model.ListingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListingImageRepository extends JpaRepository<ListingImage, Integer> {
	List<ListingImage> findByListingId(Integer listingId);

	void deleteByUrl(String url);

	@Query("SELECT COALESCE(MAX(li.sortOrder), 0) FROM ListingImage li WHERE li.listing.id = :listingId")
	Integer findMaxSortOrderByListingId(@Param("listingId") Integer listingId);
}