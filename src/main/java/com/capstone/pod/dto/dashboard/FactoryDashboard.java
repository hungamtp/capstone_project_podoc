package com.capstone.pod.dto.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FactoryDashboard {
    private double income;
    private double incomeCurrentMonth;
    private long inProcessOrder;
    private long doneOrder;
}
