package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
    },
    indexes = {
        @Index( columnList = "email , phone")
    }
)
public class Credential extends Auditable{
    @Id
    @GeneratedValue
    private int id;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String image;

    @ManyToOne(cascade = CascadeType.ALL)
    private Role role;

    @OneToOne
    private User user;

    @OneToOne
    private Factory factory;

}
