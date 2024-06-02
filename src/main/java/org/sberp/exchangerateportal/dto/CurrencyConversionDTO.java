package org.sberp.exchangerateportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionDTO {

    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double convertedAmount;
    private double fromCurrencyRate;
    private double toCurrencyRate;
}
