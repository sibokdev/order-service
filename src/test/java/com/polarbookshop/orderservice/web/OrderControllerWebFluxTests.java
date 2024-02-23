package com.polarbookshop.orderservice.web;

import com.polarbookshop.orderservice.controllers.OrderController;
import com.polarbookshop.orderservice.controllers.dtos.OrderRequest;
import com.polarbookshop.orderservice.domain.Order;
import com.polarbookshop.orderservice.domain.OrderStatus;
import com.polarbookshop.orderservice.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
@WebFluxTest(OrderController.class) // Identifies a test class that focuses on Spring WebFlux components, targeting OrderController
class OrderControllerWebFluxTests {
    @Autowired
    private WebTestClient webClient; //A WebClient variant with extra features to make testing RESTful services easier
    @MockBean
    private OrderService orderService; //Adds a mock of OrderService to the Spring application context
    @Test
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());

        given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .willReturn(Mono.just(expectedOrder)); //Defines the expected behavior for the OrderService mock bean
        webClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful() //Expects the order is created successfully
                .expectBody(Order.class).value(actualOrder -> {
                    assertThat(actualOrder).isNotNull();
                    assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });
    }
}