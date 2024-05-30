package org.sberp.exchangerateportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Also known as alphabetic code aka USD */
    @NotNull
    @Size(min = 3, max = 3)
    private String currency;

    @NotNull
    private Double rate;

    @NotNull
    private String date;
}
