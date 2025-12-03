package com.oliwier.insyrest.controller.boxpos;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.BoxE2EUtils;
import com.oliwier.insyrest.controller.BoxPosE2EUtils;
import com.oliwier.insyrest.controller.SampleE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.time.LocalDateTime;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxPosUpdateE2ETest extends BaseE2ETest {


    @Test
    void updateBoxPos_withMismatchedBposId_shouldReturn400() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 7002;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());
        postJson(baseUrl("/api/boxpos"), json, Map.class);

        String mismatchJson = BoxPosE2EUtils.buildValidJson(9999, bId, sId, sStamp, timestamp());
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

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 7003;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());
        postJson(baseUrl("/api/boxpos"), json, Map.class);

        String mismatchJson = BoxPosE2EUtils.buildValidJson(bposId, "DIFF", sId, sStamp, timestamp());
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
