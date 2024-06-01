package org.sberp.exchangerateportal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.sberp.exchangerateportal.repository.ExchangeRateRepository;
import org.sberp.exchangerateportal.service.ExchangeRateService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {


    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;


    @Test
    void testGetAllRates() {
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency("USD");
        rate.setRate(1.1);
        rate.setDate("2024-05-24");

        when(exchangeRateRepository.findAll()).thenReturn(Collections.singletonList(rate));

//        List<ExchangeRate> rates = exchangeRateService.getAllRates();
//        assertEquals(1, rates.size());
//        assertEquals("USD", rates.get(0).getCurrency());
    }

    @Test
    void testGetRatesByCurrency() {
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency("USD");
        rate.setRate(1.1);
        rate.setDate("2024-05-24");

//        when(exchangeRateRepository.findByCurrency("USD")).thenReturn(Collections.singletonList(rate));

//        List<ExchangeRate> rates = exchangeRateService.getRatesByCurrency("USD");
//        assertEquals(1, rates.size());
//        assertEquals("USD", rates.get(0).getCurrency());
    }
}
