package com.rodionov.nbki.core.service.impl;


import com.rodionov.nbki.app.dto.CreditCreateDto;
import com.rodionov.nbki.app.dto.CreditUpdateDto;
import com.rodionov.nbki.core.repo.CreditRepository;
import com.rodionov.nbki.core.service.CreditService;
import com.rodionov.nbki.domain.Credit;
import com.rodionov.nbki.platform.exception.CreditNotFoundException;
import com.rodionov.nbki.platform.mapper.CreditMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;

    public CreditServiceImpl(CreditRepository creditRepository, CreditMapper creditMapper) {
        this.creditRepository = creditRepository;
        this.creditMapper = creditMapper;
    }

    @Override
    public String createCredit(CreditCreateDto dto) {
        Credit credit = creditMapper.CreditCreationDtoToEntity(dto);
        return creditRepository.save(credit).getId();
    }

    @Override
    @Transactional
    public void updateCredit(CreditUpdateDto dto, String id) {
        Credit credit = creditRepository.findById(id).orElseThrow(() -> new CreditNotFoundException("could not update credit with id: " + id + " because it does not exist"));
        creditMapper.updateCredit(credit, dto);
        creditRepository.save(credit);
    }

    @Override
    public void deleteCredit(String id) {
        creditRepository.deleteById(id);
    }

    @Override
    public Credit getCredit(String id) {
        return creditRepository.findById(id).orElseThrow(() -> new CreditNotFoundException("could not find credit with id " + id));
    }
}
