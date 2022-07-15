package com.capstone.pod.repositories.impl;

import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.OrderRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public Double getInComeByUserId(String userId, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = criteriaBuilder.createQuery(Double.class);
        Root<OrderDetail> root = query.from(OrderDetail.class);
        Join<OrderDetail, Orders> ordersJoin = root.join(OrderDetail_.ORDERS);
        Join<OrderDetail, DesignedProduct> detailDesignedProductJoin = root.join(OrderDetail_.DESIGNED_PRODUCT);
        Join<DesignedProduct, User> userJoin = detailDesignedProductJoin.join(DesignedProduct_.USER);
        Expression<Number> income = criteriaBuilder.prod(root.get(OrderDetail_.QUANTITY), detailDesignedProductJoin.get(DesignedProduct_.DESIGNED_PRICE));
        query.multiselect(criteriaBuilder.sum(income));
        Predicate orderEqual = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
        Predicate designerEqual = criteriaBuilder.equal(userJoin.get(User_.ID) , userId);
        Predicate dateBetween = criteriaBuilder.between(ordersJoin.get(Orders_.CREATE_DATE) , startDate , endDate);
        query.where(orderEqual , designerEqual , dateBetween);
        return entityManager.createQuery(query).getSingleResult();
    }
}
