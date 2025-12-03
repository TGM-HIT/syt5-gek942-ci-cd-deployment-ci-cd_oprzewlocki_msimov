package com.oliwier.insyrest.controller.boxpos;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.BoxE2EUtils;
import com.oliwier.insyrest.controller.BoxPosE2EUtils;
import com.oliwier.insyrest.controller.SampleE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxPosCreateE2ETest extends BaseE2ETest {

    @Test
    void createBoxPos_withValidData_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 5001;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).containsKeys("bposId", "bId", "sId", "sStamp", "dateExported");
        assertThat(res.getBody().get("bposId")).isEqualTo(bposId);
        assertThat(res.getBody().get("bId")).isEqualTo(bId);
    }

    @Test
    void createBoxPos_withMinimalRequiredFields_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 5002;
        String json = BoxPosE2EUtils.buildMinimalJson(bposId, bId, sId, sStamp);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("bposId")).isEqualTo(bposId);
    }

    @Test
    void createBoxPos_withDuplicateCompositeKey_shouldReturn409OrError() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 5003;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());

        ResponseEntity<Map> first = postJson(baseUrl("/api/boxpos"), json, Map.class);
        assertThat(first.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> duplicate = postJson(baseUrl("/api/boxpos"), json, String.class);
        assertThat(duplicate.getStatusCode()).isIn(HttpStatus.CONFLICT, HttpStatus.BAD_REQUEST);
    }

    @Test
    void createBoxPos_withMissingBposId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bId": "%s",
              "sId": "%s",
              "sStamp": "%s"
            }
            """.formatted(bId, sId, sStamp);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createBoxPos_withMissingBId_shouldReturn400() {
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bposId": 5004,
              "sId": "%s",
              "sStamp": "%s"
            }
            """.formatted(sId, sStamp);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createBoxPos_withMissingSId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);

        String json = """
            {
              "bposId": 5005,
              "bId": "%s",
              "sStamp": "%s"
            }
            """.formatted(bId, timestamp());

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createBoxPos_withZeroBposId_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = BoxPosE2EUtils.buildValidJson(0, bId, sId, sStamp, timestamp());

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("bposId")).isEqualTo(0);
    }

    @Test
    void createBoxPos_withInvalidJson_shouldReturn400() {
        String json = "{ invalid json }";

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
