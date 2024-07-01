package telran.java51.communication.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllValueCloseBetweenDto {
	
        LocalDate from;
        LocalDate to;
        String source;
        String type;
        LocalDate minDate;
        LocalDate maxDate;
        BigDecimal startClose;
        BigDecimal endClose;
        BigDecimal valueClose;
        List<BigDecimal>listClose;

}
