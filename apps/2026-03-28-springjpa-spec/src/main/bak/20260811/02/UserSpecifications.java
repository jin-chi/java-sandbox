package com.example.springjpa_spec;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> nameEquals(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }

    public static Specification<User> ageEquals(Integer age) {
        return (root, query, cb) -> cb.equal(root.get("age"), age);
    }
}
