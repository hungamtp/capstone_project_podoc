package com.capstone.pod.repositories;

import java.time.LocalDate;

public interface OrderRepositoryCustom {
    Double getInComeByUserId(String userId, LocalDate startDate, LocalDate endDate);
    Long countSoldByUserId(String userId , LocalDate startDate, LocalDate endDate);
    Double getInComeByAdmin(LocalDate startDate, LocalDate endDate);
    Long countSoldAll(LocalDate startDate, LocalDate endDate);
    Long countZaloPay();
    Double getInComeByFactory(String factoryId , LocalDate startDate, LocalDate endDate);
}
