package com.rodionov.nbki.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    @Id
    @UuidGenerator
    private String id;

    @Column
    private Double creditAmount;
    @Column
    private Boolean isClosed;
    @Column
    private LocalDate creditStart;
    @Column
    private LocalDate creditEnd;
}
