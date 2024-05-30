package org.sberp.exchangerateportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmountOfCurrency {

    /** Also known as alphabetic code aka USD */
    @JacksonXmlProperty(localName = "Ccy")
    @NotNull
    private String currency;

    @JacksonXmlProperty(localName = "Amt")
    @NotNull
    private Double amount;
}
