package telran.java51.stockStat.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.stockStat.dao.IndexRepository;
import telran.java51.stockStat.dto.CalcCorrelationDto;
import telran.java51.stockStat.dto.CalcDto;
import telran.java51.stockStat.dto.IncomeWithApyDto;
import telran.java51.stockStat.dto.IndexLinkDto;
import telran.java51.stockStat.dto.NewIndexDto;
import telran.java51.stockStat.dto.PeriodBeetwinDto;
import telran.java51.stockStat.dto.TimeHistoryDto;
import telran.java51.stockStat.model.Index;

@Service
@RequiredArgsConstructor
public class StockStatServiceImpl implements StockStatService {

	final IndexRepository indexRepository;

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
		// TODO Auto-generated method stub
		return null;
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
