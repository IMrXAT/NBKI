package com.rodionov.nbki.app.dto;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record CreditGetDto (String id, Double creditAmount, Boolean isClosed, LocalDate creditStart, LocalDate creditEnd){ }
