package com.capstone.pod.repositories.impl;

import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.OrderDetailRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class OrderDetailRepositoryCustomImpl implements OrderDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrderDetail> findAllOrderDetailIsPaidTrueOrderDetail(int page, int size, String userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderDetail> query = criteriaBuilder.createQuery(OrderDetail.class);
        Root<OrderDetail> root = query.from(OrderDetail.class);
        Join<OrderDetail, Orders> ordersJoin = root.join(OrderDetail_.ORDERS);
        Join<Orders, User> userJoin = ordersJoin.join(Orders_.USER);
        query.select(root);
        Predicate userIdEqual = criteriaBuilder.equal(userJoin.get(User_.ID), userId);
        Predicate orderIsPaidTrue = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
        query.where(userIdEqual , orderIsPaidTrue);
        return entityManager.createQuery(query).setMaxResults(size).setFirstResult((page - 1) * size).getResultList();
    }
}
