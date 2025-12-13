package it.unina.ingsw.dietiestates_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unina.ingsw.dietiestates_backend.errors.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		var status = HttpStatus.UNAUTHORIZED;
		var body = ApiError.builder()
				.timestamp(LocalDateTime.now().toString())
				.status(status.value())
				.error(status.getReasonPhrase())
				.code("UNAUTHORIZED")
				.message("Autenticazione richiesta o token non valido.")
				.path(request.getRequestURI())
				.build();

		response.setStatus(status.value());
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), body);
	}
}