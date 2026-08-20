package com.example.order_search;

import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        String orderNumber,
        OrderStatus status,
        Integer totalAmount,
        LocalDateTime orderedAt,
        Long customerId,
        String customerName,
        CustomerRank customerRank
) {
        public static OrderResponseDto from(Order order) {
            return new OrderResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getOrderedAt(),
                order.getCustomer() != null ? order.getCustomer().getId() : null,
                order.getCustomer() != null ? order.getCustomer().getName() : null,
                order.getCustomer() != null ? order.getCustomer().getRank() : null
            );
        }
}
