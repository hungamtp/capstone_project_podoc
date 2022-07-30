package com.capstone.pod.repositories.impl;

import com.capstone.pod.dto.dashboard.CategorySoldCountProjection;
import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.OrderDetailRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
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
        List<Order> orders = new ArrayList<>();
        orders.add(criteriaBuilder.desc(ordersJoin.get(Orders_.CREATE_DATE)));
        query.where(userIdEqual, orderIsPaidTrue).orderBy(orders);
        return entityManager.createQuery(query).setMaxResults(size).setFirstResult((page - 1) * size).getResultList();
    }

    public List<CategorySoldCountProjection> countOrderByCategory() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategorySoldCountProjection> query = criteriaBuilder.createQuery(CategorySoldCountProjection.class);
        Root<OrderDetail> root = query.from(OrderDetail.class);
        Join<OrderDetail, Orders> ordersJoin = root.join(OrderDetail_.ORDERS);
        Join<OrderDetail, DesignedProduct> designedProductJoin = root.join(OrderDetail_.DESIGNED_PRODUCT);
        Join<DesignedProduct, Product> productJoin = designedProductJoin.join(DesignedProduct_.PRODUCT);
        Join<Product, Category> categoryJoin = productJoin.join(Product_.CATEGORY);
//        Expression<Number> income = criteriaBuilder.prod(root.get(OrderDetail_.QUANTITY), designedProductJoin.get(DesignedProduct_.DESIGNED_PRICE));
//        query.multiselect(criteriaBuilder.sum(income));
        query.multiselect(
            categoryJoin.get(Category_.ID),
            categoryJoin.get(Category_.NAME),
            categoryJoin.get(Category_.IMAGE),
            criteriaBuilder.sum(root.get(OrderDetail_.QUANTITY)).alias("count"));
        Predicate orderIsPaidTrue = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
        query.groupBy(categoryJoin.get(Category_.ID));
        List<Order> orders = new ArrayList<>();
        orders.add(criteriaBuilder.asc(ordersJoin.get(Orders_.CREATE_DATE)));
        query.where(orderIsPaidTrue);
        return entityManager.createQuery(query).getResultList();
    }

}
