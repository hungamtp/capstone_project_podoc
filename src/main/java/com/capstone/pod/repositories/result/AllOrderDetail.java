package com.capstone.pod.repositories.result;

import com.capstone.pod.entities.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AllOrderDetail {
    private List<OrderDetail> result;
    private int elements ;
}
