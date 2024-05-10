package com.polarbookshop.orderservice.controllers;

import com.polarbookshop.orderservice.controllers.dtos.OrderRequest;
import com.polarbookshop.orderservice.domain.Order;
import com.polarbookshop.orderservice.services.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class OrderController { private static final Logger log = LoggerFactory.getLogger(OrderController.class);
   private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public Flux<Order> getAllOrders() {
        log.info("Fetching the list of ORDERS received on shop");
        return orderService.getAllOrders();
    }
    @PostMapping
    public Mono<Order> submitOrder(
            @RequestBody @Valid OrderRequest orderRequest
    ) {
        return orderService.submitOrder(
                orderRequest.isbn(), orderRequest.quantity()
        );
    }
}