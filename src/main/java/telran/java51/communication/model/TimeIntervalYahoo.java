package telran.java51.communication.model;

public enum TimeIntervalYahoo {
	DAY("1d"),
	WEEK("1wk"),
	MONTH("1mo");
	
	public String type;
	TimeIntervalYahoo(String type) {
		this.type = type;
	}
}
