package telran.java51.communication.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.springframework.web.util.UriComponentsBuilder;

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
import telran.java51.communication.model.TradingSession;
import telran.java51.communication.utils.Utils;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class StockStatServiceImpl implements StockStatService {

	final String yahooBaseUrl = "https://query1.finance.yahoo.com/v7/finance/download/";
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
		updateDataFromRemoteService();
		return checkedIndexes.stream()
				.map(i -> new IndexLinkDto(i.getSource(),
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
		return StreamSupport.stream(indexRepository.findAll().spliterator(), true)
				.map(i -> i.getSource())
				.collect(Collectors.toSet());
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
			if (isIndexExist(i)) {
				checkedIndexes.add(i);
			}
		});
		return checkedIndexes;
	}

	private boolean isIndexExist(Index index) {
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

	@Scheduled(cron = "0 0 19 * * *") 
	private void updateDataFromRemoteService() {
		Set<Index> indexes = StreamSupport.stream(indexRepository.findAll().spliterator(), true)
				.collect(Collectors.toSet());
		Set<TradingSession> tradingSessions = new HashSet<>();
		String date = LocalDate.now().toString();
		for (Index index : indexes) {
			tradingSessions.addAll(getDataFromRemoteService(index.getTickerName(), date, date, index.getSource()));
		}		
		addTradingSessions(tradingSessions);
	}

	@Override
	public long addTradingSessions(Set<TradingSession> tradingSessions) {
		long prevQuantity = tradingRepository.count();
		tradingRepository.saveAll(tradingSessions);
		return tradingRepository.count() - prevQuantity;
	}

	@Override
	public Set<TradingSession> getDataFromRemoteService(String tickerName, String fromDate, String toDate,
			String source) {
		String fromTimestamp = Long.toString(Utils.getTimestampFromDateString(fromDate));
		String toTimestamp = Long.toString(Utils.getTimestampFromDateString(toDate));
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(yahooBaseUrl + tickerName)
				.queryParam("interval", "1d").queryParam("period1", fromTimestamp).queryParam("period2", toTimestamp);
		URI url = builder.build().toUri();
		RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, url);
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		MediaType contentType = MediaType.parseMediaType("text/csv; charset=UTF-8");
		if (!response.getHeaders().getContentType().equalsTypeAndSubtype(contentType)) {
			throw new UnsupportedMediaTypeStatusException(contentType.toString());
		}
		return Utils.parseTradingSessions(response.getBody(), tickerName, source);
	}

}
