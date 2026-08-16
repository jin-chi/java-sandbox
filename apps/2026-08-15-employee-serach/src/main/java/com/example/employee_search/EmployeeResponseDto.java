package com.example.employee_search;

public record EmployeeResponseDto(
        Long id,
        String name,
        String department,
        String position,
        Integer salary,
        Long managerId,
        String managerName
) {
        public static EmployeeResponseDto from(Employee employee) {
            return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getDepartment(),
                employee.getPosition(),
                employee.getSalary(),
                employee.getManager() != null ? employee.getManager().getId() : null,
                employee.getManager() != null ? employee.getManager().getName() : null
            );
        }
}
