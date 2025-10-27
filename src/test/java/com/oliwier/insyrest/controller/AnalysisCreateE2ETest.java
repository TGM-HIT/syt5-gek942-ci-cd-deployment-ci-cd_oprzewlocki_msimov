package com.oliwier.insyrest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class AnalysisCreateE2ETest extends BaseE2ETest {

    @Test
    void createValidAnalysis_shouldReturn200AndId() {
        String json = AnalysisE2EUtils.buildValidJson(null, null, timestamp());

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("aid");
    }

    @Test
    void createAnalysisWithEmptySample_shouldSucceed() {
        String json = AnalysisE2EUtils.buildValidJson(null, null, timestamp());

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("aid");
    }

    @Test
    void createAnalysisWithExistingSample_shouldSucceed() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleE2EUtils.buildValidJson(sId, sStamp, timestamp());

        ResponseEntity<Map> sampleRes = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(sampleJson, jsonHeaders()),
                Map.class
        );

        assertThat(sampleRes.getStatusCode()).isEqualTo(HttpStatus.OK);

        String analysisJson = AnalysisE2EUtils.buildValidJson(sId, sStamp, timestamp());

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(analysisJson, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("aid");
    }

    @Test
    void createAnalysisWithExtremeWeights_shouldSucceed() {
        String json = AnalysisE2EUtils.buildValidJson(null, null, timestamp())
                .replace("\"weightMea\": 1.0", "\"weightMea\": 999999.99")
                .replace("\"weightNrm\": 1.0", "\"weightNrm\": 999999.99")
                .replace("\"weightCur\": 1.0", "\"weightCur\": 999999.99");

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("aid");
    }

    @Test
    void createInvalidJson_shouldReturnBadRequest() {
        String invalid = "{ bad json";

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(invalid, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
