package telran.java51.stockStat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java51.stockStat.dto.CalcCorrelationDto;
import telran.java51.stockStat.dto.CalcDto;
import telran.java51.stockStat.dto.IncomeWithApyDto;
import telran.java51.stockStat.dto.IndexLinkDto;
import telran.java51.stockStat.dto.NewIndexDto;
import telran.java51.stockStat.dto.PeriodBeetwinDto;
import telran.java51.stockStat.dto.TimeHistoryDto;
import telran.java51.stockStat.service.StockStatServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communication")
public class StockStatController {
	
	final StockStatServiceImpl stockStatService;
	
	@PostMapping("/parser/addIndex")
	public Iterable<IndexLinkDto> addNewIndex(@RequestBody NewIndexDto newIndexDto) {
		return stockStatService.addNewIndex(newIndexDto);
	}

	@GetMapping("index/{indexName}")
	public TimeHistoryDto getTimeHystory(@PathVariable String indexName) {
		return stockStatService.getTimeHystory(indexName);
	}

	@GetMapping("/index")
	public Iterable<String> getAllIndexes() {
		return stockStatService.getAllIndexes();
	}

	@PostMapping("/index")
	public Iterable<PeriodBeetwinDto> calcPeriodBeetwin(@RequestBody CalcDto calcDto) {
		return stockStatService.calcPeriodBeetwin(calcDto);
	}

	@PostMapping("/index/apy")
	public IncomeWithApyDto calcIncomeWithApy(@RequestBody CalcDto calcDto) {
		return stockStatService.calcIncomeWithApy(calcDto);
	}

	@PostMapping("/index/correlation")
	public String calcCorrelation(@RequestBody CalcCorrelationDto calcCorrelationDto) {
		return stockStatService.calcCorrelation(calcCorrelationDto);
	}
	

}
