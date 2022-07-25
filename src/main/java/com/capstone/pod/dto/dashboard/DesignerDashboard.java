package com.capstone.pod.dto.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DesignerDashboard {
    private double income;
    private double incomeCurrentMonth;
    private long designCount;
    private long designSoldCount;
    private long designSoldCountCurrentMonth;
}
