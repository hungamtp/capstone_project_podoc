package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String image;
    private boolean isMailVerified;

    @ManyToOne(cascade = CascadeType.ALL)
    private Role role;

    @OneToOne
    private User user;

    @OneToOne
    private Factory factory;

}
