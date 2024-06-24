package telran.java51.communication.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodBeetwinDto {

	LocalDate from;
    LocalDate to;
    String source; 
    String type;
    BigDecimal max;
    BigDecimal mean;
    BigDecimal median;
    BigDecimal min;
    BigDecimal std;
}
