package com.capstone.pod.repositories.impl;

import com.capstone.pod.dto.designedProduct.DesignedProductDetailDTO;
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


    public List<DesignedProductDetailDTO> get4HighestRateDesignedProduct() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignedProductDetailDTO> query = criteriaBuilder.createQuery(DesignedProductDetailDTO.class);
        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        Join<DesignedProduct, Rating> ratingJoin = root.join(DesignedProduct_.RATINGS, JoinType.LEFT);
        Join<DesignedProduct, ImagePreview> imagePreviewJoin = root.join(DesignedProduct_.IMAGE_PREVIEWS, JoinType.LEFT);
        Join<DesignedProduct, DesignedProductTag> designedProductTagJoin = root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        Join<Tag, DesignedProductTag> tagJoin = designedProductTagJoin.join(DesignedProductTag_.TAG, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID) , tagJoin.get(Tag_.ID) , imagePreviewJoin.get(ImagePreview_.ID));
        query.multiselect(
            root.get(DesignedProduct_.ID),
            root.get(DesignedProduct_.NAME),
            imagePreviewJoin.get(ImagePreview_.IMAGE),
            root.get(DesignedProduct_.DESIGNED_PRICE),
            criteriaBuilder.avg(ratingJoin.get(Rating_.RATING_STAR)),
            tagJoin.get(Tag_.NAME)
        );
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.avg(ratingJoin.get(Rating_.RATING_STAR))));
        query.orderBy(orderList);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<DesignedProductDetailDTO> get4HighestRateDesignedProductByProductId(int productId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignedProductDetailDTO> query = criteriaBuilder.createQuery(DesignedProductDetailDTO.class);
        Root<DesignedProduct> root = query.from(DesignedProduct.class);
        Join<DesignedProduct, Rating> ratingJoin = root.join(DesignedProduct_.RATINGS, JoinType.LEFT);
        Join<DesignedProduct, ImagePreview> imagePreviewJoin = root.join(DesignedProduct_.IMAGE_PREVIEWS, JoinType.LEFT);
        Join<DesignedProduct, Product> productJoin = root.join(DesignedProduct_.PRODUCT, JoinType.LEFT);
        Join<DesignedProduct, DesignedProductTag> designedProductTagJoin = root.join(DesignedProduct_.DESIGNED_PRODUCT_TAGS, JoinType.LEFT);
        Join<Tag, DesignedProductTag> tagJoin = designedProductTagJoin.join(DesignedProductTag_.TAG, JoinType.LEFT);
        query.groupBy(root.get(DesignedProduct_.ID) , tagJoin.get(Tag_.ID) , imagePreviewJoin.get(ImagePreview_.ID));
        query.multiselect(
            root.get(DesignedProduct_.ID),
            root.get(DesignedProduct_.NAME),
            imagePreviewJoin.get(ImagePreview_.IMAGE),
            root.get(DesignedProduct_.DESIGNED_PRICE),
            criteriaBuilder.avg(ratingJoin.get(Rating_.RATING_STAR)),
            tagJoin.get(Tag_.NAME)
        );
        Predicate productIdEqual = criteriaBuilder.equal(productJoin.get(Product_.ID) , productId);
        List<Order> orderList = new ArrayList();
        orderList.add(criteriaBuilder.desc(criteriaBuilder.avg(ratingJoin.get(Rating_.RATING_STAR))));
        query.orderBy(orderList).where(productIdEqual);
        return entityManager.createQuery(query).getResultList();
    }

}


