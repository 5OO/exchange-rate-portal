package org.sberp.exchangerateportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeignCurrencyExchangeRate {

    @JacksonXmlProperty(localName = "Tp")
    private String type;

    @JacksonXmlProperty(localName = "Dt")
    private String date;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "CcyAmt")
    private List<AmountOfCurrency> amounts;

}
