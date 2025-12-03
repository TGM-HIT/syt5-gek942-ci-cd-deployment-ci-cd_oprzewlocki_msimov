package com.oliwier.insyrest.controller.sample;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.SampleE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class SampleUpdateE2ETest extends BaseE2ETest {

    @Test
    void updateExistingSample_shouldReturn200() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleE2EUtils.buildValidJson(sId, sStamp, timestamp());

        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        String updateJson = """
        {
          "s_id": "%s",
          "s_stamp": "%s",
          "name": "Updated Sample",
          "weightNet": 15.5,
          "quantity": 2
        }
        """.formatted(sId, sStamp);

        HttpEntity<String> request = new HttpEntity<>(updateJson, jsonHeaders());
        String compositeId = sId + "," + sStamp;

        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/samples/" + compositeId),
                HttpMethod.PUT,
                request,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/samples/" + compositeId),
                Map.class
        );

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody().get("name")).isEqualTo("Updated Sample");
    }


    @Test
    void updateNonexistentSample_shouldReturn404() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "name": "Nonexistent"
            }
            """.formatted(sId, sStamp);

        HttpEntity<String> request = new HttpEntity<>(json, jsonHeaders());
        String compositeId = sId + "," + sStamp;

        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/samples/" + compositeId),
                HttpMethod.PUT,
                request,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateSample_changingCompositeIdInBody_returnsOkButIgnoresIdChange() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleE2EUtils.buildValidJson(sId, sStamp, timestamp());

        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        String updateJson = """
        {
          "s_id": "different-id",
          "s_stamp": "%s",
          "name": "Changed ID Attempt"
        }
        """.formatted(sStamp);

        HttpEntity<String> request = new HttpEntity<>(updateJson, jsonHeaders());
        String compositeId = sId + "," + sStamp;

        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/samples/" + compositeId),
                HttpMethod.PUT,
                request,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/samples/" + compositeId),
                Map.class
        );
        assertThat(fetched.getBody().get("s_id")).isEqualTo(sId);
    }


    @Test
    void updateSample_partialUpdate_shouldWork() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleE2EUtils.buildValidJson(sId, sStamp, timestamp());

        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        String updateJson = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "comment": "Partial update test"
            }
            """.formatted(sId, sStamp);

        HttpEntity<String> request = new HttpEntity<>(updateJson, jsonHeaders());
        String compositeId = sId + "," + sStamp;

        ResponseEntity<Map> res = rest.exchange(
                baseUrl("/api/samples/" + compositeId),
                HttpMethod.PUT,
                request,
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody().get("comment")).isEqualTo("Partial update test");
    }
}
