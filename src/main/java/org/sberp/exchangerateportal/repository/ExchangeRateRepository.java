package org.sberp.exchangerateportal.repository;

import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.sberp.exchangerateportal.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

}
