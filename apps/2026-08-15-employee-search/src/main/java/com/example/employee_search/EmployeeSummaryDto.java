package com.example.employee_search;

public record EmployeeSummaryDto(
        Long id,
        String name,
        String department,
        String position,
        Integer salary,
        Long managerId,
        String managerName
) {

}
