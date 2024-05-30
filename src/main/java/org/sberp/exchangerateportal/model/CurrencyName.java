package org.sberp.exchangerateportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "currency")
@Data
public class CurrencyName {

    /**
     * The ISO 4217 alphabetic code of the currency. (Elsewhere referred as "currency")
     * Must not be null and must be exactly 3 characters long.
     */
    @Id
    @Column(name = "alphabetic_code", length = 3, nullable = false)
    @NotNull
    @Size(min = 3, max = 3)
    private String alphabeticCode;

    /**
     * The name of the currency.
     * Must not be empty.
     */
    @Column(name = "currency_name", nullable = false)
    @NotEmpty
    private String currency;

    /**
     * The name of the country where the currency is used.
     * Must not be empty.
     */
    @Column(name = "entity_location", nullable = false)
    @NotEmpty
    private String entityLocation;
}
