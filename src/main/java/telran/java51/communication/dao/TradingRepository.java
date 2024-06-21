package telran.java51.communication.dao;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import telran.java51.communication.model.Index;
import telran.java51.communication.model.TradingSession;
import telran.java51.communication.model.TradingSessionId;


@Repository
public interface TradingRepository extends CrudRepository<TradingSession, TradingSessionId> {
	
	@Transactional
	void deleteByTickerName(String name);
	
	TradingSession findTopBySourceOrderByDateAsc(String tickerName);
	TradingSession findTopBySourceOrderByDateDesc(String tickerName);
	boolean existsBySource(String source);

	Set<TradingSession> findByDateBetween(LocalDate from, LocalDate to);
	
	
}
