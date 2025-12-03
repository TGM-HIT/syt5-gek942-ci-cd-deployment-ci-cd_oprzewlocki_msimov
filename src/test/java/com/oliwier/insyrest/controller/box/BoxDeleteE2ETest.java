package com.oliwier.insyrest.controller.box;

import com.oliwier.insyrest.controller.BaseE2ETest;
import com.oliwier.insyrest.controller.BoxE2EUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxDeleteE2ETest extends BaseE2ETest {

    @Test
    void deleteExistingBox_shouldReturn204() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxE2EUtils.buildValidJson(bId);

        rest.postForEntity(baseUrl("/api/boxes"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        ResponseEntity<Void> res = rest.exchange(
                baseUrl("/api/boxes/" + bId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteNonexistentBox_shouldReturn404() {
        ResponseEntity<String> res = rest.exchange(
                baseUrl("/api/boxes/ZZZZ"),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBox_thenGetIt_shouldReturn404() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxE2EUtils.buildValidJson(bId);

        rest.postForEntity(baseUrl("/api/boxes"), new HttpEntity<>(json, jsonHeaders()), Map.class);

        rest.exchange(
                baseUrl("/api/boxes/" + bId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        ResponseEntity<String> getRes = rest.getForEntity(
                baseUrl("/api/boxes/" + bId),
                String.class
        );

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
