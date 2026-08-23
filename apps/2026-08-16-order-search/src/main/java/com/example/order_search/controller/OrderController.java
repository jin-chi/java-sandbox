package com.example.order_search.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_search.dto.OrderResponseDto;
import com.example.order_search.dto.OrderSearchRequestDto;
import com.example.order_search.dto.PageResponse;
import com.example.order_search.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrderResponseDto>> getOrders(@Valid @ModelAttribute OrderSearchRequestDto req,
            Pageable pageable) {
        return ResponseEntity.ok(orderService.search(req, pageable));
    }
}
