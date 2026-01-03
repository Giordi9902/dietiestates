package it.unina.ingsw.dietiestates_backend.listings.repository;

import it.unina.ingsw.dietiestates_backend.listings.model.dto.details.ListingSummaryResponse;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.searching.ListingSearchRequest;
import it.unina.ingsw.dietiestates_backend.listings.service.ListingImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ListingSearchRepositoryImpl implements ListingSearchRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ListingImageService listingImageService;

    @Override
    public Page<ListingSummaryResponse> searchListingsByFilters(ListingSearchRequest searchRequest, Pageable pageable) {
        String sql = "select listing_id, title, description, address, rooms, bathrooms, price, area, energy_class, " +
            "lng_out as lng, lat_out as lat, thumbnail_url, listing_operation, total_count " +
            "from search_listings_by_filters(" +
            "CAST(:op AS text), " +
            "CAST(:ec AS text), " +
            "CAST(:minPrice AS numeric), " +
            "CAST(:maxPrice AS numeric), " +
            "CAST(:minRooms AS int), " +
            "CAST(:maxRooms AS int), " +
            "CAST(:minArea AS double precision), " +
            "CAST(:maxArea AS double precision), " +
            "CAST(:lng AS double precision), " +
            "CAST(:lat AS double precision), " +
            "CAST(:radiusKm AS double precision), " +
            "CAST(:limit AS int), " +
            "CAST(:offset AS bigint))";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("op", searchRequest.getOperation() != null ? searchRequest.getOperation().name() : null);
        params.addValue("ec", searchRequest.getEnergyClass() != null ? searchRequest.getEnergyClass().name() : null);
        params.addValue("minPrice", searchRequest.getMinPrice());
        params.addValue("maxPrice", searchRequest.getMaxPrice());
        params.addValue("minRooms", searchRequest.getMinRooms());
        params.addValue("maxRooms", searchRequest.getMaxRooms());
        params.addValue("minArea", searchRequest.getMinArea());
        params.addValue("maxArea", searchRequest.getMaxArea());
        params.addValue("lng", searchRequest.getLng());
        params.addValue("lat", searchRequest.getLat());
        params.addValue("radiusKm", searchRequest.getRadiusKm());
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());

        final long[] totalHolder = new long[1];

        RowMapper<ListingSummaryResponse> mapper = (rs, rowNum) -> {
            if (rowNum == 0) {
                totalHolder[0] = rs.getLong("total_count");
            }
            ListingSummaryResponse dto = new ListingSummaryResponse();
            dto.setListing_id(rs.getInt("listing_id"));
            dto.setTitle(rs.getString("title"));
            dto.setDescription(rs.getString("description"));
            dto.setAddress(rs.getString("address"));
            dto.setRooms(rs.getObject("rooms") != null ? rs.getInt("rooms") : null);
            dto.setBathrooms(rs.getObject("bathrooms") != null ? rs.getInt("bathrooms") : null);
            dto.setPrice(rs.getBigDecimal("price"));
            dto.setArea(rs.getObject("area") != null ? rs.getDouble("area") : null);
            dto.setEnergyClass(rs.getString("energy_class"));
            dto.setLongitude(rs.getObject("lng") != null ? rs.getDouble("lng") : null);
            dto.setLatitude(rs.getObject("lat") != null ? rs.getDouble("lat") : null);
            String thumb = rs.getString("thumbnail_url");
            if (thumb != null && !thumb.isBlank()) {
                dto.setThumbnailUrl(listingImageService.presign(thumb));
            }
            dto.setListingOperation(rs.getString("listing_operation"));
            return dto;
        };

        List<ListingSummaryResponse> items = jdbcTemplate.query(sql, params, mapper);
        long total = items.isEmpty() ? 0 : totalHolder[0];
        return new PageImpl<>(items, pageable, total);
    }
}