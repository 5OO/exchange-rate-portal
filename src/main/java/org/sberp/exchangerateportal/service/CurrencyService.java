package org.sberp.exchangerateportal.service;

import lombok.RequiredArgsConstructor;
import org.sberp.exchangerateportal.model.CurrencyName;
import org.sberp.exchangerateportal.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyName getCurrencyByCode(String code) {
        return currencyRepository.findById(code).orElse(null);
    }
}
