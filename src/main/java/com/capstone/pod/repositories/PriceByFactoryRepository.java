package com.capstone.pod.repositories;

import com.capstone.pod.entities.PriceByFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PriceByFactoryRepository extends JpaRepository<PriceByFactory,Integer> {
    Optional<PriceByFactory> getByProductIdAndFactoryId(int productId, int factoryId);
}
