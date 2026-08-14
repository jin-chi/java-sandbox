package com.example.product_search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductSpecifications {

    // 商品名の部分一致: nameContains(String)
    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name))
                return cb.conjunction();
            String n = "%" + name + "%";
            return cb.like(root.get("name"), n);
        };
    }

    // カテゴリの完全一致: categoryEquals(String)
    public static Specification<Product> categoryEquals(String category) {
        return (root, query, cb) -> StringUtils.hasText(category) ? cb.equal(root.get("category"), category)
                : cb.conjunction();
    }

    // 価格がこの値以上: priceFrom(Integer)
    public static Specification<Product> priceFrom(Integer priceFrom) {
        return (root, query, cb) -> priceFrom != null ? cb.greaterThanOrEqualTo(root.get("price"), priceFrom)
                : cb.conjunction();
    }

    // 価格がこの値以下: priceTo(Integer)
    public static Specification<Product> priceTo(Integer priceTo) {
        return (root, query, cb) -> priceTo != null ? cb.lessThanOrEqualTo(root.get("price"), priceTo)
                : cb.conjunction();
    }

    // trueの時在庫が1以上: inStock(boolean)
    public static Specification<Product> inStock(Boolean inStock) {
        return (root, query, cb) -> {
            if (inStock == null || !inStock)
                return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("stock"), 1);
        };
    }
}
