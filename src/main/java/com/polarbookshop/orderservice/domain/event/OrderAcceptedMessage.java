package com.polarbookshop.orderservice.domain.event;

public record OrderAcceptedMessage (
        Long orderId
){}

