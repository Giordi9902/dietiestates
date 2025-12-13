package it.unina.ingsw.dietiestates_backend.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
	String timestamp;
	int status;
	String error;
	String code;
	String message;
	String path;
	List<Violation> violations;

	@Value
	@Builder
	public static class Violation{
		String field;
		String message;
	}
}