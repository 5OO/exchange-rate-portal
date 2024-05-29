package org.sberp.exchangerateportal.repository;

import org.sberp.exchangerateportal.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findByCurrency(String currency);
}
