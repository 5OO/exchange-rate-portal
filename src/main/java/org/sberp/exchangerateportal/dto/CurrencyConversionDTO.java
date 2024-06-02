package org.sberp.exchangerateportal.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyConversionDTO {

    @NotBlank(message = "From currency must not be blank")
    private String fromCurrency;

    @NotBlank(message = "To currency must not be blank")
    private String toCurrency;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private double amount;

    private double convertedAmount;
    private double fromCurrencyRate;
    private double toCurrencyRate;
}
