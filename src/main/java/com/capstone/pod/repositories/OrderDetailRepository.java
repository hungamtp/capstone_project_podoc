package com.capstone.pod.repositories;

import com.capstone.pod.entities.Factory;
import com.capstone.pod.entities.OrderDetail;
import com.capstone.pod.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> , OrderDetailRepositoryCustom {
    List<OrderDetail> findAllByDesignedProductId(String designId);
    List<OrderDetail> findAllByFactoryId(String factoryId);
    List<OrderDetail> findAllByFactory(Factory factory);
    Optional<OrderDetail> findAllByOrdersIdAndDesignedProductIdAndColorAndSize(String orderId, String designId,String color, String size);
    List<OrderDetail> findAllByOrdersAndFactory(Orders orders ,Factory factory);
    List<OrderDetail> findAllByOrders(Orders orders );
    List<OrderDetail> findAllByOrdersIdAndDesignedProductIdAndFactoryId(String orderId, String designId, String factoryId);
}
