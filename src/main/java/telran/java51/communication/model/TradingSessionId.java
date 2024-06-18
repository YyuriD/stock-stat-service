package telran.java51.communication.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "tickerName", "date" })
public class TradingSessionId implements Serializable {

	private static final long serialVersionUID = 6449136850011727098L;
	private String tickerName;
	private LocalDate date;
}
