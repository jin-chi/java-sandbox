package com.example.employee_search;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class EmployeeSpecifications {

    public static Specification<Employee> nameContains(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(name))
                return cb.conjunction();
            String keyword = "%" + name + "%";
            return cb.like(root.get("name"), keyword);
        };
    }

    public static Specification<Employee> departmentsIn(List<String> departments) {
        return (root, query, cb) -> !CollectionUtils.isEmpty(departments) ? root.get("department").in(departments)
                : cb.conjunction();
    }

    public static Specification<Employee> salaryFrom(Integer salaryFrom) {
        return (root, query, cb) -> salaryFrom != null ? cb.greaterThanOrEqualTo(root.get("salary"), salaryFrom)
                : cb.conjunction();
    }

    public static Specification<Employee> salaryTo(Integer salaryTo) {
        return (root, query, cb) -> salaryTo != null ? cb.lessThanOrEqualTo(root.get("salary"), salaryTo)
                : cb.conjunction();
    }

    public static Specification<Employee> topLevelOnly(Boolean topLevelOnly) {
        return (root, query, cb) -> {
            if (topLevelOnly == null || !topLevelOnly)
                return cb.conjunction();
            return cb.isNull(root.get("managerId"));
        };
    }
}
