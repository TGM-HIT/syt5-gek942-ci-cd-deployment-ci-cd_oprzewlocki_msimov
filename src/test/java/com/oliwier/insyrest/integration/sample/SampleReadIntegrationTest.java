package com.oliwier.insyrest.integration.sample;

import com.oliwier.insyrest.integration.BaseIntegrationTest;
import com.oliwier.insyrest.integration.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class SampleReadIntegrationTest extends BaseIntegrationTest {

    @Test
    void getAllSamples_shouldReturnList() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/samples"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

    @Test
    void getExistingSampleByCompositeId_shouldReturn200AndCorrectData() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        ResponseEntity<Map> created = postJson(
                baseUrl("/api/samples"),
                json,
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String compositeId = sId + "," + sStamp;
        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/samples/" + compositeId),
                Map.class
        );

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody().get("s_id")).isEqualTo(sId);
        assertThat(fetched.getBody().get("name")).isEqualTo("E2E Sample");
    }

    @Test
    void getNonexistentSample_shouldReturn404() {
        String compositeId = uniqueId() + "," + timestamp();

        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/samples/" + compositeId),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getSample_withInvalidCompositeIdFormat_shouldReturn400() {
        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/samples/invalid-id-format"),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND);
    }

    @Test
    void getSamplesPagination_shouldWork() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/samples?page=0&size=5"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKeys("content", "total_elements");
    }

    @Test
    void getSamplesByName_shouldReturnFiltered() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/samples?filter[name]=E2E Sample"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }
}
