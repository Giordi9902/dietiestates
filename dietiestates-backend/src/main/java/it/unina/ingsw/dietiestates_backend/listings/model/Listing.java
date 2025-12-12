package it.unina.ingsw.dietiestates_backend.listings.model;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Table(name="listing")
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="listing_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Listing {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "listing_id")
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name="energy_class")
	private EnergyClass energyClass;

	private String title;
	private String description;
	private Integer floor;

	private String address;
	private Double area;

	@Column(columnDefinition = "geometry(Point,4326)")
	private Point coordinate;


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

	public List<ListingImage> getImages() {
		if (images == null) {
			images = new ArrayList<>();
		}
		return images;
	}
}