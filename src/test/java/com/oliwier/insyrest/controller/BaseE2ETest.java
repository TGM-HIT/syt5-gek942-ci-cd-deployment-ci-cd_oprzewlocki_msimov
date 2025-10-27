package com.oliwier.insyrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseE2ETest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate rest;

    protected String uniqueId() {
        return UUID.randomUUID().toString().substring(0, 13);
    }

    protected String timestamp() {
        return LocalDateTime.now().toString();
    }

    protected HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    protected <T> ResponseEntity<T> postJson(String url, String json, Class<T> responseType) {
        return rest.postForEntity(url, new HttpEntity<>(json, jsonHeaders()), responseType);
    }
}
