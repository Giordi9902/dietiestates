package it.unina.ingsw.dietiestates_backend.errors;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.unina.ingsw.dietiestates_backend.errors.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ BadCredentialsException.class, InvalidCredentialsException.class })
	public ResponseEntity<ApiError> handleBadCredentials(Exception ex, HttpServletRequest req) {
		return build(UNAUTHORIZED, "AUTH_BAD_CREDENTIALS", "Credenziali non valide.", req, null);
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<ApiError> handleDisabled(DisabledException ex, HttpServletRequest req) {
		return build(UNAUTHORIZED, "AUTH_USER_DISABLED", "Account non attivato o disabilitato.", req, null);
	}

	@ExceptionHandler({ UsernameNotFoundException.class })
	public ResponseEntity<ApiError> handleUserNotFound(Exception ex, HttpServletRequest req) {
		return build(UNAUTHORIZED, "AUTH_USER_NOT_FOUND", "Utente non trovato.", req, null);
	}

	@ExceptionHandler({ ExpiredJwtException.class })
	public ResponseEntity<ApiError> handleJwtExpired(ExpiredJwtException ex, HttpServletRequest req) {
		return build(UNAUTHORIZED, "JWT_EXPIRED", "Sessione scaduta. Effettua nuovamente l’accesso.", req, null);
	}

	@ExceptionHandler({ SignatureException.class, MalformedJwtException.class, IllegalArgumentException.class })
	public ResponseEntity<ApiError> handleJwtInvalid(RuntimeException ex, HttpServletRequest req) {
		return build(UNAUTHORIZED, "JWT_INVALID", "Token non valido.", req, null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleMethodArgNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
		List<ApiError.Violation> vs = ex.getBindingResult().getFieldErrors().stream()
				.map(this::toViolation).toList();
		return build(UNPROCESSABLE_ENTITY, "VALIDATION_ERROR", "Dati non validi.", req, vs);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
		List<ApiError.Violation> vs = ex.getConstraintViolations().stream()
				.map(cv -> ApiError.Violation.builder()
						.field(cv.getPropertyPath().toString())
						.message(cv.getMessage())
						.build())
				.toList();
		return build(UNPROCESSABLE_ENTITY, "VALIDATION_ERROR", "Dati non validi.", req, vs);
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex, HttpServletRequest req) {
		return build(FORBIDDEN, "ACCESS_DENIED", "Permesso negato.", req, null);
	}

	@ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleNotReadable(Exception ex, HttpServletRequest req) {
		return build(BAD_REQUEST, "MALFORMED_JSON", "Payload non leggibile.", req, null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
		return build(INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Errore interno inatteso.", req, null);
	}

	private ResponseEntity<ApiError> build(HttpStatus status, String code, String message,
										   HttpServletRequest req, List<ApiError.Violation> violations) {
		ApiError body = ApiError.builder()
				.timestamp(Instant.now())
				.status(status.value())
				.error(status.getReasonPhrase())
				.code(code)
				.message(message)
				.path(req.getRequestURI())
				.violations(violations == null || violations.isEmpty() ? null : violations)
				.build();
		return ResponseEntity.status(status).body(body);
	}

	private ApiError.Violation toViolation(FieldError fe) {
		return ApiError.Violation.builder()
				.field(fe.getField())
				.message(fe.getDefaultMessage())
				.build();
	}
}