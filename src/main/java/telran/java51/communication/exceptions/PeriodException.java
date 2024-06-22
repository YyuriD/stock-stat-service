package telran.java51.communication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Incorrect period type or quantity")
public class PeriodException extends RuntimeException {

	private static final long serialVersionUID = 4866945051173862780L;
	
}
