package com.oliwier.insyrest.controller.analysis;

import com.oliwier.insyrest.controller.AnalysisIntegrationUtils;
import com.oliwier.insyrest.controller.BaseIntegrationTest;
import com.oliwier.insyrest.controller.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class AnalysisUpdateIntegrationTest extends BaseIntegrationTest {

    @Test
    void updateExistingAnalysis_shouldReturn200AndPersistChanges() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(sampleJson, jsonHeaders()), Map.class);

        String createJson = AnalysisIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(createJson, jsonHeaders()),
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long aId = ((Number) created.getBody().get("aId")).longValue();

        String updateJson = createJson.replace("\"comment\": \"E2E Analysis Test\"", "\"comment\": \"Updated E2E Test\"");
        ResponseEntity<Map> updated = rest.exchange(baseUrl("/api/analysis/" + aId),
                HttpMethod.PUT, new HttpEntity<>(updateJson, jsonHeaders()), Map.class);

        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Map> fetched = rest.getForEntity(baseUrl("/api/analysis/" + aId), Map.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((String) fetched.getBody().get("comment")).isEqualTo("Updated E2E Test");
    }

    @Test
    void updateNonexistentAnalysis_shouldReturn404() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String fakeUpdate = AnalysisIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/analysis/99999999"),
                HttpMethod.PUT,
                new HttpEntity<>(fakeUpdate, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateWithInvalidJson_shouldReturnBadRequest() {
        String invalidJson = "{ \"pol\": 12.5, BAD }";
        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/analysis/1"),
                HttpMethod.PUT,
                new HttpEntity<>(invalidJson, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void partialUpdateWithPatch_shouldSucceed() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(sampleJson, jsonHeaders()), Map.class);

        String createJson = AnalysisIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(createJson, jsonHeaders()),
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long aId = ((Number) created.getBody().get("aId")).longValue();

        String patchJson = """
            {
              "sId": "%s",
              "sStamp": "%s",
              "comment": "Patched comment",
              "density": 5.5
            }
            """.formatted(sId, sStamp);

        ResponseEntity<Map> patched = rest.exchange(
                baseUrl("/api/analysis/" + aId),
                HttpMethod.PATCH,
                new HttpEntity<>(patchJson, jsonHeaders()),
                Map.class
        );

        assertThat(patched.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Map> fetched = rest.getForEntity(baseUrl("/api/analysis/" + aId), Map.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((Number) fetched.getBody().get("density")).doubleValue()).isEqualTo(5.5);
    }

    @Test
    void updateAnalysisWithExtremeValues_shouldStillWork() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String sampleJson = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(sampleJson, jsonHeaders()), Map.class);

        String createJson = AnalysisIntegrationUtils.buildValidJson(sId, sStamp, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(createJson, jsonHeaders()),
                Map.class
        );

        Long aId = ((Number) created.getBody().get("aId")).longValue();

        String extremeUpdate = createJson
                .replace("\"weightMea\": 1.0", "\"weightMea\": 999999.99")
                .replace("\"weightNrm\": 1.0", "\"weightNrm\": 888888.88")
                .replace("\"comment\": \"E2E Analysis Test\"", "\"comment\": \"Extreme update test\"");

        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/analysis/" + aId),
                HttpMethod.PUT,
                new HttpEntity<>(extremeUpdate, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Map> fetched = rest.getForEntity(baseUrl("/api/analysis/" + aId), Map.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody().get("comment")).isEqualTo("Extreme update test");
    }
}
