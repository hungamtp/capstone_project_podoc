package com.capstone.pod.repositories.impl;

import com.capstone.pod.entities.*;
import com.capstone.pod.repositories.DesignedProductRepositoryCustom;
import com.capstone.pod.repositories.impl.projection.P;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DesignedProductRepositoryCustomImpl implements DesignedProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;


    public List<DesignedProduct> get4HighestRateDesignedProduct() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignedProduct> query = criteriaBuilder.createQuery(DesignedProduct.class);
        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        root.join(DesignedProduct_.DESIGN_COLORS, JoinType.LEFT);
        Join<DesignedProduct, OrderDetail> orderDetailJoin = root.join(DesignedProduct_.ORDER_DETAILS, JoinType.INNER);
        Join<DesignedProduct, Rating> ratingJoin = root.join(DesignedProduct_.RATINGS, JoinType.LEFT);
        Join<DesignedProduct, Product> productJoin = root.join(DesignedProduct_.PRODUCT, JoinType.LEFT);
        Join<DesignedProduct, PriceByFactory> priceByFactoryJoin = root.join(DesignedProduct_.PRICE_BY_FACTORY, JoinType.LEFT);
        Join<PriceByFactory, Factory> factoryJoin = priceByFactoryJoin.join(PriceByFactory_.FACTORY, JoinType.LEFT);
        Join<OrderDetail, Orders> ordersJoin = orderDetailJoin.join(OrderDetail_.ORDERS, JoinType.LEFT);
        root.join(DesignedProduct_.ORDER_DETAILS, JoinType.INNER);
        root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
        query.select(root);
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.avg(ratingJoin.get(Rating_.RATING_STAR))));
        Predicate publishTrue = criteriaBuilder.isTrue(root.get(DesignedProduct_.PUBLISH));
        Predicate productIsDelete = criteriaBuilder.isFalse(productJoin.get(Product_.IS_DELETED));
        Predicate isPublicTrue = criteriaBuilder.isTrue(productJoin.get(Product_.IS_PUBLIC));
        Predicate isCollaborating = criteriaBuilder.isTrue(factoryJoin.get(Factory_.IS_COLLABORATING));
        Predicate isPaid = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
        Predicate isCancel = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
        Predicate orderDetailIsCancel = criteriaBuilder.isFalse(orderDetailJoin.get(OrderDetail_.CANCELED));
        query.orderBy(orderList).where(publishTrue, isPublicTrue, productIsDelete, isCollaborating, isCancel, isPaid, orderDetailIsCancel);
        return entityManager.createQuery(query).setMaxResults(4).getResultList();
    }

    @Override
    public List<DesignedProduct> get4HighestRateDesignedProductByProductId(String productId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignedProduct> query = criteriaBuilder.createQuery(DesignedProduct.class);

        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        root.join(DesignedProduct_.DESIGN_COLORS, JoinType.LEFT);
        Join<DesignedProduct, Rating> ratingJoin = root.join(DesignedProduct_.RATINGS, JoinType.LEFT);
        Join<DesignedProduct, OrderDetail> orderDetailJoin = root.join(DesignedProduct_.ORDER_DETAILS, JoinType.INNER);
        root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        Join<DesignedProduct, PriceByFactory> priceByFactoryJoin = root.join(DesignedProduct_.PRICE_BY_FACTORY, JoinType.LEFT);
        Join<PriceByFactory, Factory> factoryJoin = priceByFactoryJoin.join(PriceByFactory_.FACTORY, JoinType.LEFT);
        Join<DesignedProduct, Product> productJoin = root.join(DesignedProduct_.PRODUCT, JoinType.LEFT);
        Join<OrderDetail, Orders> ordersJoin = orderDetailJoin.join(OrderDetail_.ORDERS, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
        query.select(root);
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.avg(ratingJoin.get(Rating_.RATING_STAR))));
        Predicate publishTrue = criteriaBuilder.isTrue(root.get(DesignedProduct_.PUBLISH));
        Predicate productIdEqual = criteriaBuilder.equal(productJoin.get(Product_.ID), productId);
        Predicate productIsDelete = criteriaBuilder.isFalse(productJoin.get(Product_.IS_DELETED));
        Predicate isPublicTrue = criteriaBuilder.isTrue(productJoin.get(Product_.IS_PUBLIC));
        Predicate isCollaborating = criteriaBuilder.isTrue(factoryJoin.get(Factory_.IS_COLLABORATING));
        Predicate isPaid = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
        Predicate isCancel = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
        Predicate orderDetailIsCancel = criteriaBuilder.isFalse(orderDetailJoin.get(OrderDetail_.CANCELED));
        query.orderBy(orderList).where(publishTrue, productIdEqual, productIsDelete, isPublicTrue, isCollaborating, isCancel, isPaid, orderDetailIsCancel);
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
        root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        Join<DesignedProduct, OrderDetail> orderDetailJoin = root.join(DesignedProduct_.ORDER_DETAILS, JoinType.LEFT);
        Join<DesignedProduct, PriceByFactory> priceByFactoryJoin = root.join(DesignedProduct_.PRICE_BY_FACTORY, JoinType.LEFT);
        Join<OrderDetail, Orders> ordersJoin = orderDetailJoin.join(OrderDetail_.ORDERS, JoinType.LEFT);
        Join<PriceByFactory, Factory> factoryJoin = priceByFactoryJoin.join(PriceByFactory_.FACTORY, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
        query.select(root);
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.sum(orderDetailJoin.get(OrderDetail_.QUANTITY))));
        Predicate productIsDelete = criteriaBuilder.isFalse(productJoin.get(Product_.IS_DELETED));
        Predicate publish = criteriaBuilder.isTrue(root.get(DesignedProduct_.PUBLISH));
        Predicate isPublicTrue = criteriaBuilder.isTrue(productJoin.get(Product_.IS_PUBLIC));
        Predicate isCollaborating = criteriaBuilder.isTrue(factoryJoin.get(Factory_.IS_COLLABORATING));
        Predicate isPaid = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
        Predicate isCancel = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
        Predicate orderDetailIsCancel = criteriaBuilder.isFalse(orderDetailJoin.get(OrderDetail_.CANCELED));
        query.orderBy(orderList).where(productIsDelete, isPublicTrue, publish, isCollaborating, isCancel, isPaid, orderDetailIsCancel);
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
        Join<OrderDetail, Orders> ordersJoin = orderDetailJoin.join(OrderDetail_.ORDERS, JoinType.LEFT);
        Join<DesignedProduct, PriceByFactory> priceByFactoryJoin = root.join(DesignedProduct_.PRICE_BY_FACTORY, JoinType.LEFT);
        Join<PriceByFactory, Factory> factoryJoin = priceByFactoryJoin.join(PriceByFactory_.FACTORY, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID));
        query.select(root);
        List<Order> orderList = new ArrayList();
        Predicate productIdEqual = criteriaBuilder.equal(productJoin.get(Product_.ID), productId);
        Predicate publishTrue = criteriaBuilder.isTrue(root.get(DesignedProduct_.PUBLISH));
        orderList.add(criteriaBuilder.desc(criteriaBuilder.sum(orderDetailJoin.get(OrderDetail_.QUANTITY))));
        Predicate productIsDelete = criteriaBuilder.isFalse(productJoin.get(Product_.IS_DELETED));
        Predicate isPublicTrue = criteriaBuilder.isTrue(productJoin.get(Product_.IS_PUBLIC));
        Predicate isCollaborating = criteriaBuilder.isTrue(factoryJoin.get(Factory_.IS_COLLABORATING));
        Predicate isPaid = criteriaBuilder.isTrue(ordersJoin.get(Orders_.IS_PAID));
        Predicate isCancel = criteriaBuilder.isFalse(ordersJoin.get(Orders_.CANCELED));
        Predicate orderDetailIsCancel = criteriaBuilder.isFalse(orderDetailJoin.get(OrderDetail_.CANCELED));
        query.orderBy(orderList).where(productIdEqual, publishTrue, productIsDelete, isPublicTrue, isCollaborating, isCancel, isPaid, orderDetailIsCancel);
        return entityManager.createQuery(query).setMaxResults(4).getResultList();
    }

    @Override
    public List<P> search(String productId) throws InterruptedException {
        FullTextEntityManager fullTextEntityManager
            = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer()
            .startAndWait();
        int indexSize = fullTextEntityManager.getSearchFactory()
            .getStatistics()
            .getNumberOfIndexedEntities(Product.class.getName());
        System.out.println(indexSize);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
            .buildQueryBuilder()
            .forEntity(Product.class)
            .get();
        org.apache.lucene.search.Query query = queryBuilder
            .keyword()
            .onField(Product_.DESCRIPTION)
            .matching(productId)
            .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery
            = fullTextEntityManager.createFullTextQuery(query, Product.class);

        List<Product> results = jpaQuery.getResultList();
        List<P> re = results.stream().map(p -> new P(p.getId() , p.getName() , p.getDescription())).collect(Collectors.toList());
        return re;
    }


}


