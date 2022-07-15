package com.capstone.pod.repositories;

import java.time.LocalDate;

public interface OrderRepositoryCustom {
    Double getInComeByUserId(String userId, LocalDate startDate, LocalDate endDate);
}
