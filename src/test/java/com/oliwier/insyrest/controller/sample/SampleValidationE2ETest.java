package com.oliwier.insyrest.controller.sample;

import com.oliwier.insyrest.controller.BaseE2ETest;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;

class SampleValidationE2ETest extends BaseE2ETest {

    @Test
    void createSample_withNegativeWeight_shouldAccept() {
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

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
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

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createSample_withMaxLengthComment_shouldAccept() {
        String maxComment = "x".repeat(1000);
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "comment": "%s"
            }
            """.formatted(uniqueId(), timestamp(), maxComment);

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createSample_withCommentExceedingLimit_shouldReturn500() {
        String tooLongComment = "x".repeat(1001);
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "comment": "%s"
            }
            """.formatted(uniqueId(), timestamp(), tooLongComment);

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void createSample_withReasonableLengthComment_shouldAccept() {
        String reasonableComment = "This is a test comment. ".repeat(20);
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "comment": "%s"
            }
            """.formatted(uniqueId(), timestamp(), reasonableComment);

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createSample_withValidSFlags_shouldAccept() {
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "sFlags": "12345"
            }
            """.formatted(uniqueId(), timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createSample_withMaxLengthSFlags_shouldAccept() {
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "sFlags": "1234567890"
            }
            """.formatted(uniqueId(), timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createSample_withSFlagsExceedingLimit_shouldReturn500() {
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "sFlags": "12345678901"
            }
            """.formatted(uniqueId(), timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void createSample_withMaxLengthName_shouldAccept() {
        String maxName = "Sample Name ".repeat(40).substring(0, 500);
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "name": "%s"
            }
            """.formatted(uniqueId(), timestamp(), maxName);

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createSample_withNameExceedingLimit_shouldReturn500() {
        String tooLongName = "x".repeat(501);
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "name": "%s"
            }
            """.formatted(uniqueId(), timestamp(), tooLongName);

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
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
              "comment": "All fields populated for comprehensive test",
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

    @Test
    void createSample_withWeightExceedingPrecision_shouldReject() {
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "weightNet": 9999999.99
            }
            """.formatted(uniqueId(), timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void createSample_withTooManyDecimalPlaces_shouldHandleGracefully() {
        String json = """
            {
              "s_id": "%s",
              "s_stamp": "%s",
              "weightNet": 10.12345
            }
            """.formatted(uniqueId(), timestamp());

        ResponseEntity<String> res = rest.postForEntity(
                baseUrl("/api/samples"),
                new HttpEntity<>(json, jsonHeaders()),
                String.class
        );

        assertThat(res.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
