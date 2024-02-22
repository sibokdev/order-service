package com.polarbookshop.orderservice.msintegration.book.dtos;

public record Book(
        String isbn,
        String title,
        String author,
        Double price
){}
