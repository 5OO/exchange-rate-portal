package org.sberp.exchangerateportal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sberp.exchangerateportal.dto.ExchangeRateDTO;
import org.sberp.exchangerateportal.model.CurrencyName;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.repository.CurrencyNameRepository;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.sberp.exchangerateportal.service.ExchangeRateService;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {


    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyNameRepository currencyNameRepository;

    @Test
    void testGetLatestRates() {
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency("USD");
        rate.setRate(1.1);
        rate.setDate(String.valueOf(LocalDate.of(2024, 5, 24)));

        CurrencyName currencyName = new CurrencyName("USD", "US Dollar", "United States");

        when(exchangeRateRepository.findLatestRates()).thenReturn(Collections.singletonList(rate));
        when(currencyNameRepository.findById("USD")).thenReturn(Optional.of(currencyName));

        List<ExchangeRateDTO> latestRates = exchangeRateService.getLatestRates();
        assertEquals(1, latestRates.size());
        assertEquals("USD", latestRates.get(0).getCurrency());
        assertEquals(1.1, latestRates.get(0).getRate());
        assertEquals("US Dollar", latestRates.get(0).getCurrencyName());
        assertEquals("United States", latestRates.get(0).getEntityLocation());
    }

    @Test
    void testGetHistoricalRatesByCurrency() {
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency("USD");
        rate.setRate(1.0852);
        rate.setDate(String.valueOf(LocalDate.of(2024, 5, 24)));

        CurrencyName currencyName = new CurrencyName("USD", "US Dollar", "United States");

        when(currencyNameRepository.findById("USD")).thenReturn(Optional.of(currencyName));

        PagedModel<ExchangeRateDTO> historicalRatesModel = exchangeRateService.getHistoricalRatesByCurrency("USD", 0, 10);

        List<ExchangeRateDTO> historicalRates = StreamSupport.stream(historicalRatesModel.spliterator(), false)
                .toList();

        assertEquals(10, historicalRates.size());
        assertEquals("USD", historicalRates.get(0).getCurrency());
        assertEquals(1.0852, historicalRates.get(0).getRate());
        assertEquals("US Dollar", historicalRates.get(0).getCurrencyName());
        assertEquals("United States", historicalRates.get(0).getEntityLocation());
    }
}
