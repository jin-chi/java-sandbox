package com.example.springjpa_spec;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public class UserSpecifications {

    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> (name == null || name.isEmpty()) ? cb.conjunction()
                : cb.equal(root.get("name"), name);
    }

    public static Specification<User> hasAge(Integer age) {
        return (root, query, cb) -> age == null ? cb.conjunction() : cb.equal(root.get("age"), age);
    }

    public static Specification<User> search(String name, Integer age) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.equal(root.get("name"), name));
            }
            if (age != null) {
                predicates.add(cb.equal(root.get("age"), age));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
