package com.rodionov.nbki.app.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreditUpdateDto(Double creditAmount, Boolean isClosed, LocalDate creditStart) {
}
