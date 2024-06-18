package com.rodionov.nbki.core.repo;

import com.rodionov.nbki.domain.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, String> {

}
