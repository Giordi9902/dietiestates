package it.unina.ingsw.dietiestates_backend.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    @Email
    private String email;
    private LocalDate birthdate;
}
