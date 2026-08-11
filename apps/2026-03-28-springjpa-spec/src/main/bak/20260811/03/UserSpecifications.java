package com.example.springjpa_spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecifications {

    public static Specification<User> nameContains(String name) {
        return (root, query, cb) -> StringUtils.hasText(name) ? cb.like(root.get("name"), "%" + name + "%")
                : cb.conjunction();
    }

    public static Specification<User> ageEquals(Integer age) {
        return (root, query, cb) -> age == null ? cb.conjunction() : cb.equal(root.get("age"), age);
    }
}
