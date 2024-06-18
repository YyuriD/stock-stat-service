package telran.java51.communication.service;

import telran.java51.communication.dto.CalcCorrelationDto;
import telran.java51.communication.dto.CalcDto;
import telran.java51.communication.dto.IncomeWithApyDto;
import telran.java51.communication.dto.IndexLinkDto;
import telran.java51.communication.dto.NewIndexDto;
import telran.java51.communication.dto.PeriodBeetwinDto;
import telran.java51.communication.dto.TimeHistoryDto;

public interface StockStatService {

	public Iterable<IndexLinkDto> addNewIndex(NewIndexDto newIndexDto);
	
	public TimeHistoryDto getTimeHystory(String source);
	
	public Iterable<String> getAllIndexes();
	
	public Iterable<PeriodBeetwinDto> calcPeriodBeetwin(CalcDto calcDto);
	
	public IncomeWithApyDto calcIncomeWithApy(CalcDto calcDto);
	
	public String calcCorrelation(CalcCorrelationDto calcCorrelationDto);
		
}
