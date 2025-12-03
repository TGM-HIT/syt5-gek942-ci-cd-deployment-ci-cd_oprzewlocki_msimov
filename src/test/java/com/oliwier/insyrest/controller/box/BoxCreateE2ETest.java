package com.oliwier.insyrest.controller.box;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.BoxE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxCreateE2ETest extends BaseE2ETest {

    @Test
    void createBox_withValidData_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxE2EUtils.buildValidJson(bId);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxes"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).containsKeys("bId", "name");
        assertThat(res.getBody().get("bId")).isEqualTo(bId);
    }

    @Test
    void createBox_withMinimalRequiredFields_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxE2EUtils.buildMinimalJson(bId);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxes"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("bId")).isEqualTo(bId);
    }

    @Test
    void createBox_withDuplicateBId_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxE2EUtils.buildValidJson(bId);

        rest.postForEntity(baseUrl("/api/boxes"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/boxes"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        // API allows upsert behavior
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
    void createBox_withBIdExceedingMaxLength_shouldReturn500() {
        String tooLongBId = "TOOLONG";  // Max is 4 chars
        String json = BoxE2EUtils.buildValidJson(tooLongBId);

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/boxes"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void createBox_withMaxLengthBId_shouldReturn201() {
        String bId = "ABCD";  // Exactly 4 chars
        String json = BoxE2EUtils.buildValidJson(bId);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxes"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createBox_withAllFieldsPopulated_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String ts = timestamp();
        String json = """
            {
              "bId": "%s",
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
