package org.sberp.exchangerateportal.service;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final ExchangeRateRepository exchangeRateRepository;

    public double convert(String fromCurrency, String toCurrency, double amount) {
        if (fromCurrency.equals("EUR")) {
            return convertFromEuro(toCurrency, amount);
        } else if (toCurrency.equals("EUR")) {
            return convertToEuro(fromCurrency, amount);
        } else {
            double amountInEuro = convertToEuro(fromCurrency, amount);
            return convertFromEuro(toCurrency, amountInEuro);
        }
    }

    private double convertFromEuro(String toCurrency, double amount) {
        Optional<ExchangeRate> rateOptional = exchangeRateRepository.findLatestRateByCurrency(toCurrency);
        if (rateOptional.isPresent()) {
            return amount * rateOptional.get().getRate();
        } else {
            throw new IllegalArgumentException("Exchange rate not found for currency: " + toCurrency);
        }
    }

    private double convertToEuro(String fromCurrency, double amount) {
        Optional<ExchangeRate> rateOptional = exchangeRateRepository.findLatestRateByCurrency(fromCurrency);
        if (rateOptional.isPresent()) {
            return amount / rateOptional.get().getRate();
        } else {
            throw new IllegalArgumentException("Exchange rate not found for currency: " + fromCurrency);
        }
    }
}
