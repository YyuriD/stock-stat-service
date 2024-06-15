package telran.java51.stockStat.service;

import telran.java51.stockStat.dto.CalcCorrelationDto;
import telran.java51.stockStat.dto.CalcDto;
import telran.java51.stockStat.dto.IncomeWithApyDto;
import telran.java51.stockStat.dto.IndexLinkDto;
import telran.java51.stockStat.dto.NewIndexDto;
import telran.java51.stockStat.dto.PeriodBeetwinDto;
import telran.java51.stockStat.dto.TimeHistoryDto;

public interface StockStatService {

	public Iterable<IndexLinkDto> addNewIndex(NewIndexDto newIndexDto);
	
	public TimeHistoryDto getTimeHystory(String indexName);
	
	public Iterable<String> getAllIndexes();
	
	public Iterable<PeriodBeetwinDto> calcPeriodBeetwin(CalcDto calcDto);
	
	public IncomeWithApyDto calcIncomeWithApy(CalcDto calcDto);
	
	public String calcCorrelation(CalcCorrelationDto calcCorrelationDto);
		
}
