package com.example.employee_search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @EntityGraph(attributePaths = { "manager" })
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    @Query("""
            SELECT new com.example.employee_search.EmployeeSummaryDto(
                e.id, e.name, e.department, e.position, e.salary, m.id, m.name
            )
            FROM Employee e
            LEFT JOIN e.manager m
            """)
    List<EmployeeSummaryDto> findAllSummary();
}
