package com.oliwier.insyrest.controller.boxpos;

import com.oliwier.insyrest.controller.BaseIntegrationTest;
import com.oliwier.insyrest.controller.BoxIntegrationUtils;
import com.oliwier.insyrest.controller.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxPosCreateIntegrationTest extends BaseIntegrationTest {


    @Test
    void createBoxPos_withMissingBposId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxIntegrationUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bpos_id": "%s",
              "s_id": "%s",
              "s_stamp": "%s"
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

        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bpos_id": 5004,
              "s_id": "%s",
              "s_stamp": "%s"
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

        postJson(baseUrl("/api/boxes"), BoxIntegrationUtils.buildValidJson(bId), Map.class);

        String json = """
            {
              "bpos_id": 5005,
              "b_id": "%s",
              "s_stamp": "%s"
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
