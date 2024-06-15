package telran.java51.stockStat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(of = "tickerName")
@Table(name = "indexes")
@ToString
public class Index {
	
	@Column(unique=true)
	String indexName;
	@Id
	String tickerName;
}
