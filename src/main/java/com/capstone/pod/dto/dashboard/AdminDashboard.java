package com.capstone.pod.dto.dashboard;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AdminDashboard {
    private long orderCountCurrentMonth;
    private long designCount;
    private long orderCount;
    private long designSoldCount;
    private double income;
    private double incomeCurrentMonth;
    private long countMomoOrder;
    private long countZaloPayOrder;
    // count by category
    private List<CategorySoldCountProjection> categorySoldCountProjections;
}
