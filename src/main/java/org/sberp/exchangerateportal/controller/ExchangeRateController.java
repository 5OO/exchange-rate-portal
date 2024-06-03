package org.sberp.exchangerateportal.controller;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.dto.ExchangeRateDTO;
import org.sberp.exchangerateportal.service.ExchangeRateService;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173","http://localhost:4200/"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exchange-rates")
@Validated
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public List<ExchangeRateDTO> getLatestRates() {
        return exchangeRateService.getLatestRates();
    }

    @GetMapping("/history/{currency}")
    public PagedModel<ExchangeRateDTO> getHistoricalRatesByCurrency(@PathVariable String currency,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "250") int size) {
        return exchangeRateService.getHistoricalRatesByCurrency(currency, page, size);
    }
}
