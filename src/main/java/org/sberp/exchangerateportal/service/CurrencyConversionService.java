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
        if (conversionRequest.getFromCurrency().equals("EUR")) {
            convertedAmount=  convertFromEuro(conversionRequest.getToCurrency(),conversionRequest.getAmount());
        } else if (conversionRequest.getToCurrency().equals("EUR")) {
            convertedAmount= convertToEuro(conversionRequest.getFromCurrency(), conversionRequest.getAmount());
        } else {
            double amountInEuro = convertToEuro(conversionRequest.getFromCurrency(), conversionRequest.getAmount());
            convertedAmount= convertFromEuro(conversionRequest.getToCurrency(), amountInEuro);
        }
        return mapToDto(conversionRequest.getFromCurrency(), conversionRequest.getToCurrency(), conversionRequest.getAmount(), convertedAmount);
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

    private CurrencyConversionDTO mapToDto(String fromCurrency, String toCurrency, double amount, double convertedAmount) {
        CurrencyConversionDTO dto = new CurrencyConversionDTO();
        dto.setFromCurrency(fromCurrency);
        dto.setToCurrency(toCurrency);
        dto.setAmount(amount);
        dto.setConvertedAmount(convertedAmount);
        return dto;
    }
}
