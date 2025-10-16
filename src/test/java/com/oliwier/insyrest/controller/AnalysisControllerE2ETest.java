package com.oliwier.insyrest.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnalysisControllerE2ERealTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String baseUrl;

    private static Long createdId;
    private static final String uniqueSampleId = UUID.randomUUID().toString().substring(0, 13);
    private static final String timestamp = LocalDateTime.now().toString();

    @BeforeEach
    void setupBaseUrl() {
        baseUrl = "http://localhost:" + port + "/api/analysis";
    }
    private String buildJson() {
        return """
        {
          "sample": {
              "s_id": "%s",
              "s_stamp": "%s",
              "name": "Real Sample",
              "weightNet": 10.0,
              "weightBru": 12.0,
              "weightTar": 2.0,
              "quantity": 1,
              "distance": 0.0,
              "dateCrumbled": "%s",
              "sFlags": "-----",
              "lane": 0,
              "comment": "inserted by real test",
              "dateExported": "%s"
          },
          "pol": 1.0,
          "nat": 1.0,
          "kal": 1.0,
          "an": 1.0,
          "glu": 1.0,
          "dry": 1.0,
          "dateIn": "%s",
          "dateOut": "%s",
          "weightMea": 1.00,
          "weightNrm": 1.00,
          "weightCur": 1.00,
          "weightDif": 0.00,
          "density": 1.00,
          "lane": 1,
          "comment": "E2E creation test",
          "aFlags": "-",
          "dateExported": "%s"
        }
        """.formatted(uniqueSampleId, timestamp, timestamp, timestamp, timestamp, timestamp, timestamp);
    }

    @Test
    @Order(1)
    void testCreateAnalysis() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> req = new HttpEntity<>(buildJson(), headers);
        ResponseEntity<Map> res = rest.postForEntity(baseUrl, req, Map.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        createdId = Long.valueOf(Objects.requireNonNull(res.getBody()).get("aid").toString());
        assertThat(createdId).isNotNull();
    }

    @Test
    @Order(2)
    void testGetAllAnalyses() {
        ResponseEntity<Map> res = rest.getForEntity(baseUrl + "?page=0&size=5", Map.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(3)
    void testGetAnalysisById() {
        ResponseEntity<Map> res = rest.getForEntity(baseUrl + "/" + createdId, Map.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(res.getBody()).get("aid").toString()).isEqualTo(createdId.toString());
    }

    @Test
    @Order(4)
    void testUpdateAnalysis() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String updateJson = """
            { "aid": %d, "comment": "updated real test" }
        """.formatted(createdId);

        HttpEntity<String> req = new HttpEntity<>(updateJson, headers);
        ResponseEntity<Map> res = rest.exchange(baseUrl + "/" + createdId, HttpMethod.PUT, req, Map.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().get("comment")).isEqualTo("updated real test");
    }

    @Test
    @Order(5)
    void testDeleteAnalysis() {
        rest.delete(baseUrl + "/" + createdId);

        ResponseEntity<String> res = rest.exchange(baseUrl + "/" + createdId, HttpMethod.DELETE, null, String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
