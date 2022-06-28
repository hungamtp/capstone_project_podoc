package com.capstone.pod.repositories;

import com.capstone.pod.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    Optional<Orders> findByTransactionId(String transactionId);
}
