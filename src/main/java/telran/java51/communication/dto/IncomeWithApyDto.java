package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.Set;

import telran.java51.communication.model.Income;

public class IncomeWithApyDto {

    LocalDate from;
    LocalDate to;  
    Set<String> source; 
    String type;   
    Income minIncome;       
    Income maxIncome;
    
}
