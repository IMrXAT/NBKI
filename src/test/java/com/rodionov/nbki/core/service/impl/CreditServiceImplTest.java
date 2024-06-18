package com.rodionov.nbki.core.service.impl;

import com.rodionov.nbki.app.dto.CreditCreateDto;
import com.rodionov.nbki.app.dto.CreditUpdateDto;
import com.rodionov.nbki.core.repo.CreditRepository;
import com.rodionov.nbki.domain.Credit;
import com.rodionov.nbki.platform.exception.CreditNotFoundException;
import com.rodionov.nbki.platform.mapper.CreditMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class CreditServiceImplTest {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CreditMapper creditMapper;

    private CreditServiceImpl creditService;

    @BeforeEach
    public void setUp() {
        creditService = new CreditServiceImpl(creditRepository, creditMapper);
    }

    @Test
    public void createCredit_shouldReturnCreditId() {
        CreditCreateDto creditCreateDto = new CreditCreateDto(123.0);

        String creditId = creditService.createCredit(creditCreateDto);

        Credit savedCredit = creditRepository.findById(creditId).orElse(null);

        assertThat(savedCredit).isNotNull();
        assertThat(savedCredit.getId()).isEqualTo(creditId);
    }

    @Test
    public void createCredit_shouldSetIsClosedFalse(){
        CreditCreateDto creditCreateDto = new CreditCreateDto(123.0);
        // Установим необходимые поля для creditCreateDto

        String creditId = creditService.createCredit(creditCreateDto);

        Credit savedCredit = creditRepository.findById(creditId).orElse(null);

        assertThat(savedCredit).isNotNull();
        assertThat(savedCredit.getIsClosed()).isEqualTo(false);
    }

    @Test
    //can fail at 00:00
    public void createCredit_shouldSetCreditStartToday(){
        CreditCreateDto creditCreateDto = new CreditCreateDto(123.0);
        String creditId = creditService.createCredit(creditCreateDto);

        Credit savedCredit = creditRepository.findById(creditId).orElse(null);

        assertThat(savedCredit).isNotNull();
        assertThat(savedCredit.getCreditStart()).isEqualTo(LocalDate.now());
    }

    @Test
    public void createCredit_shouldSetCreditEndNull(){
        CreditCreateDto creditCreateDto = new CreditCreateDto(123.0);
        String creditId = creditService.createCredit(creditCreateDto);

        Credit savedCredit = creditRepository.findById(creditId).orElse(null);

        assertThat(savedCredit).isNotNull();
        assertThat(savedCredit.getCreditEnd()).isNull();
    }



    @Test
    public void updateCredit_shouldUpdateExistingCredit() {
        Credit credit = new Credit();
        creditRepository.save(credit);

        CreditUpdateDto creditUpdateDto = new CreditUpdateDto(null, null, null);

        creditService.updateCredit(creditUpdateDto, credit.getId());

        Credit updatedCredit = creditRepository.findById(credit.getId()).orElse(null);
        assertThat(updatedCredit).isNotNull();
    }

    @Test
    public void updateCredit_isClosedTrue_shouldSetCreditEndDate() {
        Credit credit = new Credit();
        credit.setIsClosed(false);
        credit.setCreditStart(LocalDate.now());
        credit.setCreditEnd(null);
        String creditId = creditRepository.save(credit).getId();

        CreditUpdateDto updateDto = new CreditUpdateDto(null, true, null);
        creditService.updateCredit(updateDto, creditId);

        Credit updatedCredit = creditRepository.findById(creditId).orElse(null);
        assertThat(updatedCredit).isNotNull();
        assertThat(updatedCredit.getIsClosed()).isTrue();
        assertThat(updatedCredit.getCreditEnd()).isEqualTo(LocalDate.now());
    }

    @Test
    public void updateCredit_isClosedFalse_shouldSetCreditEndDateToNull() {
        Credit credit = new Credit();
        credit.setIsClosed(true);
        credit.setCreditEnd(LocalDate.now());
        String creditId = creditRepository.save(credit).getId();

        CreditUpdateDto updateDto = new CreditUpdateDto(null, false, null);

        creditService.updateCredit(updateDto, creditId);

        Credit updatedCredit = creditRepository.findById(creditId).orElse(null);
        assertThat(updatedCredit).isNotNull();
        assertThat(updatedCredit.getIsClosed()).isFalse();
        assertThat(updatedCredit.getCreditEnd()).isNull();
    }

    @Test
    public void updateCredit_shouldThrowExceptionIfCreditNotFound() {
        String nonExistentCreditId = UUID.randomUUID().toString();

        assertThatThrownBy(() -> creditService.updateCredit(null, nonExistentCreditId))
                .isInstanceOf(CreditNotFoundException.class);
    }

    @Test
    public void deleteCredit_shouldDeleteExistingCredit() {
        Credit credit = new Credit();
        creditRepository.save(credit);

        creditService.deleteCredit(credit.getId());

        assertThat(creditRepository.findById(credit.getId())).isEmpty();
    }

    @Test
    public void getCredit_shouldReturnCredit() {
        Credit credit = new Credit();
        creditRepository.save(credit);

        Credit foundCredit = creditService.getCredit(credit.getId());

        assertThat(foundCredit).isNotNull();
        assertThat(foundCredit.getId()).isEqualTo(credit.getId());
    }

    @Test
    public void getCredit_shouldThrowExceptionIfNotFound() {
        String nonExistentCreditId = UUID.randomUUID().toString();

        assertThatThrownBy(() -> creditService.getCredit(nonExistentCreditId))
                .isInstanceOf(CreditNotFoundException.class);
    }

    @Test
    public void getAllCredits_shouldReturnAllCredits() {
        Credit credit1 = new Credit();
        Credit credit2 = new Credit();
        creditRepository.save(credit1);
        creditRepository.save(credit2);

        List<Credit> credits = creditService.getAllCredits();

        assertThat(credits).hasSize(2);
    }
}