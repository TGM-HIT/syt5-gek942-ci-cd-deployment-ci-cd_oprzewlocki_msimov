package com.oliwier.insyrest.controller.analysis;


import com.oliwier.insyrest.controller.BaseE2ETest;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class AnalysisUpdateE2ETest extends BaseE2ETest {

    @Test
    void updateExistingAnalysis_shouldReturn200AndPersistChanges() {
        String createJson = AnalysisE2EUtils.buildValidJson(null, null, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(createJson, jsonHeaders()),
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long aid = ((Number) created.getBody().get("aid")).longValue();

        String updateJson = createJson.replace("\"comment\": \"E2E Analysis Test\"", "\"comment\": \"Updated E2E Test\"");
        rest.exchange(baseUrl("/api/analysis/" + aid),
                HttpMethod.PUT, new HttpEntity<>(updateJson, jsonHeaders()), Void.class);

        ResponseEntity<Map> fetched = rest.getForEntity(baseUrl("/api/analysis/" + aid), Map.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat((String) fetched.getBody().get("comment")).isEqualTo("Updated E2E Test");

        rest.exchange(baseUrl("/api/analysis/" + aid), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), Void.class);
    }

    @Test
    void updateNonexistentAnalysis_shouldReturn404() {
        String fakeUpdate = AnalysisE2EUtils.buildValidJson(null, null, timestamp());
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
    void partialUpdateWithPatch_shouldSucceedIfSupported() {
        String createJson = AnalysisE2EUtils.buildValidJson(null, null, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(createJson, jsonHeaders()),
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);
        Long aid = ((Number) created.getBody().get("aid")).longValue();

        String patchJson = """
            {
              "comment": "Patched comment",
              "density": 5.5
            }
            """;

        ResponseEntity<Map> patched = rest.exchange(
                baseUrl("/api/analysis/" + aid),
                HttpMethod.PATCH,
                new HttpEntity<>(patchJson, jsonHeaders()),
                Map.class
        );

        assertThat(patched.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NO_CONTENT);

        ResponseEntity<Map> fetched = rest.getForEntity(baseUrl("/api/analysis/" + aid), Map.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((Number) fetched.getBody().get("density")).doubleValue()).isEqualTo(5.5);

        rest.exchange(baseUrl("/api/analysis/" + aid), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), Void.class);
    }

    @Test
    void updateAnalysisWithExtremeValues_shouldStillWork() {
        String createJson = AnalysisE2EUtils.buildValidJson(null, null, timestamp());
        ResponseEntity<Map> created = rest.postForEntity(
                baseUrl("/api/analysis"),
                new HttpEntity<>(createJson, jsonHeaders()),
                Map.class
        );

        Long aid = ((Number) created.getBody().get("aid")).longValue();

        String extremeUpdate = createJson
                .replace("\"weightMea\": 1.0", "\"weightMea\": 999999.99")
                .replace("\"weightNrm\": 1.0", "\"weightNrm\": 888888.88")
                .replace("\"comment\": \"E2E Analysis Test\"", "\"comment\": \"Extreme update test\"");

        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/analysis/" + aid),
                HttpMethod.PUT,
                new HttpEntity<>(extremeUpdate, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Map> fetched = rest.getForEntity(baseUrl("/api/analysis/" + aid), Map.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody().get("comment")).isEqualTo("Extreme update test");

        rest.exchange(baseUrl("/api/analysis/" + aid), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders()), Void.class);
    }
}