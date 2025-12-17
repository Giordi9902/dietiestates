package it.unina.ingsw.dietiestates_backend.auth.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate dateOfBirth;
}
