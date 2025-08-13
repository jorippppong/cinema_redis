package com.cinema.core.domains.user;

import com.cinema.core.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "user_account")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public User toUser() {
        return new User(id, name);
    }
}
