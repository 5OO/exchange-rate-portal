package org.sberp.exchangerateportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Table(name = "exchange_rate")
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
