package com.capstone.pod.repositories;

import com.capstone.pod.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    Optional<Orders> findByTransactionId(String transactionId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "update orders  set transaction_id = ?2 where id = ?1")
    void updatePaymentId(int orderId , String paymentId);
}
