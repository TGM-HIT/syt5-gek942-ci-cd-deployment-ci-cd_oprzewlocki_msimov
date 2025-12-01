package com.oliwier.insyrest.controller.analysis;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.SampleE2EUtils;
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
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleE2EUtils.buildValidJson(sId, sStamp, timestamp());
        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(sampleJson, jsonHeaders()), Map.class);

        String json = AnalysisE2EUtils.buildValidJson(sId, sStamp, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long aId = ((Number) created.getBody().get("aId")).longValue();

        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/analysis/" + aId),
                Map.class
        );

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((Number) fetched.getBody().get("aId")).longValue()).isEqualTo(aId);
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

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/analysis?filter[sId]=" + sId),
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
