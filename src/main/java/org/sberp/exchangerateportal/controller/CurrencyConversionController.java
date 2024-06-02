package org.sberp.exchangerateportal.controller;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.dto.CurrencyConversionDTO;
import org.sberp.exchangerateportal.service.CurrencyConversionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CurrencyConversionController {

    private final CurrencyConversionService currencyConversionService;

    @PostMapping("/convert")
    public CurrencyConversionDTO convertCurrency(@RequestBody CurrencyConversionDTO conversionRequest) {
        return currencyConversionService.convert(conversionRequest);
    }

}
