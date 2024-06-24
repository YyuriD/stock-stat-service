package telran.java51.communication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Source not found")
public class SourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8639892679725003104L;
		
	public SourceNotFoundException(String message) {
		
	}

}
