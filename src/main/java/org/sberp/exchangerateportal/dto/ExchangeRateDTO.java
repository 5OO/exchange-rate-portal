package org.sberp.exchangerateportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRateDTO {
    private String currency;
    private Double rate;
    private String date;
    private String currencyName;
    private String entityLocation;
}