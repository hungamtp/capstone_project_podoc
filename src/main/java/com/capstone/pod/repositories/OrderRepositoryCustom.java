package com.capstone.pod.repositories;

import com.capstone.pod.repositories.impl.projection.FactoryRateProjection;

import java.time.LocalDateTime;

public interface OrderRepositoryCustom {
    Double getInComeByUserId(String userId, LocalDateTime startDate, LocalDateTime endDate);
    Long countSoldByUserId(String userId , LocalDateTime startDate, LocalDateTime endDate);
    Double getInComeByAdmin(LocalDateTime startDate, LocalDateTime endDate);
    Long countSoldAll(LocalDateTime startDate, LocalDateTime endDate);
    Long countZaloPay();
    Double getInComeByFactory(String factoryId , LocalDateTime startDate, LocalDateTime endDate);
    FactoryRateProjection getRateFactory(String factoryId);
}
