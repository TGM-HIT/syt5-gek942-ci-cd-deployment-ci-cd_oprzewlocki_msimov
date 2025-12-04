package com.oliwier.insyrest.controller.threshold;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.ThresholdE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ThresholdCreateE2ETest extends BaseE2ETest {

    @Test
    void createThreshold_withValidData_shouldReturn201() {
        String thId = ThresholdE2EUtils.generateShortThId();
        String ts = timestamp();
        String json = ThresholdE2EUtils.buildValidJson(thId, "10.5", "99.99", ts);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/thresholds"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).containsKey("thId");
        assertThat(res.getBody().get("thId")).isEqualTo(thId);
        assertThat(res.getBody().get("valueMin")).isEqualTo(10.5);
        assertThat(res.getBody().get("valueMax")).isEqualTo(99.99);
    }

    @Test
    void createThreshold_withMinimalRequiredFields_shouldReturn201() {
        String thId = ThresholdE2EUtils.generateShortThId();
        String json = ThresholdE2EUtils.buildMinimalJson(thId);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/thresholds"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("thId")).isEqualTo(thId);
    }

    @Test
    void createThreshold_withEqualMinMax_shouldReturn201() {
        String thId = ThresholdE2EUtils.generateShortThId();
        String ts = timestamp();
        String json = ThresholdE2EUtils.buildValidJson(thId, "50.0", "50.0", ts);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/thresholds"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("valueMin")).isEqualTo(50.0);
        assertThat(res.getBody().get("valueMax")).isEqualTo(50.0);
    }

    @Test
    void createThreshold_withZeroValues_shouldReturn201() {
        String thId = ThresholdE2EUtils.generateShortThId();
        String ts = timestamp();
        String json = ThresholdE2EUtils.buildValidJson(thId, "0", "0", ts);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/thresholds"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("valueMin")).isEqualTo(0.0);
        assertThat(res.getBody().get("valueMax")).isEqualTo(0.0);
    }

    @Test
    void createThreshold_withNegativeValues_shouldReturn201() {
        String thId = ThresholdE2EUtils.generateShortThId();
        String ts = timestamp();
        String json = ThresholdE2EUtils.buildValidJson(thId, "-50.0", "-10.0", ts);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/thresholds"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("valueMin")).isEqualTo(-50.0);
        assertThat(res.getBody().get("valueMax")).isEqualTo(-10.0);
    }

    @Test
    void createThreshold_withMaxPrecision_shouldReturn201() {
        String thId = ThresholdE2EUtils.generateShortThId();
        String ts = timestamp();
        String json = ThresholdE2EUtils.buildValidJson(thId, "123456.78", "999999.99", ts);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/thresholds"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("valueMin")).isEqualTo(123456.78);
        assertThat(res.getBody().get("valueMax")).isEqualTo(999999.99);
    }

    @Test
    void createThreshold_withoutDateChanged_shouldReturn201() {
        String thId = ThresholdE2EUtils.generateShortThId();
        String json = ThresholdE2EUtils.buildWithoutOptionalFields(thId, "10.0", "20.0");

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/thresholds"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("thId")).isEqualTo(thId);
    }

    @Test
    void createThreshold_withMissingThId_shouldReturn400() {
        String json = """
            {
              "value_min": 10.0,
              "value_max": 20.0
            }
            """;

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/thresholds"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createThreshold_withInvalidJson_shouldReturn400() {
        String json = "{ invalid json }";

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/thresholds"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createThreshold_withReversedMinMax_shouldReturn201() {
        String thId = ThresholdE2EUtils.generateShortThId();
        String ts = timestamp();
        String json = ThresholdE2EUtils.buildValidJson(thId, "100.0", "10.0", ts);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/thresholds"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("valueMin")).isEqualTo(100.0);
        assertThat(res.getBody().get("valueMax")).isEqualTo(10.0);
    }
}
