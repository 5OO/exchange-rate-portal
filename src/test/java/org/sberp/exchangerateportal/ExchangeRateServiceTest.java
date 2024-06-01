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

    }

    @Test
    void testGetRatesByCurrency() {
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency("USD");
        rate.setRate(1.1);
        rate.setDate("2024-05-24");

    }
}
