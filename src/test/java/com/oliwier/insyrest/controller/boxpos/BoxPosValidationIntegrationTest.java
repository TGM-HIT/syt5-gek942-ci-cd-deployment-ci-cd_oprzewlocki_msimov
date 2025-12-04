package com.oliwier.insyrest.controller.boxpos;

import com.oliwier.insyrest.controller.BaseIntegrationTest;
import com.oliwier.insyrest.controller.BoxIntegrationUtils;
import com.oliwier.insyrest.controller.BoxPosIntegrationUtils;
import com.oliwier.insyrest.controller.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxPosValidationIntegrationTest extends BaseIntegrationTest {

    @Test
    void createBoxPos_withNullBposId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxIntegrationUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bpos_id": null,
              "b_id": "%s",
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
    void createBoxPos_withNullBId_shouldReturn400() {
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bpos_id": 9001,
              "b_id": null,
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
    void createBoxPos_withNullSId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);

        postJson(baseUrl("/api/boxes"), BoxIntegrationUtils.buildValidJson(bId), Map.class);

        String json = """
            {
              "bpos_id": 9002,
              "b_id": "%s",
              "s_id": null,
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
    void createBoxPos_withEmptyBId_shouldReturn400() {
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bpos_id": 9003,
              "b_id": "",
              "s_id": "%s",
              "s_stamp": "%s"
            }
            """.formatted(sId, sStamp);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void createBoxPos_withNegativeBposId_shouldHandleGracefully() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxIntegrationUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = BoxPosIntegrationUtils.buildValidJson(-1, bId, sId, sStamp, timestamp());

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isIn(
                HttpStatus.CREATED,
                HttpStatus.BAD_REQUEST,
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @Test
    void createBoxPos_withInvalidBIdLength_shouldHandleGracefully() {
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = BoxPosIntegrationUtils.buildValidJson(9004, "TOOLONG123", sId, sStamp, timestamp());

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void getBoxPos_withMissingCompositeIdPart_shouldReturn400() {
        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/boxpos/123,"),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
    }

    @Test
    void getBoxPos_withSortingParameter_shouldReturnSorted() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/boxpos?sort=bpos_id,desc"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

}
