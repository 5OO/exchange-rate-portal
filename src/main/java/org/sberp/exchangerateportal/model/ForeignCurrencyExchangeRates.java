package org.sberp.exchangerateportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeignCurrencyExchangeRates {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "FxRate")
    private List<ForeignCurrencyExchangeRate> exchangeRates;

}
