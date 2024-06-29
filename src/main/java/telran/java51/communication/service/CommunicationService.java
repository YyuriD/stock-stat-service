package telran.java51.communication.service;

import java.util.Set;

import telran.java51.communication.dto.CalcCorrelationDto;
import telran.java51.communication.dto.CalcIncomeDto;
import telran.java51.communication.dto.IncomeWithApyDto;
import telran.java51.communication.dto.IndexLinkDto;
import telran.java51.communication.dto.NewIndexDto;
import telran.java51.communication.dto.ParserRequestDto;
import telran.java51.communication.dto.ParserResponseDto;
import telran.java51.communication.dto.PeriodBeetwinDto;
import telran.java51.communication.dto.TimeHistoryDto;
import telran.java51.communication.model.TradingSession;

public interface CommunicationService {

	public Iterable<ParserResponseDto> parserForYahoo(ParserRequestDto parserRequestDto);
	
	public Iterable<IndexLinkDto> addNewIndex(NewIndexDto newIndexDto);
	
	public TimeHistoryDto getTimeHystory(String source);
	
	public Iterable<String> getAllIndexes();
	
	public Iterable<PeriodBeetwinDto> calcPeriodBeetwin(CalcIncomeDto calcDto);
	
	public IncomeWithApyDto calcIncomeWithApy(CalcIncomeDto calcDto);
	
	public String calcCorrelation(CalcCorrelationDto calcCorrelationDto);
		
	public long addTradingSessions(Set<TradingSession> tradingSessions);
	
	public Iterable<TradingSession> getDataFromRemoteService(String tickerName, String fromDate, String toDate, String source);
		
}
