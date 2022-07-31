package com.capstone.pod.entities;

import com.capstone.pod.dto.support.Auditable;
import com.capstone.pod.dto.support.AuditableDateTime;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Collection;
import java.util.Comparator;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatus extends AuditableDateTime implements Comparable<OrderStatus> {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;
    private String name;
    @ManyToOne
    OrderDetail orderDetail;

    @Override
    public int compareTo(@NotNull OrderStatus o) {
        return this.lastModifiedDate.isAfter(o.lastModifiedDate) ? -1 : 1;
    }


}
