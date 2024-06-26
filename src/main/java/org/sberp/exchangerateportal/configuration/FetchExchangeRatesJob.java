package org.sberp.exchangerateportal.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.sberp.exchangerateportal.service.ExchangeRateService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FetchExchangeRatesJob implements Job {

    private final ExchangeRateService exchangeRateService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("Scheduler: Executing FetchExchangeRatesJob {} ", jobExecutionContext.getScheduledFireTime());
        try {
            exchangeRateService.fetchAndSaveRates();
            log.info("FetchExchangeRatesJob completed successfully -> next run {} ", jobExecutionContext.getNextFireTime());

        } catch (Exception e) {
            log.error("Error executing FetchExchangeRatesJob: {}", e.getMessage());
        }
    }
}
