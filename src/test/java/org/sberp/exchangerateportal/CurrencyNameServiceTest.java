package org.sberp.exchangerateportal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sberp.exchangerateportal.model.CurrencyName;
import org.sberp.exchangerateportal.repository.CurrencyNameRepository;
import org.sberp.exchangerateportal.service.CurrencyNameService;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyNameServiceTest {

    @InjectMocks
    private CurrencyNameService currencyNameService;

    @Mock
    private CurrencyNameRepository currencyNameRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(currencyNameService, "currencyNameRepository", currencyNameRepository);
    }

    @Test
    void testInit_WhenRepositoryIsNotEmpty_ShouldNotParseAndSaveCurrencyNames() {
        when(currencyNameRepository.count()).thenReturn(10L);

        currencyNameService.init();

        verify(currencyNameRepository, times(1)).count();
        verify(currencyNameRepository, never()).saveAll(any());
    }

    @Test
    void testParseAndSaveCurrencyNameData() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File("src/test/resources/currencyNames/iso-4217.xml"));
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("CcyNtry");
        Element element = (Element) nList.item(0);
        CurrencyName currencyName = currencyNameService.parseCurrencyName(element);

        assertNotNull(currencyName);
        assertEquals("EUR", currencyName.getAlphabeticCode());
        assertEquals("Euro", currencyName.getCurrency());
        assertEquals("ANDORRA", currencyName.getEntityLocation());
    }

    @Test
    void testGetElementValue() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File("src/test/resources/currencyNames/iso-4217.xml"));
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("CcyNtry");
        Element element = (Element) nList.item(0);
        String alphabeticCode = currencyNameService.getElementValue(element, "Ccy");

        assertEquals("EUR", alphabeticCode);
    }
}
