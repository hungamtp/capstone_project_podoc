package com.capstone.pod.entities;

import com.capstone.pod.constant.order.OrderState;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private int quantity;
    private String color;
    private String size;
    private boolean canceled;
    private String reasonByUser;
    private String reasonByFactory;
    private boolean isRate;
    @ManyToOne
    private Orders orders;
    @ManyToOne
    private DesignedProduct designedProduct;
    @ManyToOne
    private Factory factory;
    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL)
    List<OrderStatus> orderStatuses;
    @OneToOne(mappedBy = "orderDetail")
    private PrintingInfo printingInfo;

    public boolean isDone() {
        return this.orderStatuses.stream().filter(orderStatus -> orderStatus.getName().equals(OrderState.DONE))
            .collect(Collectors.toList()).size() != 0;
    }
    public boolean isCancel() {
        return canceled;
    }

    public boolean isCanceled() {
        return this.orderStatuses.stream().filter(orderStatus -> orderStatus.getName().equals(OrderState.CANCEL))
            .collect(Collectors.toList()).size() != 0;
    }

    public String latestStatus() {
        String result = "";
        LocalDateTime localDateTime = LocalDateTime.MIN;
        for (var status : this.orderStatuses) {
            if (status.getCreateDate().isAfter(localDateTime)) {
                localDateTime = status.getCreateDate();
                result = status.getName();
            }
        }
        return result;
    }
}
