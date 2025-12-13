package it.unina.ingsw.dietiestates_backend.auth.service;

import it.unina.ingsw.dietiestates_backend.auth.dto.ChangePasswordRequest;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.email.EmailService;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private EmailService emailService;

	@InjectMocks
	private UserService userService;

	private static final String HASHED_PASSWORD = "$2a$10$hashedpasswordxyz";
	private User mockUser;
	private final Integer userid = 1;

	@BeforeEach
	void setup(){
		mockUser = User.builder()
				.id(userid)
				.password(HASHED_PASSWORD)
				.email("test@example.com")
				.build();
	}

	@Test
	@DisplayName("Aggiornamento password con dati validi")
	void updatePassword_success() throws Exception {
		// GIVEN
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass123");
		String newHashedPassword = "$2a$10$newhashedpass123";
		// Simula la ricerca dell'utente nel DB (Successo)
		when(userRepository.findById(userid)).thenReturn(Optional.of(mockUser));
		// Simula la verifica della vecchia password (Corretta)
		when(passwordEncoder.matches("oldPass", HASHED_PASSWORD)).thenReturn(true);
		// Simula la verifica che la nuova password sia diversa (Diversa)
		when(passwordEncoder.matches("newPass123", HASHED_PASSWORD)).thenReturn(false);
		// Simula la cifratura della nuova password
		when(passwordEncoder.encode("newPass123")).thenReturn(newHashedPassword);

		// WHEN
		userService.updatePassword(mockUser, request);

		// THEN
		// 1. Verifica che la nuova password cifrata sia stata impostata sull'oggetto User
		assertEquals(newHashedPassword, mockUser.getPassword());
		// 2. Verifica che l'oggetto User modificato sia stato salvato nel repository
		verify(userRepository, times(1)).save(mockUser);
		// 3. Verifica che la notifica di cambio password sia stata inviata
		verify(emailService, times(1)).sendPasswordChangedNotification(mockUser);
	}

	@Test
	@DisplayName("P1: Fallimento - currentUser è null")
	void updatePassword_currentUserIsNull_throwsBadCredentialsException() {
		// GIVEN
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass");

		// WHEN & THEN
		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
			userService.updatePassword(null, request);
		});

		assertEquals("Not authenticated", exception.getMessage());
		verify(userRepository, never()).findById(any()); // Verifica che non sia stato chiamato il repository
	}

	@Test
	@DisplayName("P2: Fallimento - Utente non trovato nel repository")
	void updatePassword_userNotFoundInRepository_throwsUsernameNotFoundException() throws MessagingException {
		// GIVEN
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass");

		// Simula la ricerca (Fallimento)
		when(userRepository.findById(userid)).thenReturn(Optional.empty());

		// WHEN & THEN
		assertThrows(UsernameNotFoundException.class, () -> {
			userService.updatePassword(mockUser, request);
		}, "User not found");

		verify(userRepository, times(1)).findById(userid);
		verify(userRepository, never()).save(any());
		verify(emailService, never()).sendPasswordChangedNotification(any());
	}

	@Test
	@DisplayName("P3: Fallimento - Vecchia password non corretta")
	void updatePassword_oldPasswordIncorrect_throwsBadCredentialsException() {
		// GIVEN
		ChangePasswordRequest request = new ChangePasswordRequest("wrongPass", "newPass");

		when(userRepository.findById(userid)).thenReturn(Optional.of(mockUser));
		// Simula la verifica della vecchia password (Errata)
		when(passwordEncoder.matches("wrongPass", HASHED_PASSWORD)).thenReturn(false);

		// WHEN & THEN
		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
			userService.updatePassword(mockUser, request);
		});

		assertEquals("Old password is incorrect", exception.getMessage());
		verify(userRepository, never()).save(any());
	}

	@Test
	@DisplayName("P4: Fallimento - Nuova password vuota o blank")
	void updatePassword_newPasswordIsBlank_throwsBadRequestException() {
		// GIVEN
		// Testando il caso blank/vuoto
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "   ");

		when(userRepository.findById(userid)).thenReturn(Optional.of(mockUser));
		when(passwordEncoder.matches("oldPass", HASHED_PASSWORD)).thenReturn(true);

		// WHEN & THEN
		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			userService.updatePassword(mockUser, request);
		});

		assertEquals("New password must not be blank", exception.getMessage());
		verify(userRepository, never()).save(any());
	}

	@Test
	@DisplayName("P5: Fallimento - Nuova password uguale alla vecchia")
	void updatePassword_newPasswordIsSame_throwsBadRequestException() {
		// GIVEN
		// Il campo newPass è "oldPass", che corrisponde a HASHED_PASSWORD
		ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "oldPass");

		when(userRepository.findById(userid)).thenReturn(Optional.of(mockUser));
		when(passwordEncoder.matches("oldPass", HASHED_PASSWORD)).thenReturn(true); // Vecchia password OK
		// Simula la verifica che la nuova password sia diversa (Risulta UGUALE)
		when(passwordEncoder.matches("oldPass", HASHED_PASSWORD)).thenReturn(true);

		// WHEN & THEN
		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			userService.updatePassword(mockUser, request);
		});

		assertEquals("New password must be different from the old password", exception.getMessage());
		verify(userRepository, never()).save(any());
	}
}