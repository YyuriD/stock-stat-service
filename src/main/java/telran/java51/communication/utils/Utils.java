package telran.java51.communication.utils;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import telran.java51.communication.model.TradingSession;
import telran.java51.communication.model.TradingSessionHeaders;

public final class Utils {

	public static Set<TradingSession> parseTradingSessions(String text, String tickerName, String source) {
		return parseCsv(new StringReader(text), tickerName, source);
	}

	public static Set<TradingSession> parseCsv(Reader csvReader, String tickerName, String source) {
		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(TradingSessionHeaders.class).setSkipHeaderRecord(true)
				.setIgnoreHeaderCase(true).build();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yyyy]" + "[yyyy-MM-dd]" + "[MM/dd/yyyy]" +  "[yyyy/MM/dd]");
		Set<TradingSession> tradingSessions = new HashSet<TradingSession>();
		Iterable<CSVRecord> csvRecords = null;
		try {
			csvRecords = csvFormat.parse(csvReader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (CSVRecord record : csvRecords) {
			LocalDate date = LocalDate.parse(record.get(TradingSessionHeaders.DATE.name()), formatter);
			BigDecimal open = BigDecimal.valueOf(Double.parseDouble(record.get(TradingSessionHeaders.OPEN.name())));
			BigDecimal high = BigDecimal.valueOf(Double.parseDouble(record.get(TradingSessionHeaders.HIGH.name())));
			BigDecimal low = BigDecimal.valueOf(Double.parseDouble(record.get(TradingSessionHeaders.LOW.name())));
			BigDecimal close = BigDecimal.valueOf(Double.parseDouble(record.get(TradingSessionHeaders.CLOSE.name())));
			BigDecimal adjClose = BigDecimal.valueOf(Double.parseDouble(record.get(TradingSessionHeaders.ADJ_CLOSE.name())));
			BigInteger volume = BigInteger.valueOf(Long.parseLong(record.get(TradingSessionHeaders.VOLUME.name())));
			TradingSession trading = new TradingSession(tickerName, source, date, open, high, low, close, adjClose, volume);
			tradingSessions.add(trading);
		}
		return tradingSessions;
	}
	
	public static long getTimestampFromDateString(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd/MM/yyyy]" + "[yyyy-MM-dd]" + "[MM/dd/yyyy]" + "[dd-MM-yyyy]");
		LocalDate date = LocalDate.parse(dateString, formatter);	
		return Timestamp.valueOf(date.atTime(9, 0)).getTime()/1000;// without milliseconds
	}
	
}
