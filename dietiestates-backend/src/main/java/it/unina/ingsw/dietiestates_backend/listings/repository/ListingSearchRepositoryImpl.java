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
        StringBuilder sql = new StringBuilder();
        sql.append("select l.listing_id, l.title, l.description, l.address, l.rooms, l.bathrooms, ");
        sql.append(" l.price, l.area, l.energy_class, ST_X(l.coordinate) as lng, ST_Y(l.coordinate) as lat, ");
        sql.append(" (select li.url from listing_image li where li.listing_id=l.listing_id order by coalesce(li.sort_order,0) asc limit 1) as thumbnail_url, ");
        sql.append(" l.listing_type ");
        sql.append(" from listing l where 1=1 ");

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (searchRequest.getOperation() != null) {
            sql.append(" and l.listing_type = :op ");
            params.addValue("op", searchRequest.getOperation().name());
        }

        if (searchRequest.getEnergyClass() != null) {
            sql.append(" and l.energy_class = :ec ");
            params.addValue("ec", searchRequest.getEnergyClass().name());
        }

        if (searchRequest.getMinPrice() != null) {
            sql.append(" and coalesce(l.price, l.monthly_rent) >= :minPrice ");
            params.addValue("minPrice", searchRequest.getMinPrice());
        }
        if (searchRequest.getMaxPrice() != null) {
            sql.append(" and coalesce(l.price, l.monthly_rent) <= :maxPrice ");
            params.addValue("maxPrice", searchRequest.getMaxPrice());
        }

        if (searchRequest.getMinRooms() != null) {
            sql.append(" and l.rooms >= :minRooms ");
            params.addValue("minRooms", searchRequest.getMinRooms());
        }
        if (searchRequest.getMaxRooms() != null) {
            sql.append(" and l.rooms <= :maxRooms ");
            params.addValue("maxRooms", searchRequest.getMaxRooms());
        }

        if (searchRequest.getMinArea() != null) {
            sql.append(" and l.area >= :minArea ");
            params.addValue("minArea", searchRequest.getMinArea());
        }
        if (searchRequest.getMaxArea() != null) {
            sql.append(" and l.area <= :maxArea ");
            params.addValue("maxArea", searchRequest.getMaxArea());
        }

        if (searchRequest.getLng() != null && searchRequest.getLat() != null && searchRequest.getRadiusKm() != null) {
            sql.append(" and ST_DWithin(l.coordinate::geography, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography, :radiusMeters) ");
            params.addValue("lng", searchRequest.getLng());
            params.addValue("lat", searchRequest.getLat());
            params.addValue("radiusMeters", searchRequest.getRadiusKm() * 1000);
        }

        String countSql = "select count(*) from (" + sql + ") as q";
        Long total = jdbcTemplate.queryForObject(countSql, params, Long.class);

        sql.append(" order by l.listing_id desc ");
        sql.append(" limit :limit offset :offset ");
        params.addValue("limit", pageable.getPageSize());
        params.addValue("offset", pageable.getOffset());

        RowMapper<ListingSummaryResponse> mapper = (rs, rowNum) -> {
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
            dto.setListingOperation(rs.getString("listing_type"));
            return dto;
        };

        List<ListingSummaryResponse> items = jdbcTemplate.query(sql.toString(), params, mapper);
        return new PageImpl<>(items, pageable, total == null ? 0 : total);
    }
}