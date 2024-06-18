package com.rodionov.nbki.core.service.impl;


import com.rodionov.nbki.app.dto.CreditCreateDto;
import com.rodionov.nbki.app.dto.CreditUpdateDto;
import com.rodionov.nbki.core.repo.CreditRepository;
import com.rodionov.nbki.core.service.CreditService;
import com.rodionov.nbki.domain.Credit;
import com.rodionov.nbki.platform.exception.CreditNotFoundException;
import com.rodionov.nbki.platform.mapper.CreditMapper;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;

    public CreditServiceImpl(CreditRepository creditRepository, CreditMapper creditMapper) {
        this.creditRepository = creditRepository;
        this.creditMapper = creditMapper;
    }

    @Override
    public String createCredit(CreditCreateDto dto) {
        log.info("Creating credit from DTO: {}", dto);

        Credit credit = creditMapper.CreditCreationDtoToEntity(dto);
        Credit savedCredit = creditRepository.save(credit);

        log.info("Credit created with id: {}", savedCredit.getId());
        return savedCredit.getId();

    }

    @Override
    @Transactional
    public void updateCredit(CreditUpdateDto dto, String id) {
        log.info("Updating credit with id: {} using DTO: {}", id, dto);

        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new CreditNotFoundException("Could not update credit with id: " + id + " because it does not exist"));
        creditMapper.updateCredit(credit, dto);
        Credit updatedCredit = creditRepository.save(credit);

        log.info("Credit updated successfully: {}", updatedCredit);
    }

    @Override
    public void deleteCredit(String id) {
        log.info("Deleting credit with id: {}", id);
        creditRepository.deleteById(id);
        log.info("Credit deleted successfully with id: {}", id);
    }

    @Override
    public Credit getCredit(String id) {
        log.info("Fetching credit with id: {}", id);
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new CreditNotFoundException("Could not find credit with id: " + id));
        log.info("Credit fetched successfully: {}", credit);
        return credit;
    }

    @Override
    public List<Credit> getAllCredits() {
        log.info("Fetching all credits");

        List<Credit> credits = creditRepository.findAll();

        log.info("Fetched {} credits", credits.size());
        return credits;
    }
}
