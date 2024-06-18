package com.rodionov.nbki.core;

import com.rodionov.nbki.app.dto.CreditDto;

public interface CreditService {

    void createCredit(CreditDto dto);
    void updateCredit(CreditDto dto, String id);
    void deleteCredit(String id);
    void getCredit(String id);
}
