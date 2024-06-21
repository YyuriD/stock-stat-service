package telran.java51.communication.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Income {

	public Income(Set<TradingSession> purchaseSessions, Set<TradingSession> saleSessions) {
		
		this.dateOfPurchase = purchaseSessions.iterator().next().getDate();
		this.dateOfSale = saleSessions.iterator().next().getDate();
		this.purchaseAmount = purchaseSessions.stream().map(s-> s.getOpen())
				.reduce(BigDecimal.ZERO, (n1,n2)-> n1.add(n2));
		this.saleAmount = saleSessions.stream().map(s-> s.getOpen())
				.reduce(BigDecimal.ZERO, (n1,n2)-> n1.add(n2));
		this.income = saleAmount.subtract(purchaseAmount);
		this.apy = 	calculateApy();	
	}

	LocalDate dateOfPurchase;
	BigDecimal purchaseAmount;
	LocalDate dateOfSale;
	BigDecimal saleAmount;
	BigDecimal income;
	Double apy;
	
	private Double calculateApy() {
		long periodInDays = ChronoUnit.DAYS.between(getDateOfSale(), getDateOfPurchase());
		double periodInYears = (double)periodInDays / 365;
		double incomeInPercent = saleAmount.subtract(purchaseAmount).divide(purchaseAmount).doubleValue();
		return Math.pow(1 + incomeInPercent, periodInYears);
	}
}

	
