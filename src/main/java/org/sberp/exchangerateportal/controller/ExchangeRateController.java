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


    private final ExchangeRateService service;

    @GetMapping
    public List<ExchangeRate> getAllRates() {
        return service.getAllRates();
    }

    @PostMapping
    public ExchangeRate saveRate(@RequestBody ExchangeRate rate) {
        return service.saveRate(rate);
    }

}
