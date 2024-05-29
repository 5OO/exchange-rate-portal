package org.sberp.exchangerateportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmountOfCurrency {

    @JacksonXmlProperty(localName = "Ccy")
    private String currency;

    @JacksonXmlProperty(localName = "Amt")
    private Double amount;
}
