package telran.java51.communication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dto.CalcCorrelationDto;
import telran.java51.communication.dto.CalcDto;
import telran.java51.communication.dto.IncomeWithApyDto;
import telran.java51.communication.dto.IndexLinkDto;
import telran.java51.communication.dto.NewIndexDto;
import telran.java51.communication.dto.PeriodBeetwinDto;
import telran.java51.communication.dto.TimeHistoryDto;
import telran.java51.communication.service.StockStatServiceImpl;

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