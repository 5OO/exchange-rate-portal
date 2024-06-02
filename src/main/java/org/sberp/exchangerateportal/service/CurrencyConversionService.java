package org.sberp.exchangerateportal.service;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.dto.CurrencyConversionDTO;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final ExchangeRateRepository exchangeRateRepository;

    public CurrencyConversionDTO convert(CurrencyConversionDTO conversionRequest) {
        double convertedAmount;
        double fromCurrencyRate = 1;
        double toCurrencyRate = 1;

        if (conversionRequest.getFromCurrency().equals("EUR")) {
            toCurrencyRate = getRate(conversionRequest.getToCurrency());
            convertedAmount = conversionRequest.getAmount() * toCurrencyRate;
        } else if (conversionRequest.getToCurrency().equals("EUR")) {
            fromCurrencyRate = getRate(conversionRequest.getFromCurrency());
            convertedAmount = conversionRequest.getAmount() / fromCurrencyRate;
        } else {
            fromCurrencyRate = getRate(conversionRequest.getFromCurrency());
            toCurrencyRate = getRate(conversionRequest.getToCurrency());
            double amountInEuro = conversionRequest.getAmount() / fromCurrencyRate;
            convertedAmount = amountInEuro * toCurrencyRate;
        }
        return mapToDto(conversionRequest.getFromCurrency(), conversionRequest.getToCurrency(), conversionRequest.getAmount(), convertedAmount, fromCurrencyRate, toCurrencyRate);
    }

    private double getRate(String currency) {
        Optional<ExchangeRate> rateOptional = exchangeRateRepository.findLatestRateByCurrency(currency);
        if (rateOptional.isPresent()) {
            return rateOptional.get().getRate();
        } else {
            throw new IllegalArgumentException("Exchange rate not found for currency: " + currency);
        }
    }

    private CurrencyConversionDTO mapToDto(String fromCurrency, String toCurrency, double amount, double convertedAmount, double fromCurrencyRate, double toCurrencyRate) {
        return new CurrencyConversionDTO(fromCurrency, toCurrency, amount, convertedAmount, fromCurrencyRate, toCurrencyRate);
    }
}
