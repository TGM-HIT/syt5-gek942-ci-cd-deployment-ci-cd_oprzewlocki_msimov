package com.oliwier.insyrest.controller.box;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.BoxE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxUpdateE2ETest extends BaseE2ETest {

    @Test
    void updateExistingBox_shouldReturn200() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxE2EUtils.buildValidJson(bId);

        rest.postForEntity(baseUrl("/api/boxes"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        String updateJson = """
            {
              "bId": "%s",
              "name": "Updated Box",
              "numMax": 200,
              "type": 2
            }
            """.formatted(bId);

        HttpEntity<String> request = new HttpEntity<>(updateJson, jsonHeaders());

        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/boxes/" + bId),
                HttpMethod.PUT,
                request,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify by fetching
        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/boxes/" + bId),
                Map.class
        );

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody().get("name")).isEqualTo("Updated Box");
    }

    @Test
    void updateNonexistentBox_shouldReturn404() {
        String json = """
            {
              "bId": "ZZZZ",
              "name": "Nonexistent"
            }
            """;

        HttpEntity<String> request = new HttpEntity<>(json, jsonHeaders());

        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/boxes/ZZZZ"),
                HttpMethod.PUT,
                request,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateBox_changingBIdInBody_returnsOkButIgnoresIdChange() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxE2EUtils.buildValidJson(bId);

        rest.postForEntity(baseUrl("/api/boxes"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        String updateJson = """
            {
              "bId": "DIFF",
              "name": "Changed ID Attempt"
            }
            """;

        HttpEntity<String> request = new HttpEntity<>(updateJson, jsonHeaders());

        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/boxes/" + bId),
                HttpMethod.PUT,
                request,
                Map.class
        );

        // API returns 200 but ID in path takes precedence
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify original ID is maintained
        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/boxes/" + bId),
                Map.class
        );
        assertThat(fetched.getBody().get("bId")).isEqualTo(bId);
    }

    @Test
    void updateBox_partialUpdate_shouldWork() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxE2EUtils.buildValidJson(bId);

        rest.postForEntity(baseUrl("/api/boxes"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        String updateJson = """
            {
              "bId": "%s",
              "comment": "Partial update test"
            }
            """.formatted(bId);

        HttpEntity<String> request = new HttpEntity<>(updateJson, jsonHeaders());

        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/boxes/" + bId),
                HttpMethod.PUT,
                request,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/boxes/" + bId),
                Map.class
        );
        assertThat(fetched.getBody().get("comment")).isEqualTo("Partial update test");
    }
}
