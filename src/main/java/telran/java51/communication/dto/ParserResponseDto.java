package telran.java51.communication.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParserResponseDto {

	  UploadInfo uploadInfoId;    
      BigDecimal close;
      BigInteger volume;
      BigDecimal open;
      BigDecimal high;
      BigDecimal low;
}
