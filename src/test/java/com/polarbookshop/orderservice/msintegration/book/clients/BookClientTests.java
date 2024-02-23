package com.polarbookshop.orderservice.msintegration.book.clients;

import com.polarbookshop.orderservice.msintegration.book.dtos.Book;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

class BookClientTests {
    private MockWebServer mockWebServer;
    private BookClient bookClient;
    @BeforeEach
    void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start(); //Starts the mock server before running a test case
        var webClient = WebClient.builder() //Uses the mock server URL as the base URL for WebClient
                                 .baseUrl(mockWebServer.url("/").uri().toString())
                                 .build();
        this.bookClient = new BookClient(webClient);
    }
    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown(); //Shuts the mock server down after completing a test case
    }

    @Test
    void whenBookExistsThenReturnBook() {
        var bookIsbn = "1234567890";
        var mockResponse = new MockResponse() //Defines the response to be returned by the mock server
                                            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                            .setBody("""
                                                     {
                                                     "isbn": %s,
                                                     "title": "Title",
                                                     "author": "Author",
                                                     "price": 9.90,
                                                     "publisher": "Polarsophia"
                                                     }
                                            """.formatted(bookIsbn));

        mockWebServer.enqueue(mockResponse);//Adds a mock response to the queue processed by the mock server
        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);
        StepVerifier.create(book)//Initializes a StepVerifier object with the object returned by BookClient
                    .expectNextMatches(
                        b -> b.isbn().equals(bookIsbn)) //Asserts that the Book returned has the ISBN requested
                    .verifyComplete(); //Verifies that the reactive stream completed successfully
    }
}