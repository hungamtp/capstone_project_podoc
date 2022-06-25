package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;

import javax.persistence.*;
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
    private String name;
    @ManyToOne
    OrderDetail orderDetail;
}
