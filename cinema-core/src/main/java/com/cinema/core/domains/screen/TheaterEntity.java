package com.cinema.core.domains.screen;

import com.cinema.core.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "theater")
public class TheaterEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
