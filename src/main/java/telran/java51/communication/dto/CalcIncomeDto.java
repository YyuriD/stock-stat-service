package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;

@Getter
public class CalcIncomeDto {
	
	Set<String> indexs;
	String type;
	Integer quantity;
	LocalDate from;
	LocalDate to;
}
