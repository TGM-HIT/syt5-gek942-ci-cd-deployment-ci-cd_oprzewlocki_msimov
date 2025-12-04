package com.oliwier.insyrest.controller.boxpos;

import com.oliwier.insyrest.controller.BaseIntegrationTest;
import com.oliwier.insyrest.controller.BoxIntegrationUtils;
import com.oliwier.insyrest.controller.BoxPosIntegrationUtils;
import com.oliwier.insyrest.controller.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxPosUpdateIntegrationTest extends BaseIntegrationTest {


    @Test
    void updateBoxPos_withMismatchedBposId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxIntegrationUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 7002;
        String json = BoxPosIntegrationUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());
        postJson(baseUrl("/api/boxpos"), json, Map.class);

        String mismatchJson = BoxPosIntegrationUtils.buildValidJson(9999, bId, sId, sStamp, timestamp());
        String compositeId = bposId + "," + bId;

        HttpEntity<String> request = new HttpEntity<>(mismatchJson, jsonHeaders());
        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/boxpos/" + compositeId),
                HttpMethod.PUT,
                request,
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.CONFLICT);
    }

    @Test
    void updateBoxPos_withMismatchedBId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxIntegrationUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 7003;
        String json = BoxPosIntegrationUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());
        postJson(baseUrl("/api/boxpos"), json, Map.class);

        String mismatchJson = BoxPosIntegrationUtils.buildValidJson(bposId, "DIFF", sId, sStamp, timestamp());
        String compositeId = bposId + "," + bId;

        HttpEntity<String> request = new HttpEntity<>(mismatchJson, jsonHeaders());
        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/boxpos/" + compositeId),
                HttpMethod.PUT,
                request,
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.CONFLICT);
    }
}
