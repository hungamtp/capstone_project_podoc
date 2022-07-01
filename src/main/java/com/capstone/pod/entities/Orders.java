package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends Auditable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String transactionId;
    private double price;
    private String address;
    private String phone;
    private String customerName;
    private boolean isPaid;
    @ManyToOne
    User user;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL)
    Collection<OrderDetail> orderDetails;
}
