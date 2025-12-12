package it.unina.ingsw.dietiestates_backend.agency.model;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="agencies")
public class Agency {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, nullable = false)
	private String name;
	private String phoneNumber;
	private String email;

	@OneToMany(mappedBy="agency", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> agents = new ArrayList<>();
}