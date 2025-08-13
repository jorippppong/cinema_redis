package com.cinema.core.domains.screen;

import com.cinema.core.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "screen")
public class ScreenEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private TheaterEntity theater;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
