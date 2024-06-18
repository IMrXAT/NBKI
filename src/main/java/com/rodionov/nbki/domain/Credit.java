package com.rodionov.nbki.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
public class Credit {

    @Id
    @UuidGenerator
    private String id;

    private Double creditAmount;

    private LocalDate creditStart;
    private LocalDate creditEnd;
}
