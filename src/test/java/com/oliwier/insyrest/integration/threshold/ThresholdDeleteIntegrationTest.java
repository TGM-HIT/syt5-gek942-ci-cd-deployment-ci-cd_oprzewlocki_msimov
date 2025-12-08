package com.oliwier.insyrest.integration.threshold;

import com.oliwier.insyrest.integration.BaseIntegrationTest;
import com.oliwier.insyrest.integration.ThresholdIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ThresholdDeleteIntegrationTest extends BaseIntegrationTest {

    @Test
    void deleteThreshold_existing_shouldReturn204() {
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId, "10.0", "20.0", ts),
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
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId, "10.0", "20.0", ts),
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
                baseUrl("/api/thresholds/NONEXIST"),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteThreshold_multiple_shouldReturn204ForEach() {
        String thId1 = ThresholdIntegrationUtils.generateShortThId();
        String thId2 = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();

        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId1, "10.0", "20.0", ts),
                Map.class);
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId2, "30.0", "40.0", ts),
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
        String thId = ThresholdIntegrationUtils.generateShortThId();
        String ts = timestamp();
        postJson(baseUrl("/api/thresholds"),
                ThresholdIntegrationUtils.buildValidJson(thId, "10.0", "20.0", ts),
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
