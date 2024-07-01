package telran.java51.communication.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java51.communication.dto.AllValueCloseBetweenDto;
import telran.java51.communication.dto.CalcCorrelationDto;
import telran.java51.communication.dto.CalcIncomeDto;
import telran.java51.communication.dto.IncomeWithApyDto;
import telran.java51.communication.dto.IndexLinkDto;
import telran.java51.communication.dto.NewIndexDto;
import telran.java51.communication.dto.ParserRequestDto;
import telran.java51.communication.dto.ParserResponseDto;
import telran.java51.communication.dto.PeriodBeetwinDto;
import telran.java51.communication.dto.TimeHistoryDto;
import telran.java51.communication.service.CommunicationServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communication")
public class CommunicationController {
	
	final CommunicationServiceImpl communicationService;
	
	@PostMapping("/parser/addIndex")
	public Iterable<IndexLinkDto> addNewIndex(@RequestBody NewIndexDto newIndexDto) {
		return communicationService.addNewIndex(newIndexDto);
	}

	@GetMapping("index/{source}")
	public TimeHistoryDto getTimeHystory(@PathVariable String source) {
		return communicationService.getTimeHystory(source);
	}

	@GetMapping("/index")
	public Iterable<String> getAllIndexes() {
		return communicationService.getAllIndexes();
	}

	@PostMapping("/index")
	public Iterable<PeriodBeetwinDto> calcPeriodBeetwin(@RequestBody CalcIncomeDto calcDto) {
		return communicationService.calcPeriodBeetwin(calcDto);
	}
	
	@DeleteMapping("/index/{source}")
	public boolean deleteIndexAndHystory(@PathVariable String source) {
		return communicationService.deleteIndexAndHystory(source);
	}

	@PostMapping("/index/apy")
	public IncomeWithApyDto calcIncomeWithApy(@RequestBody CalcIncomeDto calcDto) {
		return communicationService.calcIncomeWithApy(calcDto);
	}

	@PostMapping("/index/correlation")
	public String calcCorrelation(@RequestBody CalcCorrelationDto calcCorrelationDto) {
		return communicationService.calcCorrelation(calcCorrelationDto);
	}
	
	@PostMapping("/parser")
	public Iterable<ParserResponseDto> ParserForYahoo(@RequestBody ParserRequestDto parserRequestDto) {
		return communicationService.parserForYahoo(parserRequestDto);
	}
	
	@PostMapping("/data")
	public Iterable<AllValueCloseBetweenDto> getAllValueCloseBetween(@RequestBody CalcIncomeDto calcIncomeDto) {
		return communicationService.getAllValueCloseBetween(calcIncomeDto);
	}
	
}
