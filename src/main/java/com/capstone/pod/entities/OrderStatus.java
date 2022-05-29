package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;
@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatus extends Auditable {
    @Id
    @GeneratedValue
    private int id;
    private String statusName;
    @OneToMany(mappedBy = "orderStatus")
    Collection<Orders> orders;
}
