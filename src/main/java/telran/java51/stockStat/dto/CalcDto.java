package telran.java51.stockStat.dto;

import java.time.LocalDate;
import java.util.Set;

public class CalcDto {
	
	Set<String> indexs;
	String type;
	Integer quantity;
	LocalDate from;
	LocalDate to;
}
