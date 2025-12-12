package it.unina.ingsw.dietiestates_backend.listings.repository;

import it.unina.ingsw.dietiestates_backend.listings.model.ListingImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingImageRepository extends JpaRepository<ListingImage, Integer> {
	List<ListingImage> findByListingId(Integer listingId);
}