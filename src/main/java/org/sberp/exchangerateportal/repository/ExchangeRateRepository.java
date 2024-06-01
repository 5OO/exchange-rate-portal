package org.sberp.exchangerateportal.repository;

import org.sberp.exchangerateportal.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT e FROM ExchangeRate e WHERE e.id IN (SELECT MAX(e2.id) FROM ExchangeRate e2 GROUP BY e2.currency)")
    List<ExchangeRate> findLatestRates();
}
