package com.oliwier.insyrest.integration.sample;

import com.oliwier.insyrest.integration.BaseIntegrationTest;
import com.oliwier.insyrest.integration.SampleIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class SampleDeleteIntegrationTest extends BaseIntegrationTest {

    @Test
    void deleteExistingSample_shouldReturn204() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        String compositeId = sId + "," + sStamp;

        ResponseEntity<Void> res = rest.exchange(
                baseUrl("/api/samples/" + compositeId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteNonexistentSample_shouldReturn404() {
        String compositeId = uniqueId() + "," + timestamp();

        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/samples/" + compositeId),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteSample_thenGetIt_shouldReturn404() {
        String sId = uniqueId();
        String sStamp = timestamp();
        String json = SampleIntegrationUtils.buildValidJson(sId, sStamp, timestamp());

        rest.postForEntity(baseUrl("/api/samples"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        String compositeId = sId + "," + sStamp;

        rest.exchange(
                baseUrl("/api/samples/" + compositeId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        ResponseEntity<String> getRes = rest.getForEntity(
                baseUrl("/api/samples/" + compositeId),
                String.class
        );

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
