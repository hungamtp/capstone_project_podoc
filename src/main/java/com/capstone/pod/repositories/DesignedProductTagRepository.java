package com.capstone.pod.repositories;

import com.capstone.pod.entities.DesignedProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DesignedProductTagRepository extends JpaRepository<DesignedProductTag, Integer> {
    List<DesignedProductTag> findAllByDesignedProductId(int designId);
}
