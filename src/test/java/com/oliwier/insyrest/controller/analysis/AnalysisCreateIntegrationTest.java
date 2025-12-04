package com.oliwier.insyrest.controller.analysis;

import com.oliwier.insyrest.controller.AnalysisIntegrationUtils;
import com.oliwier.insyrest.controller.BaseIntegrationTest;
import com.oliwier.insyrest.controller.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class AnalysisCreateIntegrationTest extends BaseIntegrationTest {

    @Test
    void createValidAnalysis_shouldReturn201AndId() {
        String json = AnalysisIntegrationUtils.buildValidJson(null, null, timestamp());

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).containsKey("aId");
    }

    @Test
    void createAnalysisWithEmptySample_shouldFail() {
        String json = AnalysisIntegrationUtils.buildValidJson(null, null, timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createAnalysisWithExistingSample_shouldSucceed() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        ResponseEntity<Map> sampleRes = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(sampleJson, jsonHeaders()),
                Map.class
        );

        assertThat(sampleRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String analysisJson = AnalysisIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(analysisJson, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).containsKey("aId");
    }

    @Test
    void createAnalysisWithExtremeWeights_shouldSucceed() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(sampleJson, jsonHeaders()), Map.class);

        String json = AnalysisIntegrationUtils.buildValidJson(sId, sStamp, timestamp())
                .replace("\"weightMea\": 1.0", "\"weightMea\": 999999.99")
                .replace("\"weightNrm\": 1.0", "\"weightNrm\": 999999.99")
                .replace("\"weightCur\": 1.0", "\"weightCur\": 999999.99");

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).containsKey("aId");
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
