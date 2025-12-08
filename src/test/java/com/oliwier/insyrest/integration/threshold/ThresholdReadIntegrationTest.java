package com.oliwier.insyrest.integration.threshold;

import com.oliwier.insyrest.integration.BaseIntegrationTest;
import com.oliwier.insyrest.integration.ThresholdIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ThresholdReadIntegrationTest extends BaseIntegrationTest {

    @Test
    void getThreshold_byExistingId_shouldReturn200() {
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        String json = ThresholdIntegrationUtils.buildValidJson(thId, "15.5", "25.5", ts);

        postJson(baseUrl("/api/thresholds"), json, Map.class);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/thresholds/" + thId),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().get("thId")).isEqualTo(thId);
        assertThat(res.getBody().get("valueMin")).isEqualTo(15.5);
        assertThat(res.getBody().get("valueMax")).isEqualTo(25.5);
    }

    @Test
    void getThreshold_byNonExistentId_shouldReturn404() {
        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/thresholds/NONEXIST"),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllThresholds_whenEmpty_shouldReturn200WithEmptyList() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/thresholds"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

    @Test
    void getAllThresholds_withSingleEntry_shouldReturn200() {
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        String json = ThresholdIntegrationUtils.buildValidJson(thId, "10.0", "20.0", ts);

        postJson(baseUrl("/api/thresholds"), json, Map.class);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/thresholds"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        List content = (List) res.getBody().get("content");
        assertThat(content).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void getAllThresholds_withMultipleEntries_shouldReturn200() {
        String thId1 = ThresholdIntegrationUtils.generateShortThId();
        String thId2 = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();

        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId1, "10.0", "20.0", ts),
                Map.class);
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId2, "30.0", "40.0", ts),
                Map.class);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/thresholds"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        List content = (List) res.getBody().get("content");
        assertThat(content).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void getThreshold_withSpecialCharactersInId_shouldReturn404() {
        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/thresholds/TH@#$%"),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
