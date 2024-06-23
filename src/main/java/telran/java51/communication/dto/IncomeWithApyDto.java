package telran.java51.communication.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import telran.java51.communication.model.Income;

@Getter
@Setter
@AllArgsConstructor
public class IncomeWithApyDto {

    LocalDate from;
    LocalDate to;  
    List<String> source; 
    String type;   
    Income minIncome;       
    Income maxIncome;
    
}
