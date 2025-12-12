package it.unina.ingsw.dietiestates_backend.listings.model;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.ListingOperation;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name="listing")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Listing {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "listing_id")
	private Integer id;
	@Enumerated(EnumType.STRING)
	@Column(name="energy_class")
	private EnergyClass energyClass;
	@Enumerated(EnumType.STRING)
	@Column(name="listing_operation")
	private ListingOperation listingOperation;
	private String title;
	private String description;
	private Integer floor;
	private String address;
	private Double area;
	@Column(columnDefinition = "geometry(Point,4326)")
	private Point coordinate;
	private BigDecimal price;
	private Integer rooms;
	private Integer bathrooms;
	private Boolean hasElevator;
	private Boolean hasParking;
	private Boolean hasGarden;
	private Boolean hasBalcony;

	@OneToOne
	@JoinColumn(name = "listed_by_agentId", referencedColumnName = "id")
	private User listedBy;

	@OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ListingImage> images;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;

	public List<ListingImage> getImages() {
		if (images == null) {
			images = new ArrayList<>();
		}
		return images;
	}
}