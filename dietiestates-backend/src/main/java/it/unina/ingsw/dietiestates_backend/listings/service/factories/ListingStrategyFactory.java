package it.unina.ingsw.dietiestates_backend.listings.service.factories;

import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ListingStrategyFactory {
	private final Map<Class<?>, ListingStrategy<?>> byRequest;
	private final Map<Class<? extends Listing>, ListingStrategy<?>> byListing;

	public ListingStrategyFactory(
			List<ListingStrategy<?>> strategyList
	){
		this.byRequest = strategyList.stream()
				.collect(Collectors.toMap(ListingStrategy::getRequestType, Function.identity()));
		this.byListing = strategyList.stream()
				.collect(Collectors.toMap(ListingStrategy::getListingType, Function.identity()));
	}

	@SuppressWarnings("unchecked")
	public <T> ListingStrategy<T> getStrategy(Class<T> requestType) {
		ListingStrategy<?> strategy = byRequest.get(requestType);
		if (strategy == null) {
			throw new IllegalArgumentException("Unsupported listing request type: " + requestType);
		}
		return (ListingStrategy<T>) strategy;
	}

	@SuppressWarnings("unchecked")
	public <L extends Listing> ListingStrategy<?> getStrategyForListing(Class<L> listingType) {
		ListingStrategy<?> strategy = byListing.get(listingType);
		if (strategy == null) {
			throw new IllegalArgumentException("Unsupported listing entity type: " + listingType);
		}
		return strategy;
	}
}