package org.sberp.exchangerateportal.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.model.AmountOfCurrency;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.model.ForeignCurrencyExchangeRate;
import org.sberp.exchangerateportal.model.ForeignCurrencyExchangeRates;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    private static final String EXCHANGE_RATE_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU";

    public List<ExchangeRate> getAllRates() {
        return exchangeRateRepository.findAll();
    }

    public ExchangeRate saveRate(ExchangeRate rate) {
        return exchangeRateRepository.save(rate);
    }

    public void fetchAndSaveRates() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(EXCHANGE_RATE_API_URL, String.class);

        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ForeignCurrencyExchangeRates exchangeRates = xmlMapper.readValue(response, ForeignCurrencyExchangeRates.class);

            for (ForeignCurrencyExchangeRate exchangeRate : exchangeRates.getExchangeRates()) {
                for (AmountOfCurrency amount : exchangeRate.getAmounts()) {
                    if (!amount.getCurrency().equals("EUR")) {
                        ExchangeRate rate = new ExchangeRate();
                        rate.setCurrency(amount.getCurrency());
                        rate.setRate(amount.getAmount());
                        rate.setDate(exchangeRate.getDate());
                        saveRate(rate);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO logging  and error handling implementation
        }
    }

    public List<ExchangeRate> getRatesByCurrency(String currency) {
        return exchangeRateRepository.findByCurrency(currency);
    }
}
