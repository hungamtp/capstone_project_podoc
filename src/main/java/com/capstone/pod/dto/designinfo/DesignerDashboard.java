package com.capstone.pod.dto.designinfo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DesignerDashboard {
    private double income;
    private long designCount;
    private long designSoldCount;
}
