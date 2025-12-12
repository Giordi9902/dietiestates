package it.unina.ingsw.dietiestates_backend.listings.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeometryService {

	private final GeometryFactory geometryFactory;

	public Point createPoint(double lng, double lat) {
		Point point = geometryFactory.createPoint(new Coordinate(lng, lat));
		point.setSRID(4326);
		return point;
	}
}