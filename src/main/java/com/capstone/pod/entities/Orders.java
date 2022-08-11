package com.capstone.pod.entities;

import com.capstone.pod.constant.order.OrderState;
import com.capstone.pod.dto.support.AuditableDateTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends AuditableDateTime {
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
    private String appTransId;
    private boolean isPaid;
    private boolean canceled;
    @ManyToOne
    User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails;

    // Order can be cancel that has PENDING , CANCEL only
    public boolean canCancel() {
        return this.orderDetails
            .stream().flatMap(orderDetails -> orderDetails.getOrderStatuses().stream()).collect(Collectors.toList())
            .stream().filter(orderStatus -> !orderStatus.getName().equals(OrderState.PENDING)).collect(Collectors.toList())
            .stream().filter(orderStatus -> !orderStatus.getName().equals(OrderState.CANCEL)).collect(Collectors.toList())
            .size() == 0;
    }
}
