package com.rodionov.nbki.core.service;

import com.rodionov.nbki.app.dto.CreditCreateDto;
import com.rodionov.nbki.app.dto.CreditUpdateDto;
import com.rodionov.nbki.domain.Credit;

public interface CreditService {

    String createCredit(CreditCreateDto dto);
    void updateCredit(CreditUpdateDto dto, String id);
    void deleteCredit(String id);
    Credit getCredit(String id);
}
