package com.example.springjpa_spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecifications {

    public static Specification<User> nameContains(String name) {
        return (root, query, cb) -> StringUtils.hasText(name) ? cb.like(root.get("name"), "%" + name + "%")
                : cb.conjunction();
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) -> StringUtils.hasText(email) ? cb.like(root.get("email"), "%" + email + "%")
                : cb.conjunction();
    }

    public static Specification<User> ageEquals(Integer age) {
        return (root, query, cb) -> age != null ? cb.equal(root.get("age"), age) : cb.conjunction();
    }

    public static Specification<User> keywordContains(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword))
                return cb.conjunction();
            String kw = "%" + keyword + "%";
            return cb.or(
                    cb.like(root.get("name"), kw),
                    cb.like(root.get("email"), kw));
        };
    }

    public static Specification<User> ageFrom(Integer ageFrom) {
        return (root, query, cb) -> ageFrom != null ? cb.greaterThanOrEqualTo(root.get("age"), ageFrom)
                : cb.conjunction();
    }

    public static Specification<User> ageTo(Integer ageTo) {
        return (root, query, cb) -> ageTo != null ? cb.lessThanOrEqualTo(root.get("age"), ageTo) : cb.conjunction();
    }
}
