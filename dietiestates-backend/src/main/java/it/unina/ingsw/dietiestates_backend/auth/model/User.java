package it.unina.ingsw.dietiestates_backend.auth.model;

import it.unina.ingsw.dietiestates_backend.agency.model.Agency;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {
	@Id
	@GeneratedValue
	private Integer id;
	private String firstname;
	private String lastname;
	@Column(unique = true)
	private String email;
	private LocalDate dateOfBirth;
	private String password;
	private boolean accountLocked;
	private boolean enabled;
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedDate;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "agency_id")
	private Agency agency;

	@Enumerated(EnumType.STRING)
	private AuthProvider provider;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles;

	@Override
	public String getName() {
		return email;
	}

	public String getFullName(){
		return firstname + " " + lastname;
	}

	@Override
	public boolean isAccountNonExpired(){
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isEnabled(){
		return enabled;
	}

	@Override
	public boolean isCredentialsNonExpired(){
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream()
				.map(r->new SimpleGrantedAuthority(r.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}

	public List<Role> getRoles() {
		if (roles == null) {
			roles = new ArrayList<>();
		}
		return this.roles;
	}
}