package telran.java51.communication.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dao.IndexRepository;
import telran.java51.communication.dao.TradingRepository;
import telran.java51.communication.dto.CalcCorrelationDto;
import telran.java51.communication.dto.CalcIncomeDto;
import telran.java51.communication.dto.IncomeWithApyDto;
import telran.java51.communication.dto.IndexLinkDto;
import telran.java51.communication.dto.NewIndexDto;
import telran.java51.communication.dto.ParserRequestDto;
import telran.java51.communication.dto.ParserResponseDto;
import telran.java51.communication.dto.PeriodBeetwinDto;
import telran.java51.communication.dto.TimeHistoryDto;
import telran.java51.communication.exceptions.SourceNotFoundException;
import telran.java51.communication.exceptions.WrongParametersException;
import telran.java51.communication.model.Income;
import telran.java51.communication.model.Index;
import telran.java51.communication.model.PeriodType;
import telran.java51.communication.model.TradingSession;
import telran.java51.communication.utils.Utils;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class CommunicationServiceImpl implements CommunicationService {

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
		return StreamSupport.stream(indexRepository.findAll().spliterator(), true).map(i -> i.getSource())
				.collect(Collectors.toSet());
	}

	@Override 
	public Iterable<PeriodBeetwinDto> calcPeriodBeetwin(CalcIncomeDto calcIncomeDto) {
		List<String> source = indexRepository.findAllBySourceIn(calcIncomeDto.getIndexs()).stream()
				.map(i -> i.getSource()).collect(Collectors.toList());		
		if(source.size() < 1 || calcIncomeDto.getQuantity() <= 0) {
			throw new WrongParametersException();
		}
			
		List<List<TradingSession>> allTradings = new ArrayList<>();	
		List<List<Income>> allIncomes = new ArrayList<>();
		for (int i = 0; i < source.size(); i++) {
			List<TradingSession> tradings = tradingRepository.findByDateBetweenAndSource(calcIncomeDto.getFrom(),
					calcIncomeDto.getTo(), source.get(i));
			allTradings.add(tradings);
			allIncomes.add(new ArrayList<Income>());
		}

		int periodInDays = calcDaysQuantity(calcIncomeDto.getQuantity(), calcIncomeDto.getType());
		int totalDays = (int) ChronoUnit.DAYS.between(calcIncomeDto.getFrom(), calcIncomeDto.getTo());
		if (totalDays < periodInDays) {
			throw new WrongParametersException();
		}
		int counter = totalDays - periodInDays + 1;
		LocalDate fromDate = calcIncomeDto.getFrom();
		LocalDate toDate = calcIncomeDto.getFrom().plusDays(periodInDays);
		
		for (int i = 0; i < counter; i++) {			
			for (int j = 0; j < allTradings.size(); j++) {
				Set<TradingSession> purchaseSessions = new HashSet<>();
				Set<TradingSession> saleSessions = new HashSet<>();
				purchaseSessions.add( findTradingByDate(allTradings.get(j), fromDate));
				saleSessions.add(findTradingByDate(allTradings.get(j), toDate));	
				allIncomes.get(j).add(new Income(purchaseSessions, saleSessions));				
			}
			fromDate = fromDate.plusDays(1);
			toDate = toDate.plusDays(1);
		}
		allIncomes.stream().forEach(i-> Collections.sort(i));
		
		return allIncomes.stream()
				.map(i-> new PeriodBeetwinDto(calcIncomeDto.getFrom(),
						 calcIncomeDto.getTo(), i.get(0).getSources().get(0),
						 calcIncomeDto.getQuantity() +  " " + calcIncomeDto.getType(),
						 i.get(i.size()-1).getIncome(),
						 calcMean(i), calcMedian(i),
						 i.get(0).getIncome(),calcStdDeviation(i))).collect(Collectors.toList());
	}

	private BigDecimal calcMean(List<Income> incomes) {
		BigDecimal res = incomes.stream().map(i-> i.getIncome()).reduce(BigDecimal.ZERO, (n1,n2)-> n1.add(n2));
		res = res.divide(new BigDecimal(incomes.size()), 2,RoundingMode.HALF_UP);
		return res;
	}
	
	private BigDecimal calcMedian(List<Income> incomes) {
		Collections.sort(incomes);
		int size = incomes.size();
		if(incomes.size()%2 == 0) {
			return (incomes.get(size/2 - 1)
					.getIncome().add(incomes.get(incomes.size()/2)
							.getIncome())).divide(new BigDecimal(2));
		}else {
			return incomes.get(size/2).getIncome();
		}
	}
	
	private BigDecimal calcStdDeviation(List<Income> incomes) {
		double mean = calcMean(incomes).doubleValue(); 
        double sumOfSquaredDifferences = 0.0;
        List<Double> values = incomes.stream().map(i-> i.getIncome().doubleValue()).collect(Collectors.toList());
        for (Double value : values) {
            sumOfSquaredDifferences += Math.pow(value - mean, 2);  
        }
        double meanOfSquaredDifferences = sumOfSquaredDifferences / values.size(); 
        return BigDecimal.valueOf(Math.sqrt(meanOfSquaredDifferences)); 
	}
	
	
	@Override
	public IncomeWithApyDto calcIncomeWithApy(CalcIncomeDto calcIncomeDto) {
		List<String> source = indexRepository.findAllBySourceIn(calcIncomeDto.getIndexs()).stream()
				.map(i -> i.getSource()).collect(Collectors.toList());		
		if(source.size() < 1 || calcIncomeDto.getQuantity() <= 0) {
			throw new WrongParametersException();
		}
			
		List<List<TradingSession>> allTradings = new ArrayList<>();		
		for (int i = 0; i < source.size(); i++) {
			List<TradingSession> tradings = tradingRepository.findByDateBetweenAndSource(calcIncomeDto.getFrom(),
					calcIncomeDto.getTo(), source.get(i));
			allTradings.add(tradings);
		}

		int periodInDays = calcDaysQuantity(calcIncomeDto.getQuantity(), calcIncomeDto.getType());
		int totalDays = (int) ChronoUnit.DAYS.between(calcIncomeDto.getFrom(), calcIncomeDto.getTo());
		if (totalDays < periodInDays) {
			throw new WrongParametersException();
		}
		int counter = totalDays - periodInDays + 1;
		LocalDate fromDate = calcIncomeDto.getFrom();
		LocalDate toDate = calcIncomeDto.getFrom().plusDays(periodInDays);
		List<Income> incomes = new ArrayList<>();

		for (int i = 0; i < counter; i++) {
			Set<TradingSession> purchaseSessions = new HashSet<>();
			Set<TradingSession> saleSessions = new HashSet<>();
			for (int j = 0; j < allTradings.size(); j++) {
				purchaseSessions.add(findTradingByDate(allTradings.get(j), fromDate));
				saleSessions.add(findTradingByDate(allTradings.get(j), toDate));
			}
			incomes.add(new Income(purchaseSessions, saleSessions));
			fromDate = fromDate.plusDays(1);
			toDate = toDate.plusDays(1);
		}

		Collections.sort(incomes);
		Income minIncome = incomes.get(0);
		Income maxIncome = incomes.get(incomes.size() - 1);

		return new IncomeWithApyDto(calcIncomeDto.getFrom(), calcIncomeDto.getTo(), source, calcIncomeDto.getType(),
				minIncome, maxIncome);
	}

	private TradingSession findTradingByDate(List<TradingSession> list, LocalDate date) {
		Comparator<TradingSession> comparator = new Comparator<TradingSession>() {
			@Override
			public int compare(TradingSession o1, TradingSession o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		};
		TradingSession pattern = new TradingSession(null, null, date, null, null, null, null, null, null);
		int index = Collections.binarySearch(list, pattern, comparator);
		if (index < 0) {
			index = -index - 1;
			if(index >= list.size()) {
				index = list.size() - 1;
			}
		}
		return list.get(index);
	}

	@Override
	public String calcCorrelation(CalcCorrelationDto calcCorrelationDto) {
		// TODO Auto-generated method stub
		return null;
	}

	private Integer calcDaysQuantity(Integer quantity, String type) {
		try {
			switch (PeriodType.valueOf(type.toUpperCase())) {
			case DAYS:
				return quantity;
			case WEEKS:
				return quantity * 7;
			case MONTHS:
				return quantity * 30;
			case YEARS:
				return quantity * 365;
			case DECADES:
				return quantity * 3652;
			case CENTURIES:
				return quantity * 36520;
			default:
				throw new WrongParametersException();
			}
		} catch (IllegalArgumentException e) {
			throw new WrongParametersException(e.getMessage());
		}
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

	@Scheduled(cron = "0 00 00 * * *")
	private void updateDataFromRemoteService() {
		Set<Index> indexes = StreamSupport.stream(indexRepository.findAll().spliterator(), true)
				.collect(Collectors.toSet());
		Set<TradingSession> tradingSessions = new HashSet<>();
		String toDate = LocalDate.now().toString(); 
		String fromDate = LocalDate.now().toString(); 
		for (Index index : indexes) {
			Set<TradingSession> set = getDataFromRemoteService(index.getTickerName(), fromDate, toDate, index.getSource());
			if(set == null) {
				continue;
			}
			tradingSessions.addAll(set);
		}
		for (TradingSession tradingSession : tradingSessions) {
			System.out.println("Added trading session of \"" + tradingSession.getSource() + "\""
			+ " for " + tradingSession.getDate());
		}
		addTradingSessions(tradingSessions);
	}
	
	@Override
	public Iterable<ParserResponseDto> ParserForYahoo(ParserRequestDto parserRequestDto) {
		
		return null;
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
			
			ResponseEntity<String> response;		
			try {
				response = restTemplate.exchange(request, String.class);
			} catch (RestClientException e) {
				System.out.println(e.getMessage() + "; from date " 
						+ fromDate.toString() + "; to date "  + toDate
						+ "; source = " + source + ";");
				return null;
			}		
			MediaType contentType = MediaType.parseMediaType("text/csv; charset=UTF-8");
			if (!response.getHeaders().getContentType().equalsTypeAndSubtype(contentType)) {
				throw new UnsupportedMediaTypeStatusException(contentType.toString());
			}
	
				
		return Utils.parseTradingSessions(response.getBody(), tickerName, source);
	}

	
}
