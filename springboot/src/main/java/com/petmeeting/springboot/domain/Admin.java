package com.petmeeting.springboot.domain;

import com.petmeeting.springboot.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@DiscriminatorColumn(name = "ADMIN", length = 10)
public class Admin extends Users {

    @Transient
    private Role userGroup = Role.ROLE_ADMIN;
}
