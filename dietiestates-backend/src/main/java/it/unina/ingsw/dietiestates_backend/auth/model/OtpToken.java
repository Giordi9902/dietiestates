package it.unina.ingsw.dietiestates_backend.auth.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OtpToken {
	@Id
	@GeneratedValue
	private Integer id;
	private String token;
	private LocalDateTime createdAt;
	private LocalDateTime expiresAt;
	private LocalDateTime validatedAt;
	@ManyToOne
	@JoinColumn(name="userId",nullable=false)
	private User user;
}