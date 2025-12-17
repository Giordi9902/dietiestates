package it.unina.ingsw.dietiestates_backend.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unina.ingsw.dietiestates_backend.auth.dto.ChangePasswordRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.UpdateProfileRequest;
import it.unina.ingsw.dietiestates_backend.auth.dto.UserDetailsResponse;
import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name="Profile")
public class UserController {
	private final UserService userService;

    @PutMapping(value = "/me/password", produces = "application/json")
    public ResponseEntity<String> changeMyPassword(
            @AuthenticationPrincipal User currentUser,
            @RequestBody ChangePasswordRequest request
    ) throws BadRequestException, MessagingException {
        userService.updatePassword(currentUser, request);
        return ResponseEntity.ok().body("\"Password updated\"");
    }

    @PutMapping(value="/me/update", produces="application/json")
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal User current,
            @RequestBody UpdateProfileRequest request
    ){
        userService.updateProfile(current,request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/me", produces="application/json")
    public ResponseEntity<UserDetailsResponse> profileDetails(@AuthenticationPrincipal User current){
        return ResponseEntity.ok(userService.getUserDetails(current));
    }
}