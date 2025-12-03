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

        postJson(baseUrl("/api/boxes"), json, Map.class);

        String updateJson = """
            {
              "bid": "%s",
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
    }

    @Test
    void updateNonexistentBox_shouldReturn404() {
        String json = """
            {
              "bid": "ZZZZ",
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
}
