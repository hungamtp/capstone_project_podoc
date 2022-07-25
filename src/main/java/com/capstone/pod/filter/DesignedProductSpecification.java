package com.capstone.pod.filter;

import com.capstone.pod.entities.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@AllArgsConstructor
@NoArgsConstructor
public class DesignedProductSpecification implements Specification<Product> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if(criteria.getKey().equalsIgnoreCase("category")){
            Join<Product , DesignedProduct> productJoin = root.join(DesignedProduct_.PRODUCT);
           return  builder.equal(productJoin.join(Product_.CATEGORY).get(Category_.ID) , criteria.getValue());
        }
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if(criteria.getValue().equals("false")){
                return builder.isFalse(root.get(criteria.getKey()));
            }
            if(criteria.getValue().equals("true")){
                return builder.isTrue(root.get(criteria.getKey()));
            }
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
