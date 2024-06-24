package telran.java51.communication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;
@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Incorrect parameters")
public class WrongParametersException extends RuntimeException {

	private static final long serialVersionUID = 4866945051173862780L;
	
	public WrongParametersException(String message) {
		
	}
}
