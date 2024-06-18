package telran.java51.communication.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"tickerName","date"})
@Entity
@ToString
@Table(name = "trading_sessions")
@IdClass(TradingSessionId.class)
public class TradingSession  implements Serializable{

	private static final long serialVersionUID = -179664029256824275L;
		
	@Id
	String tickerName;
	
	String source;
	
	@Id	
	@Temporal(TemporalType.DATE)
	LocalDate date;
	
	BigDecimal open;
	
	BigDecimal high;
	
	BigDecimal low;
	
	BigDecimal close;
	
	BigDecimal adjClose;
	
	BigInteger volume;

	
}
