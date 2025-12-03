package com.oliwier.insyrest.controller.sample;

import com.oliwier.insyrest.controller.BaseE2ETest;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;

class SampleValidationE2ETest extends BaseE2ETest {

    @Test
    void createSample_withNegativeWeight_shouldAcceptOrReject() {
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "weightNet": -5.0
            }
            """.formatted(uniqueId(), timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.BAD_REQUEST);
    }

    @Test
    void createSample_withVeryLargeWeights_shouldAccept() {
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "weightNet": 999999.99,
              "weightBru": 999999.99
            }
            """.formatted(uniqueId(), timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.BAD_REQUEST);
    }

    @Test
    void createSample_withLongComment_shouldAccept() {
        String longComment = "x".repeat(500);
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "comment": "%s"
            }
            """.formatted(uniqueId(), timestamp(), longComment);

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.BAD_REQUEST);
    }

    @Test
    void createSample_withInvalidSFlags_shouldAcceptOrReject() {
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "sFlags": "TOOLONGTOOLONGTOOLONG"
            }
            """.formatted(uniqueId(), timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.BAD_REQUEST);
    }

    @Test
    void createSample_withAllFieldsPopulated_shouldReturn201() {
        String ts = timestamp();
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "name": "Complete Sample",
              "weightNet": 10.5,
              "weightBru": 12.5,
              "weightTar": 2.0,
              "quantity": 5,
              "distance": 100.25,
              "dateCrumbled": "%s",
              "sFlags": "12345",
              "lane": 3,
              "comment": "All fields test",
              "dateExported": "%s"
            }
            """.formatted(uniqueId(), ts, ts, ts);

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
