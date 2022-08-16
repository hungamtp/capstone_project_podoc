package com.capstone.pod.entities;

import com.capstone.pod.constant.order.OrderState;
import com.capstone.pod.dto.support.AuditableDateTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends AuditableDateTime implements Comparable<Orders>{
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
    private boolean refunded;
    private boolean canceled;
    private String cancelReason;
    @ManyToOne
    User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails;

    // Order can be cancel that has PENDING , CANCEL only
    public boolean canCancel() {
        return this.canceled ? false : this.orderDetails.stream().map(orderDetail -> orderDetail.latestStatus()).collect(Collectors.toList())
            .stream().filter(status -> !status.equals(OrderState.PENDING)).collect(Collectors.toList())
            .stream().filter(status -> !status.equals(OrderState.CANCEL)).collect(Collectors.toList())
            .size() == 0;
    }

    @Override
    public int compareTo(@NotNull Orders o) {
        return this.lastModifiedDate.isAfter(o.lastModifiedDate) ? -1 : 1;
    }
}
