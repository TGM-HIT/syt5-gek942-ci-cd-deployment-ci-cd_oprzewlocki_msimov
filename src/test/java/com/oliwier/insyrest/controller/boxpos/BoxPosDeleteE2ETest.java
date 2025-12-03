package com.oliwier.insyrest.controller.boxpos;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.BoxE2EUtils;
import com.oliwier.insyrest.controller.BoxPosE2EUtils;
import com.oliwier.insyrest.controller.SampleE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxPosDeleteE2ETest extends BaseE2ETest {

    @Test
    void deleteBoxPos_existing_shouldReturn204() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 8001;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());

        ResponseEntity<Map> created = postJson(baseUrl("/api/boxpos"), json, Map.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String compositeId = bposId + "," + bId;
        ResponseEntity<Void> deleted = rest.exchange(
                baseUrl("/api/boxpos/" + compositeId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> fetched = rest.getForEntity(
                baseUrl("/api/boxpos/" + compositeId),
                String.class
        );
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBoxPos_nonexistent_shouldReturn404() {
        String compositeId = "99997,ZZZZ";

        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/boxpos/" + compositeId),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBoxPos_withInvalidCompositeId_shouldReturn400() {
        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/boxpos/invalid"),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBoxPos_twice_shouldReturn404OnSecond() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 8002;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());
        postJson(baseUrl("/api/boxpos"), json, Map.class);

        String compositeId = bposId + "," + bId;
        ResponseEntity<Void> firstDelete = rest.exchange(
                baseUrl("/api/boxpos/" + compositeId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(firstDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> secondDelete = rest.exchange(
                baseUrl("/api/boxpos/" + compositeId),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(secondDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
