package org.sberp.exchangerateportal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sberp.exchangerateportal.dto.CurrencyConversionDTO;
import org.sberp.exchangerateportal.exception.ApiException;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.sberp.exchangerateportal.service.CurrencyConversionService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConversionServiceTest {


    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;

    private ExchangeRate usdRate;
    private ExchangeRate gbpRate;

    @BeforeEach
    void setUp() {
        usdRate = new ExchangeRate();
        usdRate.setCurrency("USD");
        usdRate.setRate(1.1);

        gbpRate = new ExchangeRate();
        gbpRate.setCurrency("GBP");
        gbpRate.setRate(0.9);
    }

    @Test
    void testConvert_WhenFromCurrencyIsEUR() {
        when(exchangeRateRepository.findLatestRateByCurrency("USD")).thenReturn(Optional.of(usdRate));

        CurrencyConversionDTO request = new CurrencyConversionDTO("EUR", "USD", 10, 0, 0, 0);
        CurrencyConversionDTO response = currencyConversionService.convert(request);

        assertNotNull(response);
        assertEquals(10, response.getAmount());
        assertEquals(11, response.getConvertedAmount());
        assertEquals("EUR", response.getFromCurrency());
        assertEquals("USD", response.getToCurrency());
        assertEquals(1, response.getFromCurrencyRate());
        assertEquals(1.1, response.getToCurrencyRate());
    }

    @Test
    void testConvert_WhenToCurrencyIsEUR() {
        when(exchangeRateRepository.findLatestRateByCurrency("USD")).thenReturn(Optional.of(usdRate));

        CurrencyConversionDTO request = new CurrencyConversionDTO("USD", "EUR", 100, 0, 0, 0);
        CurrencyConversionDTO response = currencyConversionService.convert(request);

        assertNotNull(response);
        assertEquals(100, response.getAmount());
        assertEquals(90.91, response.getConvertedAmount(), 0.01);  // Small tolerance for floating point arithmetic
        assertEquals("USD", response.getFromCurrency());
        assertEquals("EUR", response.getToCurrency());
        assertEquals(1.1, response.getFromCurrencyRate());
        assertEquals(1, response.getToCurrencyRate());
    }

    @Test
    void testConvert_WhenNeitherCurrencyIsEUR() {
        when(exchangeRateRepository.findLatestRateByCurrency("USD")).thenReturn(Optional.of(usdRate));
        when(exchangeRateRepository.findLatestRateByCurrency("GBP")).thenReturn(Optional.of(gbpRate));

        CurrencyConversionDTO request = new CurrencyConversionDTO("USD", "GBP", 100, 0, 0, 0);
        CurrencyConversionDTO response = currencyConversionService.convert(request);

        assertNotNull(response);
        assertEquals(100, response.getAmount());
        assertEquals(81.82, response.getConvertedAmount(), 0.01);  // Small tolerance for floating point arithmetic
        assertEquals("USD", response.getFromCurrency());
        assertEquals("GBP", response.getToCurrency());
        assertEquals(1.1, response.getFromCurrencyRate());
        assertEquals(0.9, response.getToCurrencyRate());
    }

    @Test
    void testConvert_WhenExchangeRateNotFound_ShouldThrowApiException() {
        when(exchangeRateRepository.findLatestRateByCurrency(anyString())).thenReturn(Optional.empty());

        CurrencyConversionDTO request = new CurrencyConversionDTO("USD", "GBP", 100, 0, 0, 0);

        ApiException exception = assertThrows(ApiException.class, () -> currencyConversionService.convert(request));

        assertEquals("Exchange rate not found for currency: USD", exception.getMessage());
    }
}
