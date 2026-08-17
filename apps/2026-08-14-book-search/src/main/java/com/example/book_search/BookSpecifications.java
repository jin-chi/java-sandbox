package com.example.book_search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BookSpecifications {

    public static Specification<Book> keywordContains(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword))
                return cb.conjunction();
            String k = "%" + keyword + "%";
            return cb.or(
                    cb.like(root.get("title"), k),
                    cb.like(root.get("author"), k));
        };
    }

    public static Specification<Book> publisherEquals(String publisher) {
        return (root, query, cb) -> StringUtils.hasText(publisher) ? cb.equal(root.get("publisher"), publisher)
                : cb.conjunction();
    }

    public static Specification<Book> priceMax(Integer priceMax) {
        return (root, query, cb) -> priceMax != null ? cb.lessThanOrEqualTo(root.get("price"), priceMax)
                : cb.conjunction();
    }

    public static Specification<Book> yearFrom(Integer yearFrom) {
        return (root, query, cb) -> yearFrom != null ? cb.greaterThanOrEqualTo(root.get("publishedYear"), yearFrom)
                : cb.conjunction();
    }

    public static Specification<Book> yearTo(Integer yearTo) {
        return (root, query, cb) -> yearTo != null ? cb.lessThanOrEqualTo(root.get("publishedYear"), yearTo)
                : cb.conjunction();
    }
}
