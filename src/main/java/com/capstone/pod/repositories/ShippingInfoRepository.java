package com.capstone.pod.repositories;

import com.capstone.pod.entities.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, String> {
    List<ShippingInfo> findAllByUserId(String userId);
}
