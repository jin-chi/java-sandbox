package com.example.employee_search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponseDto> search(EmployeeRequestDto req) {
        if (req.isEmpty())
            return List.of();

        Specification<Employee> spec = EmployeeSpecifications.nameContains(req.getName())
                .and(EmployeeSpecifications.departmentsIn(req.getDepartments()))
                .and(EmployeeSpecifications.salaryFrom(req.getSalaryFrom()))
                .and(EmployeeSpecifications.salaryTo(req.getSalaryTo()))
                .and(EmployeeSpecifications.topLevelOnly(req.getTopLevelOnly()));

        List<EmployeeResponseDto> result = employeeRepository.findAll(spec)
                .stream()
                .map(EmployeeResponseDto::from)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        return result;
    }
}
