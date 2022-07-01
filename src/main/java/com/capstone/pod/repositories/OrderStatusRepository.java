package com.capstone.pod.repositories;

import com.capstone.pod.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, String> {
    Optional<OrderStatus> findByName(String name);
}
