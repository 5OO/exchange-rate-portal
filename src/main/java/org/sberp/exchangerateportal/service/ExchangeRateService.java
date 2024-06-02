package org.sberp.exchangerateportal.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sberp.exchangerateportal.dto.ExchangeRateDTO;
import org.sberp.exchangerateportal.exception.ApiException;
import org.sberp.exchangerateportal.exception.ResourceNotFoundException;
import org.sberp.exchangerateportal.model.*;
import org.sberp.exchangerateportal.repository.CurrencyNameRepository;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExchangeRateService {

    private static final String EXCHANGE_RATE_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU";
    private static final String EXCHANGE_RATE_HISTORY_API_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency?tp=EU&ccy=%s&dtFrom=2014-09-30&dtTo=%s";
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyNameRepository currencyNameRepository;

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
            log.debug("exchange rates fetched and mapped ");
        } catch (Exception e) {
            log.error("Failed to fetch or save exchange rates", e);
            throw new ApiException("Failed to fetch or save exchange rates");
        }
    }

    public void saveRate(ExchangeRate rate) {
        exchangeRateRepository.save(rate);
    }

    public Page<ExchangeRateDTO> getHistoricalRatesByCurrency(String currency, int page, int size) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(EXCHANGE_RATE_HISTORY_API_URL, currency, LocalDate.now());
        String response = restTemplate.getForObject(url, String.class);

        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ForeignCurrencyExchangeRates exchangeRates = xmlMapper.readValue(response, ForeignCurrencyExchangeRates.class);

            if (exchangeRates.getExchangeRates() == null) {
                log.error("No exchange rates found for currency: {}", currency);
                throw new ResourceNotFoundException("No exchange rates found for currency: " + currency);
            }

            List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<>();

            for (ForeignCurrencyExchangeRate rate : exchangeRates.getExchangeRates()) {
                for (AmountOfCurrency amount : rate.getAmounts()) {
                    if (amount.getCurrency().equals(currency)) {
                        Optional<CurrencyName> currencyNameOptional = currencyNameRepository.findById(amount.getCurrency());
                        CurrencyName currencyName = currencyNameOptional.orElse(new CurrencyName(currency, "Unknown Currency", "Unknown Location"));

                        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(
                                amount.getCurrency(),
                                amount.getAmount(),
                                rate.getDate(),
                                currencyName.getCurrency(),
                                currencyName.getEntityLocation()
                        );
                        exchangeRateDTOList.add(exchangeRateDTO);
                    }
                }
            }
            int start = Math.min(page * size, exchangeRateDTOList.size());
            int end = Math.min((page + 1) * size, exchangeRateDTOList.size());
            List<ExchangeRateDTO> paginatedList = exchangeRateDTOList.subList(start, end);

            return new PageImpl<>(paginatedList, PageRequest.of(page, size), exchangeRateDTOList.size());
        } catch (Exception e) {
            log.error("No exchange rates found for currency: {} ", currency, e);
            throw new ApiException("Failed to fetch historical rates for currency: " + currency);
        }
    }

    public List<ExchangeRate> getLatestRates() {
        return exchangeRateRepository.findLatestRates();
    }
}
