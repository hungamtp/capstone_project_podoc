package com.capstone.pod.repositories;

import com.capstone.pod.entities.Factory;
import com.capstone.pod.entities.OrderDetail;

import com.capstone.pod.entities.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> , OrderDetailRepositoryCustom {
    List<OrderDetail> findAllByDesignedProductId(String designId);
    List<OrderDetail> findAllByFactoryId(String factoryId);
    List<OrderDetail> findAllByFactory(Factory factory);
    OrderDetail findByOrdersIdAndDesignedProductId(String orderId, String designId);
    List<OrderDetail> findAllByOrders(Orders orders);
    List<OrderDetail> findAllByOrdersIdAndDesignedProductIdAndFactoryId(String orderId, String designId, String factoryId);
}
