package com.oliwier.insyrest.controller.box;

import com.oliwier.insyrest.controller.BaseIntegrationTest;
import com.oliwier.insyrest.controller.BoxIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxCreateIntegrationTest extends BaseIntegrationTest {

    @Test
    void createBox_withValidData_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxIntegrationUtils.buildValidJson(bId);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxes"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).containsKey("bid");
        assertThat(res.getBody().get("bid")).isEqualTo(bId);
    }

    @Test
    void createBox_withMinimalRequiredFields_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String json = """
            {
              "bid": "%s"
            }
            """.formatted(bId);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxes"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("bid")).isEqualTo(bId);
    }

    @Test
    void createBox_withDuplicateBId_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxIntegrationUtils.buildValidJson(bId);

        rest.postForEntity(baseUrl("/api/boxes"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/boxes"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createBox_withMissingBId_shouldReturn400() {
        String json = """
            {
              "name": "Box without ID"
            }
            """;

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/boxes"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createBox_withInvalidJson_shouldReturn400() {
        String json = "{ invalid json }";

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/boxes"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createBox_withAllFieldsPopulated_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String ts = timestamp();
        String json = """
            {
              "bid": "%s",
              "name": "Complete Box",
              "numMax": 250,
              "type": 2,
              "comment": "Fully populated test box",
              "dateExported": "%s"
            }
            """.formatted(bId, ts);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxes"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
