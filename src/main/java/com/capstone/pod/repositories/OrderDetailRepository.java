package com.capstone.pod.repositories;

import com.capstone.pod.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> , OrderRepositoryCustom {
    List<OrderDetail> findAllByDesignedProductId(String designId);
}
