package org.sberp.exchangerateportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "currency")
@Data
public class CurrencyName {

    @Id
    @Column(name = "alphabetic_code", length = 3, nullable = false)
    @NotNull
    @Size(min = 3, max = 3)
    private String alphabeticCode;

    @Column(name = "currency_name", nullable = false)
    @NotEmpty
    private String currency;

    @Column(name = "entity_location", nullable = false)
    @NotEmpty
    private String entityLocation;
}
