package com.oliwier.insyrest.controller.threshold;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.ThresholdE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ThresholdReadE2ETest extends BaseE2ETest {

    @Test
    void getThreshold_byExistingId_shouldReturn200() {
        String thId = uniqueId();
        String ts = timestamp();
        String json = ThresholdE2EUtils.buildValidJson(thId, "15.5", "25.5", ts);

        postJson(baseUrl("/api/thresholds"), json, Map.class);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/thresholds/" + thId),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().get("th_id")).isEqualTo(thId);
        assertThat(res.getBody().get("value_min")).isEqualTo(15.5);
        assertThat(res.getBody().get("value_max")).isEqualTo(25.5);
    }

    @Test
    void getThreshold_byNonExistentId_shouldReturn404() {
        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/thresholds/NONEXISTENT"),
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
        List content = (List) res.getBody().get("content");
        assertThat(content).isEmpty();
    }

    @Test
    void getAllThresholds_withSingleEntry_shouldReturn200() {
        String thId = uniqueId();
        String ts = timestamp();
        String json = ThresholdE2EUtils.buildValidJson(thId, "10.0", "20.0", ts);

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
        String thId1 = uniqueId();
        String thId2 = uniqueId();
        String ts = timestamp();

        postJson(baseUrl("/api/thresholds"),
                ThresholdE2EUtils.buildValidJson(thId1, "10.0", "20.0", ts),
                Map.class);
        postJson(baseUrl("/api/thresholds"),
                ThresholdE2EUtils.buildValidJson(thId2, "30.0", "40.0", ts),
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
