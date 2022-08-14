package com.capstone.pod.repositories;

import com.capstone.pod.dto.dashboard.CategorySoldCountProjection;
import com.capstone.pod.repositories.impl.projection.AllOrderDetail;

import java.util.List;

public interface OrderDetailRepositoryCustom {
    AllOrderDetail findAllOrderDetailIsPaidTrueOrderDetail(int page, int size, String userId);
    List<CategorySoldCountProjection> countOrderByCategory();
}
