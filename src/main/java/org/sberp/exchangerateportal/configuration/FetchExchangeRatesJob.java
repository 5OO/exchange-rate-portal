package org.sberp.exchangerateportal.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sberp.exchangerateportal.service.ExchangeRateService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FetchExchangeRatesJob implements Job {

    private final ExchangeRateService exchangeRateService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("scheduler: Executing FetchExchangeRatesJob");
        exchangeRateService.fetchAndSaveRates();
        log.info("FetchExchangeRatesJob completed successfully");
    }
}
