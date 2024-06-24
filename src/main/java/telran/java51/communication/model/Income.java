package telran.java51.communication.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import telran.java51.communication.exceptions.SourceNotFoundException;

@Getter
@Setter
public class Income implements Comparable<Income> {

	public Income(Set<TradingSession> purchaseSessions, Set<TradingSession> saleSessions) {
		if (purchaseSessions == null || saleSessions == null) {
			throw new SourceNotFoundException();
		}
		this.dateOfPurchase = purchaseSessions.iterator().next().getDate();
		this.dateOfSale = saleSessions.iterator().next().getDate();
		this.purchaseAmount = purchaseSessions.stream().map(s -> s.getOpen()).reduce(BigDecimal.ZERO,
				(n1, n2) -> n1.add(n2));
		this.saleAmount = saleSessions.stream().map(s -> s.getClose()).reduce(BigDecimal.ZERO, (n1, n2) -> n1.add(n2));
		this.income = saleAmount.subtract(purchaseAmount);
		this.apy = calculateApy();
		sources = new ArrayList<String>();
		purchaseSessions.stream().forEach(s-> this.sources.add(s.getSource()));
	}

	private LocalDate dateOfPurchase;
	private BigDecimal purchaseAmount;
	private LocalDate dateOfSale;
	private BigDecimal saleAmount;
	private BigDecimal income;
	private Double apy;
	private List<String> sources;

	private Double calculateApy() {
		return income.doubleValue() / purchaseAmount.doubleValue() * 100;
	}

	@Override
	public int compareTo(Income o) {
		return income.compareTo(o.income);
	}
}
