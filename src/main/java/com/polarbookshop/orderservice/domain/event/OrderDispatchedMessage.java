package com.polarbookshop.orderservice.domain.event;

public record OrderDispatchedMessage (
        Long orderId
){}

