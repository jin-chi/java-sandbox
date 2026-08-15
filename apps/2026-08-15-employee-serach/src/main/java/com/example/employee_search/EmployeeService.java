package com.example.employee_search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public PageResponse<EmployeeResponseDto> search(EmployeeRequestDto req, Pageable pageable) {
        if (req.isEmpty())
            return PageResponse.from(Page.empty(pageable));

        Specification<Employee> spec = EmployeeSpecifications.nameContains(req.getName())
                .and(EmployeeSpecifications.departmentsIn(req.getDepartments()))
                .and(EmployeeSpecifications.salaryFrom(req.getSalaryFrom()))
                .and(EmployeeSpecifications.salaryTo(req.getSalaryTo()))
                .and(EmployeeSpecifications.topLevelOnly(req.getTopLevelOnly()));

        Page<Employee> page = employeeRepository.findAll(spec, pageable);

        if (page.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        return PageResponse.from(page.map(EmployeeResponseDto::from));
    }
}
