package com.example.springjpa_spec;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasName(String name) {
        return (root, query, criteriaBuilder) -> (name == null || name.isEmpty()) ? null
                : criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<User> hasAge(Integer age) {
        return (root, query, criteriaBuilder) -> (age == null) ? null : criteriaBuilder.equal(root.get("age"), age);
    }
}
