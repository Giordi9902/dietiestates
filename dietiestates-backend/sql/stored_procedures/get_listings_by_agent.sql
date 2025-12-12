CREATE OR REPLACE FUNCTION get_listings_by_agent(p_agent_id INT)
    RETURNS TABLE (
                      listing_id INT,
                      title VARCHAR,
                      description varchar,
                      address VARCHAR,
                      price DOUBLE PRECISION,
                      rooms INT,
                      bathrooms INT,
                      listingOperation varchar,
                      area DOUBLE PRECISION,
                      energy_class VARCHAR,
                      longitude DOUBLE PRECISION,
                      latitude DOUBLE PRECISION,
                      thumbnail_url VARCHAR
                  )
AS $$
BEGIN
    RETURN QUERY
        SELECT
            l.listing_id,
            l.title,
            l.address,
            l.description,
            l.price,
            l.rooms,
            l.bathrooms,
            l.operation,
            l.area,
            l.energy_class,
            ST_X(l.coordinate) AS longitude,
            ST_Y(l.coordinate) AS latitude,
            (
                SELECT url
                FROM listing_image li
                WHERE li.listing_id = l.listing_id
                ORDER BY COALESCE(li.sort_order, 0) ASC
                LIMIT 1
            ) AS thumbnail_url
        FROM
            listing l
        WHERE
            l.listed_by_agent_id = p_agent_id;
END;
$$ LANGUAGE plpgsql;