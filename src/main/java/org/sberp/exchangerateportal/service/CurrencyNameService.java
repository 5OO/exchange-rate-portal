package org.sberp.exchangerateportal.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sberp.exchangerateportal.model.CurrencyName;
import org.sberp.exchangerateportal.repository.CurrencyNameRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

    public CurrencyName getCurrencyByCode(String code) {
        return currencyNameRepository.findById(code).orElse(null);
    }

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
            log.debug("Getting xml file length is: {} bytes", xmlFile.length());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            List<CurrencyName> currencyNames = new ArrayList<>();
            NodeList nList = doc.getElementsByTagName("CcyNtry");
            log.debug("node list size is {} CcyNtry elements", nList.getLength());

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    CurrencyName currencyName = new CurrencyName();

                    if (eElement.getElementsByTagName("Ccy").getLength() > 0) {
                        currencyName.setAlphabeticCode(eElement.getElementsByTagName("Ccy").item(0).getTextContent());
                    }
                    if (eElement.getElementsByTagName("CcyNm").getLength() > 0) {
                        currencyName.setCurrency(eElement.getElementsByTagName("CcyNm").item(0).getTextContent());
                    }
                    if (eElement.getElementsByTagName("CtryNm").getLength() > 0) {
                        currencyName.setEntityLocation(eElement.getElementsByTagName("CtryNm").item(0).getTextContent());
                    }

                    if (currencyName.getAlphabeticCode() != null && currencyName.getCurrency() != null && currencyName.getEntityLocation() != null) {
                        currencyNames.add(currencyName);
                        log.debug("AlphabeticCode {} parsed in sequence of {}", currencyName.getAlphabeticCode(), temp);
                    }
                }
            }
            currencyNameRepository.saveAll(currencyNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
