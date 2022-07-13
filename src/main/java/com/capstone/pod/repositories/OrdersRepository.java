package com.capstone.pod.repositories;

import com.capstone.pod.entities.Orders;
import com.capstone.pod.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,String> {
    Optional<Orders> findByTransactionId(String transactionId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "update orders  set transaction_id = ?2 where id = ?1")
    void updatePaymentId(String orderId , String paymentId);

    Page<Orders> findAllByIsPaidIsFalseAndUser(Pageable pageable , User user);

}
