package com.polarbookshop.orderservice.msintegration.book.clients;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.reactive.function.client.WebClient;

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
}