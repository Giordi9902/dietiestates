package it.unina.ingsw.dietiestates_backend.errors;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import it.unina.ingsw.dietiestates_backend.errors.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static it.unina.ingsw.dietiestates_backend.errors.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
@Slf4j
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

	@ExceptionHandler(ListingNotFoundException.class)
	public ResponseEntity<ApiError> handleListingNotFound(ListingNotFoundException ex, HttpServletRequest req) {
		return build(LISTING_NOT_FOUND.getHttpStatus(), LISTING_NOT_FOUND.name(), ex.getMessage(), req, null);
	}

	@ExceptionHandler(AccountNotActivatedException.class)
	public ResponseEntity<ApiError> handleAccountNotActivated(AccountNotActivatedException ex, HttpServletRequest req) {
		return build(ACCOUNT_NOT_ACTIVATED.getHttpStatus(), ACCOUNT_NOT_ACTIVATED.name(), ex.getMessage(), req, null);
	}

	@ExceptionHandler(AgentWithAdminRoleException.class)
	public ResponseEntity<ApiError> handleAgentWithAdminRole(AgentWithAdminRoleException ex, HttpServletRequest req) {
		return build(AGENT_CANNOT_BE_ADMIN.getHttpStatus(), AGENT_CANNOT_BE_ADMIN.name(), ex.getMessage(), req, null);
	}

	@ExceptionHandler(CannotRevokeAdminException.class)
	public ResponseEntity<ApiError> handleCannotRevokeAdmin(CannotRevokeAdminException ex, HttpServletRequest req) {
		return build(CANNOT_REVOKE_ADMIN.getHttpStatus(), CANNOT_REVOKE_ADMIN.name(), ex.getMessage(), req, null);
	}

	@ExceptionHandler(EmailAlreadyPresentException.class)
	public ResponseEntity<ApiError> handleEmailAlreadyPresent(EmailAlreadyPresentException ex, HttpServletRequest req) {
		return build(EMAIL_ALREADY_EXISTS.getHttpStatus(), EMAIL_ALREADY_EXISTS.name(), ex.getMessage(), req, null);
	}

	@ExceptionHandler(InvalidActivationCodeException.class)
	public ResponseEntity<ApiError> handleInvalidActivationCode(InvalidActivationCodeException ex, HttpServletRequest req) {
		return build(INVALID_ACTIVATION_CODE.getHttpStatus(), INVALID_ACTIVATION_CODE.name(), ex.getMessage(), req, null);
	}

	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<ApiError> handleInvalidRefreshToken(InvalidRefreshTokenException ex, HttpServletRequest req) {
		return build(INVALID_REFRESH_TOKEN.getHttpStatus(), INVALID_REFRESH_TOKEN.name(), ex.getMessage(), req, null);
	}

	@ExceptionHandler(OperationNotPermittedException.class)
	public ResponseEntity<ApiError> handleOperationNotPermitted(OperationNotPermittedException ex, HttpServletRequest req) {
		return build(OPERATION_NOT_PERMITTED.getHttpStatus(), OPERATION_NOT_PERMITTED.name(), ex.getMessage(), req, null);
	}

	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex, HttpServletRequest req) {
		return build(FORBIDDEN, "ACCESS_DENIED", "Permesso negato.", req, null);
	}

	@ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleNotReadable(Exception ex, HttpServletRequest req) {
		log.error("Payload non leggibile on {}: {}", req.getRequestURI(), ex.getMessage(), ex);
		return build(BAD_REQUEST, "MALFORMED_JSON", "Payload non leggibile.", req, null);
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<ApiError> handlePSQLException(SQLException ex, HttpServletRequest req) {
		String sqlState = ex.getSQLState();
		HttpStatus status = BAD_REQUEST;
		if (sqlState != null && sqlState.startsWith("23")) {
			status = CONFLICT;
		}
		log.warn("Database error on {}: sqlState={} message={}", req.getRequestURI(), sqlState, ex.getMessage());
		String message = ex.getMessage();
		if (message != null && message.length() > 200) message = message.substring(0, 200) + "...";
		return build(status, "DATABASE_ERROR", "Errore database: " + (message != null ? message : "errore interno"), req, null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
		log.error("Unhandled exception on {}: {}", req.getRequestURI(), ex.getMessage(), ex);
		return build(INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Errore interno inatteso.", req, null);
	}

	private ResponseEntity<ApiError> build(HttpStatus status, String code, String message,
										   HttpServletRequest req, List<ApiError.Violation> violations) {
		ApiError body = ApiError.builder()
				.timestamp(LocalDateTime.now().toString())
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