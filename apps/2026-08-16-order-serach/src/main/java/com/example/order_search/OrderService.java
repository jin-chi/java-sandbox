package com.example.order_search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public PageResponse<OrderResponseDto> search(OrderSearchRequestDto req, Pageable pageable) {
        if (req.isEmpty())
            return PageResponse.from(Page.empty());

        Specification<Order> spec = OrderSpecifications.orderNumberContains(req.getOrderNumber())
                .and(OrderSpecifications.statusesIn(req.getStatuses()))
                .and(OrderSpecifications.amountFrom(req.getAmountFrom()))
                .and(OrderSpecifications.amountTo(req.getAmountTo()))
                .and(OrderSpecifications.orderedFrom(req.getOrderedFrom()))
                .and(OrderSpecifications.orderedTo(req.getOrderedTo()))
                .and(OrderSpecifications.customerNameContains(req.getCustomerName()))
                .and(OrderSpecifications.customerRankEquals(req.getCustomerRank()));

        Page<Order> page = orderRepository.findAll(spec, pageable);

        if (page.isEmpty()) {
            throw new OrderNotFoundException("Order not found");
        }

        return PageResponse.from(page.map(OrderResponseDto::from));
    }
}
