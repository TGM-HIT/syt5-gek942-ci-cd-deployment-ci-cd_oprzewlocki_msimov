package com.oliwier.insyrest.controller.box;

import com.oliwier.insyrest.controller.BaseE2ETest;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;

class BoxValidationE2ETest extends BaseE2ETest {

    @Test
    void createBox_withMaxLengthName_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String maxName = "x".repeat(255);
        String json = """
            {
              "b_id": "%s",
              "name": "%s"
            }
            """.formatted(bId, maxName);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxes"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createBox_withNameExceedingLimit_shouldReturn500() {
        String bId = uniqueId().substring(0, 4);
        String tooLongName = "x".repeat(256);
        String json = """
            {
              "b_id": "%s",
              "name": "%s"
            }
            """.formatted(bId, tooLongName);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxes"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void createBox_withMaxLengthComment_shouldReturn201() {
        String bId = uniqueId().substring(0, 4);
        String maxComment = "x".repeat(255);
        String json = """
            {
              "b_id": "%s",
              "comment": "%s"
            }
            """.formatted(bId, maxComment);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxes"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createBox_withCommentExceedingLimit_shouldReturn500() {
        String bId = uniqueId().substring(0, 4);
        String tooLongComment = "x".repeat(256);
        String json = """
            {
              "b_id": "%s",
              "comment": "%s"
            }
            """.formatted(bId, tooLongComment);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxes"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void createBox_withNegativeNumMax_shouldAccept() {
        String bId = uniqueId().substring(0, 4);
        String json = """
            {
              "b_id": "%s",
              "num_max": -10
            }
            """.formatted(bId);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxes"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createBox_withVeryLargeNumMax_shouldAccept() {
        String bId = uniqueId().substring(0, 4);
        String json = """
            {
              "b_id": "%s",
              "num_max": 999999
            }
            """.formatted(bId);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxes"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createBox_withNegativeType_shouldAccept() {
        String bId = uniqueId().substring(0, 4);
        String json = """
            {
              "b_id": "%s",
              "type": -1
            }
            """.formatted(bId);

        ResponseEntity<String> res = postJson(
                baseUrl("/api/boxes"),
                json,
                String.class
        );

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
