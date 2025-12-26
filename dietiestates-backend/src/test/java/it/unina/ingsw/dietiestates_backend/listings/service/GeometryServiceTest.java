package it.unina.ingsw.dietiestates_backend.listings.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import static org.junit.jupiter.api.Assertions.*;

class GeometryServiceTest {
	private GeometryFactory geometryFactory;
	private GeometryService geometryService;
	private final double VALID_COORD = 10.0;
	
	@BeforeEach
	void setUp(){
		geometryFactory = new GeometryFactory();
		geometryService = new GeometryService(geometryFactory);
	}

	@Test
	void createPoint_ValidCoordinates_ReturnsCorrectPoint(){
		// GIVEN
		double expectedLng = 10.50; // Longitudine valida
		double expectedLat = 45.75; // Latitudine valida

		// WHEN
		Point resultPoint = geometryService.createPoint(expectedLng, expectedLat);

		// THEN
		assertNotNull(resultPoint, "Il punto non deve essere null.");

		// Verifica che le coordinate siano state impostate correttamente
		assertEquals(expectedLng, resultPoint.getX(), 0.00001, "La Longitudine (X) non corrisponde.");
		assertEquals(expectedLat, resultPoint.getY(), 0.00001, "La Latitudine (Y) non corrisponde.");

		// Verifica lo SRID
		assertEquals(4326, resultPoint.getSRID(), "L'SRID deve essere impostato a 4326.");
	}

	@Test
	void createPoint_BoundaryCoordinates_ReturnsCorrectPoint() {
		// GIVEN
		double minLng = -180.0;
		double maxLat = 90.0;

		// WHEN
		Point resultPoint = geometryService.createPoint(minLng, maxLat);

		// THEN
		assertNotNull(resultPoint);
		assertEquals(minLng, resultPoint.getX(), 0.00001, "La Longitudine minima non corrisponde.");
		assertEquals(maxLat, resultPoint.getY(), 0.00001, "La Latitudine massima non corrisponde.");
		assertEquals(4326, resultPoint.getSRID(), "L'SRID deve essere 4326 anche ai limiti.");
	}

	@Test
	void createPoint_ZeroCoordinates_ReturnsCorrectPoint() {
		// GIVEN
		double zeroLng = 0.0;
		double zeroLat = 0.0;

		// WHEN
		Point resultPoint = geometryService.createPoint(zeroLng, zeroLat);

		// THEN
		assertNotNull(resultPoint);
		assertEquals(zeroLng, resultPoint.getX(), 0.00001, "La Longitudine zero non corrisponde.");
		assertEquals(zeroLat, resultPoint.getY(), 0.00001, "La Latitudine zero non corrisponde.");
		assertEquals(4326, resultPoint.getSRID(), "L'SRID deve essere 4326.");
	}

	@Test
	void createPoint_NegativeCoordinates_ReturnsCorrectPoint() {
		// GIVEN
		double negativeLng = -50.123;
		double negativeLat = -10.987;

		// WHEN
		Point resultPoint = geometryService.createPoint(negativeLng, negativeLat);

		// THEN
		assertNotNull(resultPoint);
		assertEquals(negativeLng, resultPoint.getX(), 0.00001, "La Longitudine negativa non corrisponde.");
		assertEquals(negativeLat, resultPoint.getY(), 0.00001, "La Latitudine negativa non corrisponde.");
		assertEquals(4326, resultPoint.getSRID(), "L'SRID deve essere 4326.");
	}

	@Test
	void createPoint_OutOfRangeCoordinates_PointIsCreated() {
		// GIVEN
		double invalidLng = 200.0; // Oltre 180
		double invalidLat = 100.0;  // Oltre 90

		// WHEN
		Point resultPoint = geometryService.createPoint(invalidLng, invalidLat);

		// THEN
		assertNotNull(resultPoint);
		assertEquals(invalidLng, resultPoint.getX(), 0.00001);
		assertEquals(invalidLat, resultPoint.getY(), 0.00001);
		assertEquals(4326, resultPoint.getSRID(), "L'SRID deve essere 4326 anche con coordinate invalide.");
	}

	@Nested
	@DisplayName("When Longitude is invalid")
	class LongitudeTests {

		@Test
		@DisplayName("should throw exception for NaN")
		void shouldThrowExceptionForNaN() {
			assertThrows(IllegalArgumentException.class,
					() -> geometryService.createPoint(Double.NaN, VALID_COORD));
		}

		@Test
		@DisplayName("should throw exception for Positive Infinity")
		void shouldThrowExceptionForPositiveInfinity() {
			assertThrows(IllegalArgumentException.class,
					() -> geometryService.createPoint(Double.POSITIVE_INFINITY, VALID_COORD));
		}

		@Test
		@DisplayName("should throw exception for Negative Infinity")
		void shouldThrowExceptionForNegativeInfinity() {
			assertThrows(IllegalArgumentException.class,
					() -> geometryService.createPoint(Double.NEGATIVE_INFINITY, VALID_COORD));
		}
	}

	@Nested
	@DisplayName("When Latitude is invalid")
	class LatitudeTests {

		@Test
		@DisplayName("should throw exception for NaN")
		void shouldThrowExceptionForNaN() {
			assertThrows(IllegalArgumentException.class,
					() -> geometryService.createPoint(VALID_COORD, Double.NaN));
		}

		@Test
		@DisplayName("should throw exception for Positive Infinity")
		void shouldThrowExceptionForPositiveInfinity() {
			assertThrows(IllegalArgumentException.class,
					() -> geometryService.createPoint(VALID_COORD, Double.POSITIVE_INFINITY));
		}

		@Test
		@DisplayName("should throw exception for Negative Infinity")
		void shouldThrowExceptionForNegativeInfinity() {
			assertThrows(IllegalArgumentException.class,
					() -> geometryService.createPoint(VALID_COORD, Double.NEGATIVE_INFINITY));
		}
	}
}