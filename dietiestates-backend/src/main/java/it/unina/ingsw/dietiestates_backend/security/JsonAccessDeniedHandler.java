package it.unina.ingsw.dietiestates_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unina.ingsw.dietiestates_backend.errors.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class JsonAccessDeniedHandler implements AccessDeniedHandler {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException {
		var status = HttpStatus.FORBIDDEN;
		var body = ApiError.builder()
				.timestamp(LocalDateTime.now().toString())
				.status(status.value())
				.error(status.getReasonPhrase())
				.code("ACCESS_DENIED")
				.message("Permesso negato.")
				.path(request.getRequestURI())
				.build();

		response.setStatus(status.value());
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), body);
	}
}