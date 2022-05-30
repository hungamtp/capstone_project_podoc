package com.capstone.pod.repositories.impl;

import com.capstone.pod.dto.designedProduct.DesignedProductDTO;
import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.DesignedProductRepositoryCustom;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DesignedProductRepositoryCustomImpl implements DesignedProductRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;


    public List<DesignedProductDTO> get4BestSellerDesignedProduct(){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();;
        CriteriaQuery<DesignedProductDTO> query = criteriaBuilder.createQuery(DesignedProductDTO.class);
        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        Join<DesignedProduct , Rating> ratingJoin = root.join(DesignedProduct_.RATINGS , JoinType.LEFT);
        Join<DesignedProduct , Tag> tagJoin = root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS).join(DesignedProductTag_.TAG , JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
//        query.groupBy(tagJoin.get(Tag_.ID));
        query.multiselect(
            root.get(DesignedProduct_.ID),
            root.get(DesignedProduct_.NAME),
            root.get(DesignedProduct_.IMAGE),
            root.get(DesignedProduct_.PRICE),
            criteriaBuilder.avg(ratingJoin.get(Rating_.ratingStar)),
            tagJoin.get(Tag_.NAME).as(ArrayList.class).alias("tags")
        );
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.avg(ratingJoin.get(Rating_.ratingStar))));
        query.orderBy(orderList);
        return entityManager.createQuery(query).setMaxResults(4).getResultList();
    }

}


