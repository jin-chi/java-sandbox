package com.example.springjpa_spec;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> name == null ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<User> hasAge(Integer age) {
        return (root, query, cb) -> age == null ? null : cb.equal(root.get("age"), age);
    }
}
