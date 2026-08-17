package com.example.employee_search;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public ResponseEntity<PageResponse<EmployeeResponseDto>> getEmployees(@Valid @ModelAttribute EmployeeRequestDto req, Pageable pageable) {
        return ResponseEntity.ok(employeeService.search(req, pageable));
    }

    // 射影の実験用
    @GetMapping("/summary")
    public ResponseEntity<List<EmployeeSummaryDto>> getSummary() {
        return ResponseEntity.ok(employeeRepository.findAllSummary());
    }
}
