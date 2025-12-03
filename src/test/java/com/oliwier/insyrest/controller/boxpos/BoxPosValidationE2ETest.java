package com.oliwier.insyrest.controller.boxpos;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.BoxE2EUtils;
import com.oliwier.insyrest.controller.BoxPosE2EUtils;
import com.oliwier.insyrest.controller.SampleE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxPosValidationE2ETest extends BaseE2ETest {

    @Test
    void createBoxPos_withNullBposId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bposId": null,
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
    void createBoxPos_withNullBId_shouldReturn400() {
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bposId": 9001,
              "bId": null,
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
    void createBoxPos_withNullSId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);

        String json = """
            {
              "bposId": 9002,
              "bId": "%s",
              "sId": null,
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
    void createBoxPos_withEmptyBId_shouldReturn400() {
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = """
            {
              "bposId": 9003,
              "bId": "",
              "sId": "%s",
              "sStamp": "%s"
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

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = BoxPosE2EUtils.buildValidJson(-1, bId, sId, sStamp, timestamp());

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

        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        String json = BoxPosE2EUtils.buildValidJson(9004, "TOOLONG123", sId, sStamp, timestamp());

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
                baseUrl("/api/boxpos?sort=bposId,desc"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

    @Test
    void createBoxPos_withFutureDateExported_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 9005;
        String futureDate = "2099-12-31T23:59:59";
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, futureDate);

        ResponseEntity<Map> res = postJson(
                baseUrl("/api/boxpos"),
                json,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
