package org.sberp.exchangerateportal.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sberp.exchangerateportal.dto.ExchangeRateDTO;
import org.sberp.exchangerateportal.exception.ApiException;
import org.sberp.exchangerateportal.exception.NetworkException;
import org.sberp.exchangerateportal.exception.ResourceNotFoundException;
import org.sberp.exchangerateportal.model.*;
import org.sberp.exchangerateportal.repository.CurrencyNameRepository;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
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
        String response = fetchExchangeRatesWithRetry();
        processAndSaveExchangeRates(response);
    }

    private String fetchExchangeRatesWithRetry() {
        RestTemplate restTemplate = new RestTemplate();
        String response = null;
        for (int i = 0; i < 3; i++) {
            try {
                response = restTemplate.getForObject(EXCHANGE_RATE_API_URL, String.class);
                if (response != null) {
                    break;
                }
            } catch (ResourceAccessException e) {
                handleRetry(i, e);
            }
        }
        return response;
    }

    private void handleRetry(int attempt, ResourceAccessException e) {
        if (attempt == 2) {
            log.error("Failed to fetch exchange rates after {} retries", attempt + 1);
            throw new NetworkException("Failed to fetch exchange rates after multiple attempts", e);
        }
        log.warn("Retrying... ({}/{})", attempt + 1, 3);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.error("Retry sleep interrupted", ie);
            throw new NetworkException("Retry sleep interrupted", ie);
        }
    }

    private void processAndSaveExchangeRates(String response) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ForeignCurrencyExchangeRates exchangeRates = xmlMapper.readValue(response, ForeignCurrencyExchangeRates.class);
            saveExchangeRates(exchangeRates);
            log.debug("Exchange rates fetched and mapped");
        } catch (Exception e) {
            log.error("Failed to map and save exchange rates ", e);
            throw new ApiException("Failed to map and save exchange rates ", e);
        }
    }

    private void saveExchangeRates(ForeignCurrencyExchangeRates exchangeRates) {
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
    }

    public void saveRate(ExchangeRate rate) {
        exchangeRateRepository.save(rate);
    }

    public PagedModel<ExchangeRateDTO> getHistoricalRatesByCurrency(String currency, int page, int size) {
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

            Page<ExchangeRateDTO> exchangeRateDTOPage = new PageImpl<>(paginatedList, PageRequest.of(page, size), exchangeRateDTOList.size());

            return PagedModel.of(exchangeRateDTOPage.getContent(), new PagedModel.PageMetadata(
                    exchangeRateDTOPage.getSize(),
                    exchangeRateDTOPage.getNumber(),
                    exchangeRateDTOPage.getTotalElements(),
                    exchangeRateDTOPage.getTotalPages()
            ));

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("No exchange rates found for currency: {} ", currency, e);
            throw new ApiException("Failed to fetch historical rates for currency: " + currency);
        }
    }

    public List<ExchangeRateDTO> getLatestRates() {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findLatestRates();
        return exchangeRates.stream().map(this::convertToDTO).toList();
    }

    private ExchangeRateDTO convertToDTO(ExchangeRate exchangeRate) {
        Optional<CurrencyName> currencyNameOptional = currencyNameRepository.findById(exchangeRate.getCurrency());
        CurrencyName currencyName = currencyNameOptional.orElse(new CurrencyName(exchangeRate.getCurrency(), "Unknown Currency", "Unknown Location"));

        return new ExchangeRateDTO(
                exchangeRate.getCurrency(),
                exchangeRate.getRate(),
                exchangeRate.getDate(),
                currencyName.getCurrency(),
                currencyName.getEntityLocation()
        );
    }
}
