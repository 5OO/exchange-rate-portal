package org.sberp.exchangerateportal.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sberp.exchangerateportal.exception.XmlParsingException;
import org.sberp.exchangerateportal.model.CurrencyName;
import org.sberp.exchangerateportal.repository.CurrencyNameRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class CurrencyNameService {

    private final CurrencyNameRepository currencyNameRepository;

     @PostConstruct
    public void init() {
        if (currencyNameRepository.count() == 0) {
            log.debug("currencyNameRepository size is {} -> starting currencyName xml file parsing", currencyNameRepository.count());
            parseAndSaveCurrencyNameData();
            log.debug("end of parsing currency names xml file");
            log.debug("currencyNameRepository size is {} elements", currencyNameRepository.count());
        }
    }

    private void parseAndSaveCurrencyNameData() {
        try {
            File xmlFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("currencyNames/iso-4217-list-one.xml")).getFile());
            log.debug("Getting xml file: {}", xmlFile.exists());
            log.debug("xml file length is {} bytes", xmlFile.length());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            List<CurrencyName> currencyNames = new ArrayList<>();
            NodeList nList = doc.getElementsByTagName("CcyNtry");
            log.debug("node list size is {} CcyNtry elements", nList.getLength());

            for (int i = 0; i < nList.getLength(); i++) {
                Element element = (Element) nList.item(i);
                CurrencyName currencyName = parseCurrencyName(element);
                if (currencyName != null) {
                    currencyNames.add(currencyName);
                    log.debug("AlphabeticCode {} parsed in sequence of {}", currencyName.getAlphabeticCode(), i);
                }
            }
            currencyNameRepository.saveAll(currencyNames);
        } catch (Exception e) {
            log.error("Failed to parse and save currency name data", e);
            throw new XmlParsingException("Failed to parse and save currency name data", e);
        }
    }

    public CurrencyName parseCurrencyName(Element element) {
        String alphabeticCode = getElementValue(element, "Ccy");
        String currency = getElementValue(element, "CcyNm");
        String entityLocation = getElementValue(element, "CtryNm");

        if (alphabeticCode != null && currency != null && entityLocation != null) {
            CurrencyName currencyName = new CurrencyName();
            currencyName.setAlphabeticCode(alphabeticCode);
            currencyName.setCurrency(currency);
            currencyName.setEntityLocation(entityLocation);
            return currencyName;
        }
        return null;
    }

    public String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

}
