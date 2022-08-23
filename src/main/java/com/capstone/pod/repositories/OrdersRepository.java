package com.capstone.pod.repositories;

import com.capstone.pod.entities.Orders;
import com.capstone.pod.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,String> , OrderRepositoryCustom {
    Optional<Orders> findByTransactionId(String transactionId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "update orders  set transaction_id = ?2 where id = ?1")
    void updatePaymentId(String orderId , String paymentId);

    Page<Orders> findAllByUser(Pageable pageable , User user);
    Page<Orders> findAllByUserAndIsPaidAndCanceled(Pageable pageable , User user , Boolean isPaid , Boolean canceled);
    Page<Orders> findAllByUserAndIsPaid(Pageable pageable , User user , Boolean isPaid);
    Page<Orders> findAllByUserAndCanceled(Pageable pageable , User user , Boolean canceled);

    Long countByIsPaid(boolean isPaid);

    @Modifying
    @Query("update Orders o set o.canceled = true where o.id = :id")
    void setOrderIsCanceled(@Param("id") String id);

}
