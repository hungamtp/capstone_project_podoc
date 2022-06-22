package com.capstone.pod.filter;

import com.capstone.pod.entities.DesignedProduct;
import com.capstone.pod.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DesignedProductSpecificationBuilder {
    private final List<SearchCriteria> params;

    public DesignedProductSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public DesignedProductSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<DesignedProduct> build() {
        if (params.size() == 0) {
            return null;
        }
        List<Specification> specs = params.stream()
                .map(ProductSpecification::new)
                .collect(Collectors.toList());
        Specification result = specs.get(0);
        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}
