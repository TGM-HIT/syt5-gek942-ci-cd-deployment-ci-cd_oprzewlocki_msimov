package com.oliwier.insyrest.integration.box;

import com.oliwier.insyrest.integration.BaseIntegrationTest;
import com.oliwier.insyrest.integration.BoxIntegrationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class BoxReadIntegrationTest extends BaseIntegrationTest {

    @Test
    void getAllBoxes_shouldReturnList() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/boxes"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKey("content");
    }

    @Test
    void getExistingBoxById_shouldReturn200AndCorrectData() {
        String bId = uniqueId().substring(0, 4);
        String json = BoxIntegrationUtils.buildValidJson(bId);

        ResponseEntity<Map> created = postJson(
                baseUrl("/api/boxes"),
                json,
                Map.class
        );

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Map> fetched = rest.getForEntity(
                baseUrl("/api/boxes/" + bId),
                Map.class
        );

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody().get("bid")).isEqualTo(bId);
        assertThat(fetched.getBody().get("name")).isEqualTo("E2E Test Box");
    }

    @Test
    void getNonexistentBox_shouldReturn404() {
        ResponseEntity<String> res = rest.getForEntity(
                baseUrl("/api/boxes/ZZZZ"),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBoxesPagination_shouldWork() {
        ResponseEntity<Map> res = rest.getForEntity(
                baseUrl("/api/boxes?page=0&size=5"),
                Map.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).containsKeys("content", "total_elements");
    }
}
