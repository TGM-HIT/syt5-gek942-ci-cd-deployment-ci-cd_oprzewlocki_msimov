package com.oliwier.insyrest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AnalysisCreateE2ETest extends BaseE2ETest {

    @Test
    void createValidAnalysis_shouldReturn200AndId() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = buildValidJson(uniqueId());
        HttpEntity<String> req = new HttpEntity<>(json, headers);

        ResponseEntity<Map> res = rest.postForEntity(baseUrl(), req, Map.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("aid");
    }

    @Test
    void createWithMissingField_shouldFail() {
        String json = buildValidJson(uniqueId()).replace("\"name\": \"Valid Sample\",", "");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> res = rest.postForEntity(baseUrl(), new HttpEntity<>(json, headers), String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createWithDuplicateSampleId_shouldReturnConflictOrBadRequest() {
        String id = uniqueId();
        String json = buildValidJson(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        rest.postForEntity(baseUrl(), new HttpEntity<>(json, headers), Map.class);
        ResponseEntity<String> second = rest.postForEntity(baseUrl(), new HttpEntity<>(json, headers), String.class);

        assertThat(second.getStatusCode()).isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST);
    }

    @Test
    void createWithExtremeWeights_shouldSucceed() {
        String json = buildValidJson(uniqueId()).replace("\"weightNet\": 10.0", "\"weightNet\": 999999.9");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> res = rest.postForEntity(baseUrl(), new HttpEntity<>(json, headers), Map.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createInvalidJson_shouldReturnBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> req = new HttpEntity<>("{ bad json", headers);

        ResponseEntity<String> res = rest.postForEntity(baseUrl(), req, String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}