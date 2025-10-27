package com.oliwier.insyrest.controller;


import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class AnalysisReadE2ETest extends BaseE2ETest {

    @Test
    void getAllAnalyses_shouldReturnList() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/analysis"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

    @Test
    void getExistingAnalysisById_shouldReturn200AndCorrectData() {
        String json = AnalysisE2EUtils.buildValidJson(null, null, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long aid = ((Number) created.getBody().get("aid")).longValue();

        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/analysis/" + aid),
                Map.class
        );

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((Number) fetched.getBody().get("aid")).longValue()).isEqualTo(aid);
    }

    @Test
    void getNonexistentAnalysis_shouldReturn404() {
        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/analysis/99999999"),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAnalysisBySampleReference_shouldReturnList() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleE2EUtils.buildValidJson(sId, sStamp, timestamp());
        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(sampleJson, jsonHeaders()), Map.class);

        String analysisJson = AnalysisE2EUtils.buildValidJson(sId, sStamp, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(analysisJson, jsonHeaders()),
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/analysis?s_id=" + sId + "&s_stamp=" + sStamp),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

    @Test
    void getAnalysesPagination_shouldWork() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/analysis?page=0&size=5"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKeys("content", "totalElements");
    }
}
