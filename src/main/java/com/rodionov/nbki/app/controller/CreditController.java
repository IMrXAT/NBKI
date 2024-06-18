package com.rodionov.nbki.app.controller;

import com.rodionov.nbki.app.dto.CreditCreateDto;
import com.rodionov.nbki.app.dto.CreditGetDto;
import com.rodionov.nbki.app.dto.CreditUpdateDto;
import com.rodionov.nbki.core.service.CreditService;
import com.rodionov.nbki.domain.Credit;
import com.rodionov.nbki.platform.mapper.CreditMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {

    private final CreditService creditService;

    private final CreditMapper creditMapper;

    public CreditController(CreditService creditService, CreditMapper creditMapper) {
        this.creditService = creditService;
        this.creditMapper = creditMapper;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createCredit(@RequestBody CreditCreateDto credit) {
        return creditService.createCredit(credit);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCredit(@PathVariable String id, @RequestBody CreditUpdateDto credit) {
        creditService.updateCredit(credit, id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditGetDto getCredit(@PathVariable String id) {
        Credit credit = creditService.getCredit(id);
        return creditMapper.EntityToCreditGetDto(credit);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCredit(@PathVariable String id) {
        creditService.deleteCredit(id);
    }
}
