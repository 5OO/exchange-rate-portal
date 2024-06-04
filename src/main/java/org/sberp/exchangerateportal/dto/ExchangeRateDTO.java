package org.sberp.exchangerateportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRateDTO {

    @NotBlank(message = "Currency code cannot be blank")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currency;

    @NotNull(message = "Rate cannot be null")
    private Double rate;

    @NotBlank(message = "Date cannot be blank")
    private String date;

    @NotBlank(message = "Currency name cannot be blank")
    private String currencyName;

    @NotBlank
    private String entityLocation;
}