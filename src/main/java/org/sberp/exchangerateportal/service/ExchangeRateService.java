package org.sberp.exchangerateportal.service;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExchangeRateService {

    private final ExchangeRateRepository repository;

    private static final String EXCHANGE_RATE_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU";

    public List<ExchangeRate> getAllRates() {
        return repository.findAll();
    }

    public ExchangeRate saveRate(ExchangeRate rate) {
        return repository.save(rate);
    }

    public void fetchAndSaveRates() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(EXCHANGE_RATE_API_URL, String.class);

    }
}
