package it.unina.ingsw.dietiestates_backend.auth.service;

import it.unina.ingsw.dietiestates_backend.auth.controller.UserController;
import it.unina.ingsw.dietiestates_backend.auth.dto.ChangePasswordRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.UpdateProfileRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.UserDetailsResponse;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	public void updatePassword(User currentUser, ChangePasswordRequest request) throws BadRequestException, MessagingException {
		if (currentUser == null) {
			throw new org.springframework.security.authentication.BadCredentialsException("Not authenticated");
		}
		var user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"));

		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			throw new org.springframework.security.authentication.BadCredentialsException("Old password is incorrect");
		}
		if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
			throw new BadRequestException("New password must not be blank");
		}
		if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
			throw new BadRequestException("New password must be different from the old password");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		emailService.sendPasswordChangedNotification(user);
	}

	public void updateProfile(User current, UpdateProfileRequest request) {
		if (current == null) {
			throw new org.springframework.security.authentication.BadCredentialsException("Not authenticated");
		}
		var user = userRepository.findById(current.getId())
				.orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"));

		if (request.getFirstname() != null && !request.getFirstname().isBlank()) {
			user.setFirstname(request.getFirstname());
		}
		if (request.getLastname() != null && !request.getLastname().isBlank()) {
			user.setLastname(request.getLastname());
		}
		if (request.getEmail() != null && !request.getEmail().isBlank()) {
			user.setEmail(request.getEmail());
		}
		if (request.getBirthdate() != null) {
			user.setDateOfBirth(request.getBirthdate());
		}
		userRepository.save(user);
	}

	public UserDetailsResponse getUserDetails(User current) {
		if (current == null) {
			throw new org.springframework.security.authentication.BadCredentialsException("Not authenticated");
		}
		var user = userRepository.findById(current.getId())
				.orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"));
		return new UserDetailsResponse(
				user.getFirstname(),
				user.getLastname(),
				user.getEmail(),
				user.getDateOfBirth());
	}
}