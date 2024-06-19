package telran.java51.communication.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.persistence.Entity;
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
import telran.java51.communication.exceptions.SourceNotFoundException;
import telran.java51.communication.model.Index;

@Service
@RequiredArgsConstructor
public class StockStatServiceImpl implements StockStatService {

	final IndexRepository indexRepository;
	final TradingRepository tradingRepository;

	@Override
	public Iterable<IndexLinkDto> addNewIndex(NewIndexDto newIndexDto) {
		Set<Index> newIndexes = new HashSet<>();
		newIndexDto.indexToHTML.forEach((k, v) -> newIndexes.add(new Index(k, v)));
		Set<Index> checkedIndexes = checkIndexes(newIndexes);
		Set<Index> existIndexes = new HashSet<>();
		indexRepository.findAll().forEach(i -> existIndexes.add(i));
		checkedIndexes.removeAll(existIndexes);
		existIndexes.addAll(checkedIndexes);
		indexRepository.saveAll(existIndexes);
		return checkedIndexes.stream()
				.map(i -> new IndexLinkDto(i.getIndexName(),
						"https://finance.yahoo.com/quote/" + i.getTickerName() + "/history?p=" + i.getTickerName()))
				.collect(Collectors.toSet());
	}

	@Override
	public TimeHistoryDto getTimeHystory(String source) {
		if (!tradingRepository.existsBySource(source)) {
			throw new SourceNotFoundException();
		}
		LocalDate fromDate = tradingRepository.findTopBySourceOrderByDateAsc(source).getDate();
		LocalDate toDate = tradingRepository.findTopBySourceOrderByDateDesc(source).getDate();
		return new TimeHistoryDto(source, fromDate, toDate);
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

	private Set<Index> checkIndexes(Set<Index> indexes) {
		Set<Index> checkedIndexes = new HashSet<Index>();
		indexes.forEach(i -> {
			if (isIndexExis(i)) {
				checkedIndexes.add(i);
			}
		});
		return checkedIndexes;
	}

	private boolean isIndexExis(Index index) {
		final String yahooBaseUrl = "https://query1.finance.yahoo.com/v7/finance/download/";
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(yahooBaseUrl + index.getTickerName());
		URI url = builder.build().toUri();
		RequestEntity<String> request = new RequestEntity<>(HttpMethod.GET, url);
		try {
			ResponseEntity<String> response = restTemplate.exchange(request, String.class);
			return response.getStatusCode() == HttpStatus.OK;	
		} catch (Exception e) {
			return false;
		}
	}

}
