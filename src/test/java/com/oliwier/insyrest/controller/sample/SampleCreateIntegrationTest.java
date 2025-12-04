package com.oliwier.insyrest.controller.sample;

import com.oliwier.insyrest.controller.BaseIntegrationTest;
import com.oliwier.insyrest.controller.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class SampleCreateIntegrationTest extends BaseIntegrationTest {

    @Test
    void createSample_withValidData_shouldReturn201() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/samples"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).containsKeys("s_id", "s_stamp", "name");
        assertThat(res.getBody().get("s_id")).isEqualTo(sId);
    }

    @Test
    void createSample_withMinimalRequiredFields_shouldReturn201() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s"
            }
            """.formatted(sId, sStamp);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/samples"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createSample_withDuplicateCompositeId_shouldReturn201OnSecondAttempt() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        ResponseEntity<Map> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createSample_withMissingSId_shouldReturn400() {
        String json = """
            {
              "s_stamp": "%s",
              "name": "Missing ID Sample"
            }
            """.formatted(timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createSample_withMissingSStamp_shouldReturn400() {
        String json = """
            {
              "s_id": "%s",
              "name": "Missing Stamp Sample"
            }
            """.formatted(uniqueId());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createSample_withInvalidJson_shouldReturn400() {
        String json = "{ invalid json }";

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

