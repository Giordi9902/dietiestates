package it.unina.ingsw.dietiestates_backend.errors;


import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static it.unina.ingsw.dietiestates_backend.errors.BusinessErrorCodes.ACCOUNT_LOCKED;
import static it.unina.ingsw.dietiestates_backend.errors.BusinessErrorCodes.BAD_CREDENTIALS;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
		return ResponseEntity
				.status(UNAUTHORIZED)
				.body(
						ExceptionResponse.builder()
							.businessErrorCode(ACCOUNT_LOCKED.getCode())
							.businessErrorDescription(ACCOUNT_LOCKED.getDescription())
							.error(exp.getMessage())
							.build()
					);
	}

	@ExceptionHandler(InvalidActivationCodeException.class)
	public ResponseEntity<ExceptionResponse> handleException(InvalidActivationCodeException e){
		return ResponseEntity
				.status(BAD_REQUEST)
				.body(
						ExceptionResponse.builder()
							.businessErrorCode(BusinessErrorCodes.NO_CODE.getCode())
							.businessErrorDescription(e.getMessage())
							.error(e.getMessage())
							.build()
					);
	}


	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleException() {
		return ResponseEntity
				.status(UNAUTHORIZED)
				.body(
						ExceptionResponse.builder()
							.businessErrorCode(BAD_CREDENTIALS.getCode())
							.businessErrorDescription(BAD_CREDENTIALS.getDescription())
							.error("Login and / or Password is incorrect")
							.build()
					);
	}

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ExceptionResponse> handleException(MessagingException exp) {
		return ResponseEntity
				.status(INTERNAL_SERVER_ERROR)
				.body(
						ExceptionResponse.builder()
							.error(exp.getMessage())
							.build()
					);
	}

	@ExceptionHandler(OperationNotPermittedException.class)
	public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exp) {
		return ResponseEntity
				.status(BAD_REQUEST)
				.body(
						ExceptionResponse.builder()
							.error(exp.getMessage())
							.build()
					);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
		Set<String> errors = new HashSet<>();
		exp.getBindingResult().getAllErrors()
				.forEach(error -> {
					var errorMessage = error.getDefaultMessage();
					errors.add(errorMessage);
				});

		return ResponseEntity
				.status(BAD_REQUEST)
				.body(
						ExceptionResponse.builder()
							.validationErrors(errors)
							.build()
					);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
		log.error("Unhandled exception caught in GlobalExceptionHandler", exp);
		return ResponseEntity
				.status(INTERNAL_SERVER_ERROR)
				.body(
						ExceptionResponse.builder()
							.businessErrorDescription("Internal error, please contact the admin")
							.error(exp.getMessage())
							.build()
					);
	}
}