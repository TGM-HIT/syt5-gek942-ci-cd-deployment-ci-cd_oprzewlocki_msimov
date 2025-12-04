package com.oliwier.insyrest.controller.analysis;

import com.oliwier.insyrest.controller.AnalysisIntegrationUtils;
import com.oliwier.insyrest.controller.BaseIntegrationTest;
import com.oliwier.insyrest.controller.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AnalysisDeleteIntegrationTest extends BaseIntegrationTest {

    @Test
    void deleteExistingAnalysis_shouldReturn204() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(sampleJson, jsonHeaders()), Map.class);

        String json = AnalysisIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long aId = ((Number) created.getBody().get("aId")).longValue();

        ResponseEntity<Void> deleted = rest.exchange(
                baseUrl("/api/analysis/" + aId),
                HttpMethod.DELETE,
                new HttpEntity<>(jsonHeaders()),
                Void.class
        );

        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getAfterDelete = rest.getForEntity(
                baseUrl("/api/analysis/" + aId),
                String.class
        );
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteNonexistentAnalysis_shouldReturn404() {
        ResponseEntity<Void> res = rest.exchange(
                baseUrl("/api/analysis/99999999"),
                HttpMethod.DELETE,
                new HttpEntity<>(jsonHeaders()),
                Void.class
        );
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteSameAnalysisTwice_shouldReturn204Then404() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(sampleJson, jsonHeaders()), Map.class);

        String json = AnalysisIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );
        Long aId = ((Number) created.getBody().get("aId")).longValue();

        ResponseEntity<Void> first = rest.exchange(
                baseUrl("/api/analysis/" + aId),
                HttpMethod.DELETE,
                new HttpEntity<>(jsonHeaders()),
                Void.class
        );
        assertThat(first.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Void> second = rest.exchange(
                baseUrl("/api/analysis/" + aId),
                HttpMethod.DELETE,
                new HttpEntity<>(jsonHeaders()),
                Void.class
        );
        assertThat(second.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteAnalysisWithExistingSampleReference_shouldStillWork() {
        String sid = uniqueId();
        String stamp = timestamp();
        String sampleJson = SampleIntegrationUtils.buildValidJson(sid, stamp, timestamp());
        ResponseEntity<Map> sampleRes = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(sampleJson, jsonHeaders()),
                Map.class
        );
        assertThat(sampleRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String analysisJson = AnalysisIntegrationUtils.buildValidJson(sid, stamp, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(analysisJson, jsonHeaders()),
                Map.class
        );
        Long aId = ((Number) created.getBody().get("aId")).longValue();

        ResponseEntity<Void> deleted = rest.exchange(
                baseUrl("/api/analysis/" + aId),
                HttpMethod.DELETE,
                new HttpEntity<>(jsonHeaders()),
                Void.class
        );
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteAnalysisWithoutId_shouldReturn405() {
        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/analysis"),
                HttpMethod.DELETE,
                new HttpEntity<>(jsonHeaders()),
                String.class
        );
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
