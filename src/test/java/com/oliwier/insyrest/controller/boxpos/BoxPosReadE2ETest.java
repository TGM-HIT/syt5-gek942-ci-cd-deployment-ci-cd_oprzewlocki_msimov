package com.oliwier.insyrest.controller.boxpos;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.BoxE2EUtils;
import com.oliwier.insyrest.controller.BoxPosE2EUtils;
import com.oliwier.insyrest.controller.SampleE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxPosReadE2ETest extends BaseE2ETest {

    @Test
    void getAllBoxPos_shouldReturnPaginatedList() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/boxpos"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKeys("content", "pageable", "total_elements");
    }

    @Test
    void getBoxPosByCompositeId_existing_shouldReturn200() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 6001;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());

        ResponseEntity<Map> created = postJson(baseUrl("/api/boxpos"), json, Map.class);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String compositeId = bposId + "," + bId;
        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/boxpos/" + compositeId),
                Map.class
        );

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody().get("bposId")).isEqualTo(bposId);
        assertThat(fetched.getBody().get("bId")).isEqualTo(bId);
    }

    @Test
    void getBoxPosByCompositeId_nonexistent_shouldReturn404() {
        String compositeId = "99999,ZZZZ";

        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/boxpos/" + compositeId),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBoxPos_withInvalidCompositeIdFormat_shouldReturn400() {
        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/boxpos/invalid-format"),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
    }

    @Test
    void getBoxPosPagination_withCustomPageSize_shouldWork() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/boxpos?page=0&size=10"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map body = res.getBody();
        assertThat(body).containsKey("pageable");
        assertThat(((Map) body.get("pageable")).get("page_size")).isEqualTo(10);
    }

    @Test
    void getBoxPosPagination_secondPage_shouldWork() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/boxpos?page=1&size=5"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

    @Test
    void getBoxPos_filterByBId_shouldReturnFiltered() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 6002;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());
        postJson(baseUrl("/api/boxpos"), json, Map.class);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/boxpos?filter[bId]=" + bId),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

    @Test
    void getBoxPos_filterBySId_shouldReturnFiltered() {
        String bId = uniqueId().substring(0, 4);
        String sId = uniqueId();
        String sStamp = timestamp();

        postJson(baseUrl("/api/boxes"), BoxE2EUtils.buildValidJson(bId), Map.class);
        postJson(baseUrl("/api/samples"), SampleE2EUtils.buildValidJson(sId, sStamp, timestamp()), Map.class);

        int bposId = 6003;
        String json = BoxPosE2EUtils.buildValidJson(bposId, bId, sId, sStamp, timestamp());
        postJson(baseUrl("/api/boxpos"), json, Map.class);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/boxpos?filter[sId]=" + sId),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }
}
