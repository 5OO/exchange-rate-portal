package org.sberp.exchangerateportal.controller;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.service.ExchangeRateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exchange-rates")
public class ExchangeRateController {


    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public List<ExchangeRate> getAllRates() {
        return exchangeRateService.getAllRates();
    }

    @PostMapping
    public ExchangeRate saveRate(@RequestBody ExchangeRate rate) {
        return exchangeRateService.saveRate(rate);
    }

}
