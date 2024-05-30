package org.sberp.exchangerateportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeignCurrencyExchangeRate {

    @JacksonXmlProperty(localName = "Tp")
    @NotNull
    private String type;

    @JacksonXmlProperty(localName = "Dt")
    @NotNull
    private String date;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "CcyAmt")
    @NotNull
    private List<AmountOfCurrency> amounts;

}
