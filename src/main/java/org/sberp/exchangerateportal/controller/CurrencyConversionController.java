package org.sberp.exchangerateportal.controller;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.service.CurrencyConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyConversionController {

    private final CurrencyConversionService currencyConversionService;

    @GetMapping("/convert")
    public double convertCurrency(@RequestParam String from, @RequestParam String to, @RequestParam double amount) {
        return currencyConversionService.convert(from, to, amount);
    }
}
