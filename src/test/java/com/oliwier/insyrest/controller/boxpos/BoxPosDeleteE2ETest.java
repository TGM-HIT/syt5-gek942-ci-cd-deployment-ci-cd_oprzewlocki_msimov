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

}
