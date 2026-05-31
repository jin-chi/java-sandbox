package com.example.springjpa_spec;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasName(String name) {
        if (name == null || name.isEmpty())  return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<User> hasAge(Integer age) {
        if (age == null)  return null;
        return (root, query, criteriaBulder) -> criteriaBulder.equal(root.get("age"), age);
    }
}
