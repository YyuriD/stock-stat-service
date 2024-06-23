package telran.java51.communication.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import telran.java51.communication.exceptions.SourceNotFoundException;

@Getter
@Setter
public class Income implements Comparable<Income> {

	public Income(Set<TradingSession> purchaseSessions, Set<TradingSession> saleSessions) {
		if (purchaseSessions == null || saleSessions == null) {
			throw new SourceNotFoundException();// TODO
		}
		this.dateOfPurchase = purchaseSessions.iterator().next().getDate();
		this.dateOfSale = saleSessions.iterator().next().getDate();
		this.purchaseAmount = purchaseSessions.stream().map(s -> s.getOpen()).reduce(BigDecimal.ZERO,
				(n1, n2) -> n1.add(n2));
		this.saleAmount = saleSessions.stream().map(s -> s.getClose()).reduce(BigDecimal.ZERO, (n1, n2) -> n1.add(n2));
		this.income = saleAmount.subtract(purchaseAmount);
		this.apy = calculateApy();
	}

	LocalDate dateOfPurchase;
	BigDecimal purchaseAmount;
	LocalDate dateOfSale;
	BigDecimal saleAmount;
	BigDecimal income;
	Double apy;

	private Double calculateApy() {
//		long periodInDays = ChronoUnit.DAYS.between(getDateOfPurchase(), getDateOfSale());
		return income.doubleValue() / purchaseAmount.doubleValue() * 100;
	}

	@Override
	public int compareTo(Income o) {
		return income.compareTo(o.income);
	}
}
