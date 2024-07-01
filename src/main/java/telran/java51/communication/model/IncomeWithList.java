package telran.java51.communication.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeWithList extends Income {

	public IncomeWithList(Set<TradingSession> purchaseSessions, Set<TradingSession> saleSessions, List<BigDecimal> closes) {
		super(purchaseSessions, saleSessions);
		this.closes = closes;
	}
	
	private List<BigDecimal> closes;

}
