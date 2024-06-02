package org.sberp.exchangerateportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@SpringBootApplication
public class ExchangeRatePortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRatePortalApplication.class, args);
    }

}
