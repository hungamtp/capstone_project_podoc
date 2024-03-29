package com.capstone.pod.repositories.impl;

import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.OrderRepositoryCustom;
import com.capstone.pod.repositories.impl.projection.FactoryRateProjection;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public Double getInComeByUserId(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Double> query = criteriaBuilder.createQuery(Double.class);
            Root<OrderDetail> root = query.from(OrderDetail.class);
            Join<OrderDetail, Orders> ordersJoin = root.join(OrderDetail_.ORDERS);
            Join<OrderDetail, DesignedProduct> detailDesignedProductJoin = root.join(OrderDetail_.DESIGNED_PRODUCT);
            Join<DesignedProduct, User> userJoin = detailDesignedProductJoin.join(DesignedProduct_.USER);
            Expression<Number> income = criteriaBuilder.prod(root.get(OrderDetail_.QUANTITY), detailDesignedProductJoin.get(DesignedProduct_.DESIGNED_PRICE));
            query.multiselect(criteriaBuilder.sum(income));
            Predicate orderEqual = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
            Predicate notCanceled = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
            Predicate factoryNotCanceled = criteriaBuilder.isFalse(root.get(OrderDetail_.CANCELED));
            Predicate designerEqual = criteriaBuilder.equal(userJoin.get(User_.ID), userId);
            Predicate dateBetween = criteriaBuilder.between(ordersJoin.get(Orders_.CREATE_DATE), startDate.minusDays(1), endDate);
            query.where(orderEqual, designerEqual, dateBetween, notCanceled, factoryNotCanceled);
            return entityManager.createQuery(query).getSingleResult();
        } catch (IllegalArgumentException e) {
            return 0d;
        }
    }

    public Long countSoldByUserId(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<OrderDetail> root = query.from(OrderDetail.class);
            Join<OrderDetail, Orders> ordersJoin = root.join(OrderDetail_.ORDERS);
            Join<OrderDetail, DesignedProduct> detailDesignedProductJoin = root.join(OrderDetail_.DESIGNED_PRODUCT);
            Join<DesignedProduct, User> userJoin = detailDesignedProductJoin.join(DesignedProduct_.USER);
            query.multiselect(criteriaBuilder.sum(root.get(OrderDetail_.QUANTITY)));
            Predicate orderEqual = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
            Predicate designerEqual = criteriaBuilder.equal(userJoin.get(User_.ID), userId);
            Predicate paidOrder = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
            Predicate notCanceled = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
            Predicate factoryNotCanceled = criteriaBuilder.isFalse(root.get(OrderDetail_.CANCELED));
            Predicate dateBetween = criteriaBuilder.between(ordersJoin.get(Orders_.CREATE_DATE), startDate.minusDays(1), endDate);
            query.where(orderEqual, designerEqual, dateBetween, paidOrder, notCanceled, factoryNotCanceled);
            return entityManager.createQuery(query).getSingleResult();
        } catch (IllegalArgumentException e) {
            return 0l;
        }

    }

    public Double getInComeByAdmin(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Double> query = criteriaBuilder.createQuery(Double.class);
            Root<OrderDetail> root = query.from(OrderDetail.class);
            Join<OrderDetail, Orders> ordersJoin = root.join(OrderDetail_.ORDERS);
            Join<OrderDetail, DesignedProduct> detailDesignedProductJoin = root.join(OrderDetail_.DESIGNED_PRODUCT);
            Join<DesignedProduct, PriceByFactory> priceByFactoryJoin = detailDesignedProductJoin.join(DesignedProduct_.PRICE_BY_FACTORY);
            Join<PriceByFactory, Factory> factoryJoin = priceByFactoryJoin.join(PriceByFactory_.FACTORY);
            Expression<Number> incomeBefore = criteriaBuilder.prod(root.get(OrderDetail_.QUANTITY), priceByFactoryJoin.get(PriceByFactory_.PRICE));
            Expression<Number> afterCommission = criteriaBuilder.prod(factoryJoin.get(Factory_.TRADE_DISCOUNT), incomeBefore);
            query.multiselect(criteriaBuilder.sum(afterCommission));
            Predicate orderEqual = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
            Predicate notCanceled = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
            Predicate factoryNotCanceled = criteriaBuilder.isFalse(root.get(OrderDetail_.CANCELED));
            Predicate dateBetween = criteriaBuilder.between(ordersJoin.get(Orders_.CREATE_DATE), startDate.minusDays(1), endDate);
            query.where(orderEqual, dateBetween, notCanceled, factoryNotCanceled);
            return entityManager.createQuery(query).getSingleResult();
        } catch (IllegalArgumentException e) {
            return 0d;
        } catch (NoResultException e) {
            return 0d;
        }
    }

    public Double getInComeByFactory(String factoryId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Double> query = criteriaBuilder.createQuery(Double.class);
            Root<OrderDetail> root = query.from(OrderDetail.class);
            Join<OrderDetail, Orders> ordersJoin = root.join(OrderDetail_.ORDERS);
            Join<OrderDetail, DesignedProduct> detailDesignedProductJoin = root.join(OrderDetail_.DESIGNED_PRODUCT);
            Join<DesignedProduct, PriceByFactory> priceByFactoryJoin = detailDesignedProductJoin.join(DesignedProduct_.PRICE_BY_FACTORY);
            Join<PriceByFactory, Factory> factoryFactoryJoin = priceByFactoryJoin.join(PriceByFactory_.FACTORY);
            Expression<Number> income = criteriaBuilder.prod(root.get(OrderDetail_.QUANTITY)
                , criteriaBuilder.sum(priceByFactoryJoin.get(PriceByFactory_.PRICE), detailDesignedProductJoin.get(DesignedProduct_.DESIGNED_PRICE)));
            query.multiselect(criteriaBuilder.sum(income));
            Predicate orderEqual = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
            Predicate notCanceled = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
            Predicate factoryNotCanceled = criteriaBuilder.isFalse(root.get(OrderDetail_.CANCELED));
            Predicate dateBetween = criteriaBuilder.between(ordersJoin.get(Orders_.CREATE_DATE), startDate.minusDays(1), endDate);
            Predicate factoryEqual = criteriaBuilder.equal(factoryFactoryJoin.get(Factory_.ID), factoryId);
            query.where(orderEqual, dateBetween, factoryEqual, notCanceled, factoryNotCanceled);
            return entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            return Double.valueOf(0);
        }
    }

    public Long countSoldAll(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<OrderDetail> root = query.from(OrderDetail.class);
            Join<OrderDetail, Orders> ordersJoin = root.join(OrderDetail_.ORDERS);
            query.multiselect(criteriaBuilder.sum(root.get(OrderDetail_.QUANTITY)));
            Predicate orderEqual = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
            Predicate cancel = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
            Predicate notCanceled = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
            Predicate factoryNotCanceled = criteriaBuilder.isFalse(root.get(OrderDetail_.CANCELED));
            Predicate dateBetween = criteriaBuilder.between(ordersJoin.get(Orders_.CREATE_DATE), startDate.minusDays(1), endDate);
            query.where(orderEqual, dateBetween, notCanceled, factoryNotCanceled , cancel);
            return entityManager.createQuery(query).getSingleResult();
        } catch (IllegalArgumentException e) {
            return 0L;
        }

    }

    public Long countZaloPay() {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<Orders> root = query.from(Orders.class);
            query.multiselect(criteriaBuilder.count(root.get(Orders_.ID)));
            Predicate orderEqual = criteriaBuilder.isTrue(root.get(Orders_.IS_PAID));
            Predicate zaloPay = criteriaBuilder.like(root.get(Orders_.TRANSACTION_ID), "%\\_%");
            query.where(orderEqual, zaloPay);
            return entityManager.createQuery(query).getSingleResult();
        } catch (IllegalArgumentException e) {
            return 0l;
        }

    }

    public FactoryRateProjection getRateFactory(String factoryId) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<FactoryRateProjection> query = criteriaBuilder.createQuery(FactoryRateProjection.class);
            Root<Rating> root = query.from(Rating.class);
            Join<Rating, DesignedProduct> designedProductJoin = root.join(Rating_.DESIGNED_PRODUCT);
            Join<DesignedProduct, PriceByFactory> priceByFactoryJoin = designedProductJoin.join(DesignedProduct_.PRICE_BY_FACTORY);
            Join<PriceByFactory, Factory> factoryFactoryJoin = priceByFactoryJoin.join(PriceByFactory_.FACTORY);

            Expression<Double> rate = criteriaBuilder.avg(root.get(Rating_.RATING_STAR));
            query.multiselect(rate.alias("rates"),
                criteriaBuilder.count(root.get(Rating_.ID)).alias("count"));
            Predicate factoryEqual = criteriaBuilder.equal(factoryFactoryJoin.get(Factory_.ID), factoryId);
            query.where(factoryEqual);
            return entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            return new FactoryRateProjection(0d, 0l);
        }
    }
}
