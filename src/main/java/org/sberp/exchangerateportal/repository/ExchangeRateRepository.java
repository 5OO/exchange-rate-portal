package org.sberp.exchangerateportal.repository;

import org.sberp.exchangerateportal.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findByCurrency(String currency);

    @Query("SELECT e FROM ExchangeRate e WHERE e.date = (SELECT MAX(e2.date) FROM ExchangeRate e2)")
    List<ExchangeRate> findLatestRates();
}
