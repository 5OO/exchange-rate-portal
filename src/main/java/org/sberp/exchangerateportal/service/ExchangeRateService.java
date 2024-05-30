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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    private static final String EXCHANGE_RATE_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU";
    private static final String EXCHANGE_RATE_HISTORY_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency?tp=EU&ccy=%s&dtFrom=2014-09-30&dtTo=%s";

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

    public List<ExchangeRate> getHistoricalRatesByCurrency(String currency) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(EXCHANGE_RATE_HISTORY_API_URL, currency, LocalDate.now().toString());
        String response = restTemplate.getForObject(url, String.class);

        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ForeignCurrencyExchangeRates exchangeRates = xmlMapper.readValue(response, ForeignCurrencyExchangeRates.class);
            List<ExchangeRate> rates = new ArrayList<>();

            for (ForeignCurrencyExchangeRate rate : exchangeRates.getExchangeRates()) {
                for (AmountOfCurrency amount : rate.getAmounts()) {
                    if (amount.getCurrency().equals(currency)) {
                        ExchangeRate exchangeRate = new ExchangeRate();
                        exchangeRate.setCurrency(amount.getCurrency());
                        exchangeRate.setRate(amount.getAmount());
                        exchangeRate.setDate(rate.getDate());
                        rates.add(exchangeRate);
                    }
                }
            }
            return rates;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
