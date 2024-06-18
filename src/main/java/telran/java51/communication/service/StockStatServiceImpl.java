package telran.java51.communication.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.IndexRepository;
import telran.java51.communication.dao.TradingRepository;
import telran.java51.communication.dto.CalcCorrelationDto;
import telran.java51.communication.dto.CalcDto;
import telran.java51.communication.dto.IncomeWithApyDto;
import telran.java51.communication.dto.IndexLinkDto;
import telran.java51.communication.dto.NewIndexDto;
import telran.java51.communication.dto.PeriodBeetwinDto;
import telran.java51.communication.dto.TimeHistoryDto;
import telran.java51.communication.model.Index;
import telran.java51.communication.model.TradingSession;

@Service
@RequiredArgsConstructor
public class StockStatServiceImpl implements StockStatService {

	final IndexRepository indexRepository;
	final TradingRepository tradingRepository;

	@Override
	public Iterable<IndexLinkDto> addNewIndex(NewIndexDto newIndexDto) {
		Set<Index> newIndexes = new HashSet<>();			
		newIndexDto.indexToHTML.forEach((k,v) -> newIndexes.add(new Index(k,v)));
		Set<Index> existIndexes = new HashSet<>();
		indexRepository.findAll().forEach(i-> existIndexes.add(i));
		newIndexes.removeAll(existIndexes);
		existIndexes.addAll(newIndexes);
		indexRepository.saveAll(existIndexes);		
		return newIndexes.stream().
				map(i -> new IndexLinkDto(i.getIndexName(),
						"https://finance.yahoo.com/quote/" + i.getTickerName() + "/history?p=" + i.getTickerName())).collect(Collectors.toSet());
	}
	

	@Override
	public TimeHistoryDto getTimeHystory(String indexName) {
		Index index = indexRepository.findByIndexName(indexName);
		LocalDate fromData = tradingRepository.findTopByTickerNameOrderByDateAsc(index.getTickerName()).getDate(); 
		LocalDate toData = tradingRepository.findTopByTickerNameOrderByDateDesc(index.getTickerName()).getDate(); 
		return new TimeHistoryDto(indexName, fromData, toData);
	}

	@Override
	public Iterable<String> getAllIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<PeriodBeetwinDto> calcPeriodBeetwin(CalcDto calcDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IncomeWithApyDto calcIncomeWithApy(CalcDto calcDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String calcCorrelation(CalcCorrelationDto calcCorrelationDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
