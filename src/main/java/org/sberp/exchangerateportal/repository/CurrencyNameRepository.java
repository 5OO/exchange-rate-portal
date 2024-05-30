package org.sberp.exchangerateportal.repository;

import org.sberp.exchangerateportal.model.CurrencyName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyNameRepository extends JpaRepository<CurrencyName, String> {
}
