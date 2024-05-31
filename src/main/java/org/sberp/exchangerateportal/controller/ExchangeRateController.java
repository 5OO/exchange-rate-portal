package org.sberp.exchangerateportal.controller;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.service.ExchangeRateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {


    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public List<ExchangeRate> getLatestRates() {
        return exchangeRateService.getLatestRates();
    }

    @PostMapping
    public ExchangeRate saveRate(@RequestBody ExchangeRate rate) {
        return exchangeRateService.saveRate(rate);
    }

    @GetMapping("/fetch")
    public void fetchAndSaveRates() {
        exchangeRateService.fetchAndSaveRates();
    }

    @GetMapping("/{currency}")
    public List<ExchangeRate> getRatesByCurrency(@PathVariable String currency) {
        return exchangeRateService.getRatesByCurrency(currency);
    }

    @GetMapping("/history/{currency}")
    public List<ExchangeRate> getHistoricalRatesByCurrency(@PathVariable String currency) {
        return exchangeRateService.getHistoricalRatesByCurrency(currency);
    }
}
