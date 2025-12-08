package com.oliwier.insyrest.integration.threshold;

import com.oliwier.insyrest.integration.BaseIntegrationTest;
import com.oliwier.insyrest.integration.ThresholdIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ThresholdUpdateIntegrationTest extends BaseIntegrationTest {

    @Test
    void updateThreshold_changeValues_shouldReturn200() {
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId, "10.0", "20.0", ts),
                Map.class);

        String updateJson = ThresholdIntegrationUtils.buildValidJson(thId, "15.0", "25.0", ts);
        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/thresholds/" + thId),
                HttpMethod.PUT,
                new HttpEntity<>(updateJson, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().get("valueMin")).isEqualTo(15.0);
        assertThat(res.getBody().get("valueMax")).isEqualTo(25.0);
    }

    @Test
    void updateThreshold_swapMinAndMax_shouldReturn200() {
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId, "10.0", "90.0", ts),
                Map.class);

        String updateJson = ThresholdIntegrationUtils.buildValidJson(thId, "90.0", "10.0", ts);
        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/thresholds/" + thId),
                HttpMethod.PUT,
                new HttpEntity<>(updateJson, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().get("valueMin")).isEqualTo(90.0);
        assertThat(res.getBody().get("valueMax")).isEqualTo(10.0);
    }

    @Test
    void updateThreshold_increaseValues_shouldReturn200() {
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId, "10.0", "20.0", ts),
                Map.class);

        String updateJson = ThresholdIntegrationUtils.buildValidJson(thId, "100.0", "200.0", ts);
        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/thresholds/" + thId),
                HttpMethod.PUT,
                new HttpEntity<>(updateJson, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().get("valueMin")).isEqualTo(100.0);
        assertThat(res.getBody().get("valueMax")).isEqualTo(200.0);
    }

    @Test
    void updateThreshold_nonExistent_shouldReturn404() {
        String ts = timestamp();
        String json = ThresholdIntegrationUtils.buildValidJson("NONEXIST", "10.0", "20.0", ts);

        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/thresholds/NONEXIST"),
                HttpMethod.PUT,
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateThreshold_toNullValues_shouldReturn200() {
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId, "10.0", "20.0", ts),
                Map.class);

        String updateJson = ThresholdIntegrationUtils.buildWithNullValues(thId);
        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/thresholds/" + thId),
                HttpMethod.PUT,
                new HttpEntity<>(updateJson, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
