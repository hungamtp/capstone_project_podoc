package com.capstone.pod.repositories;

import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.DesignedProductTag;
import com.capstone.pod.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignedProductTagRepository extends JpaRepository<DesignedProductTag, String> {
    List<DesignedProductTag> findAllByDesignedProductId(String designId);
    List<DesignedProductTag> findByDesignedProduct(DesignedProduct designedProduct);
    Optional<DesignedProductTag> findByDesignedProductAndTag(DesignedProduct designedProduct , Tag tag);
    List<DesignedProductTag> findByTag(Tag tag);
}
