package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParserRequestDto {
	
	    Set<String> source;
		LocalDate fromData;
		LocalDate toData;
	    String type;
}
