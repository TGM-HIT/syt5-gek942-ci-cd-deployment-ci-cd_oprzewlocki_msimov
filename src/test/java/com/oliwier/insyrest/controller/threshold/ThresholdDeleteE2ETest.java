package com.oliwier.insyrest.controller.threshold;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.ThresholdE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ThresholdDeleteE2ETest extends BaseE2ETest {

    @Test
    void deleteThreshold_existing_shouldReturn204() {
        String thId = uniqueId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdE2EUtils.buildValidJson(thId, "10.0", "20.0", ts),
                Map.class);

        ResponseEntity<Void> res = rest.exchange(
                baseUrl("/api/thresholds/" + thId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteThreshold_verifyDeleted_shouldReturn404() {
        String thId = uniqueId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdE2EUtils.buildValidJson(thId, "10.0", "20.0", ts),
                Map.class);

        rest.exchange(
                baseUrl("/api/thresholds/" + thId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        ResponseEntity<String> getRes = rest.getForEntity(
                baseUrl("/api/thresholds/" + thId),
                String.class
        );

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteThreshold_nonExistent_shouldReturn404() {
        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/thresholds/NONEXISTENT"),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteThreshold_multiple_shouldReturn204ForEach() {
        String thId1 = uniqueId();
        String thId2 = uniqueId();
        String ts = timestamp();

        postJson(baseUrl("/api/thresholds"),
                ThresholdE2EUtils.buildValidJson(thId1, "10.0", "20.0", ts),
                Map.class);
        postJson(baseUrl("/api/thresholds"),
                ThresholdE2EUtils.buildValidJson(thId2, "30.0", "40.0", ts),
                Map.class);

        ResponseEntity<Void> res1 = rest.exchange(
                baseUrl("/api/thresholds/" + thId1),
                HttpMethod.DELETE,
                null,
                Void.class
        );
        ResponseEntity<Void> res2 = rest.exchange(
                baseUrl("/api/thresholds/" + thId2),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteThreshold_twice_shouldReturn404OnSecond() {
        String thId = uniqueId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdE2EUtils.buildValidJson(thId, "10.0", "20.0", ts),
                Map.class);

        rest.exchange(
                baseUrl("/api/thresholds/" + thId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        ResponseEntity<String> res2 = rest.exchange(
                baseUrl("/api/thresholds/" + thId),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
