package com.capstone.pod.repositories.impl;

import com.capstone.pod.dto.designedProduct.DesignedProductDetailDto;
import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.DesignedProductRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DesignedProductRepositoryCustomImpl implements DesignedProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;


    public List<DesignedProduct> get4HighestRateDesignedProduct() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignedProduct> query = criteriaBuilder.createQuery(DesignedProduct.class);
        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        root.join(DesignedProduct_.DESIGN_COLORS, JoinType.LEFT);
        Join<DesignedProduct, Rating> ratingJoin = root.join(DesignedProduct_.RATINGS, JoinType.LEFT);
        Join<DesignedProduct, Product> productJoin = root.join(DesignedProduct_.PRODUCT, JoinType.LEFT);
        root.join(DesignedProduct_.ORDER_DETAILS, JoinType.INNER);
        root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
        query.select(root);
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.avg(ratingJoin.get(Rating_.RATING_STAR))));
        Predicate publishTrue = criteriaBuilder.isTrue(root.get(DesignedProduct_.PUBLISH));
        Predicate productIsDelete = criteriaBuilder.isFalse(productJoin.get(Product_.IS_DELETED));
        Predicate isPublicTrue = criteriaBuilder.isTrue(productJoin.get(Product_.IS_PUBLIC));
        query.orderBy(orderList).where(publishTrue , isPublicTrue , productIsDelete);
        return entityManager.createQuery(query).setMaxResults(4).getResultList();
    }

    @Override
    public List<DesignedProduct> get4HighestRateDesignedProductByProductId(String productId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignedProduct> query = criteriaBuilder.createQuery(DesignedProduct.class);
        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        root.join(DesignedProduct_.DESIGN_COLORS, JoinType.LEFT);
        Join<DesignedProduct, Rating> ratingJoin = root.join(DesignedProduct_.RATINGS, JoinType.LEFT);
        root.join(DesignedProduct_.ORDER_DETAILS, JoinType.INNER);
        root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        Join<DesignedProduct, Product> productJoin = root.join(DesignedProduct_.PRODUCT, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
        query.select(root);
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.avg(ratingJoin.get(Rating_.RATING_STAR))));
        Predicate publishTrue = criteriaBuilder.isTrue(root.get(DesignedProduct_.PUBLISH));
        Predicate productIdEqual = criteriaBuilder.equal(productJoin.get(Product_.ID), productId);
        Predicate productIsDelete = criteriaBuilder.isFalse(productJoin.get(Product_.IS_DELETED));
        Predicate isPublicTrue = criteriaBuilder.isTrue(productJoin.get(Product_.IS_PUBLIC));
        query.orderBy(orderList).where(publishTrue, productIdEqual , productIsDelete , isPublicTrue);
        return entityManager.createQuery(query).setMaxResults(4).getResultList();
    }

    @Override
    public List<DesignedProduct> get4BestSeller() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignedProduct> query = criteriaBuilder.createQuery(DesignedProduct.class);
        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        Join<DesignedProduct, Product> productJoin = root.join(DesignedProduct_.PRODUCT, JoinType.LEFT);
        root.join(DesignedProduct_.DESIGN_COLORS, JoinType.LEFT);
        root.join(DesignedProduct_.RATINGS, JoinType.LEFT);
        root.join(DesignedProduct_.ORDER_DETAILS, JoinType.INNER);
        root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        Join<DesignedProduct, OrderDetail> orderDetailJoin = root.join(DesignedProduct_.ORDER_DETAILS, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
        query.select(root);
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.sum(orderDetailJoin.get(OrderDetail_.QUANTITY))));
        Predicate productIsDelete = criteriaBuilder.isFalse(productJoin.get(Product_.IS_DELETED));
        Predicate publish = criteriaBuilder.isTrue(root.get(DesignedProduct_.PUBLISH));
        Predicate isPublicTrue = criteriaBuilder.isTrue(productJoin.get(Product_.IS_PUBLIC));
        query.orderBy(orderList).where(productIsDelete , isPublicTrue , publish);
        return entityManager.createQuery(query).setMaxResults(4).getResultList();
    }

    @Override
    public List<DesignedProduct> get4BestSellerById(String productId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignedProduct> query = criteriaBuilder.createQuery(DesignedProduct.class);
        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        root.join(DesignedProduct_.DESIGN_COLORS, JoinType.LEFT);
        root.join(DesignedProduct_.RATINGS, JoinType.LEFT);
        root.join(DesignedProduct_.ORDER_DETAILS, JoinType.INNER);
        root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        Join<DesignedProduct, Product> productJoin = root.join(DesignedProduct_.PRODUCT, JoinType.LEFT);
        Join<DesignedProduct, OrderDetail> orderDetailJoin = root.join(DesignedProduct_.ORDER_DETAILS, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
        query.select(root);
        List<Order> orderList = new ArrayList();
        Predicate productIdEqual = criteriaBuilder.equal(productJoin.get(Product_.ID), productId);
        Predicate publishTrue = criteriaBuilder.isTrue(root.get(DesignedProduct_.PUBLISH));
        orderList.add(criteriaBuilder.desc(criteriaBuilder.sum(orderDetailJoin.get(OrderDetail_.QUANTITY))));
        Predicate productIsDelete = criteriaBuilder.isFalse(productJoin.get(Product_.IS_DELETED));
        Predicate isPublicTrue = criteriaBuilder.isTrue(productJoin.get(Product_.IS_PUBLIC));
        query.orderBy(orderList).where(productIdEqual , publishTrue , productIsDelete , isPublicTrue);
        return entityManager.createQuery(query).setMaxResults(4).getResultList();
    }
}


