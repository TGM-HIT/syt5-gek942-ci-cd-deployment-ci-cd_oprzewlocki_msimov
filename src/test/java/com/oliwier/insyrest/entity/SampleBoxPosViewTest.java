package com.oliwier.insyrest.entity;

import com.oliwier.insyrest.entity.id.SampleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SampleBoxPosView Entity Unit Tests")
class SampleBoxPosViewTest {

    private SampleBoxPosView view;
    private SampleId sampleId;

    @BeforeEach
    void setUp() {
        view = new SampleBoxPosView();
        sampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
    }

    @Test
    @DisplayName("Should create view with all fields using constructor")
    void constructor_WithAllFields_SetsAllProperties() {
        LocalDateTime stamp = LocalDateTime.of(2025, 12, 8, 10, 0);
        SampleBoxPosView fullView = new SampleBoxPosView("S001", stamp, "BOX1/5");

        assertEquals("S001", fullView.getSId());
        assertEquals(stamp, fullView.getSStamp());
        assertEquals("BOX1/5", fullView.getBoxpos());
    }

    @Test
    @DisplayName("Should create view using builder")
    void builder_WithAllFields_CreatesView() {
        LocalDateTime stamp = LocalDateTime.of(2025, 12, 8, 10, 0);

        SampleBoxPosView builtView = SampleBoxPosView.builder()
                .sId("S001")
                .sStamp(stamp)
                .boxpos("BOX1/5")
                .build();

        assertEquals("S001", builtView.getSId());
        assertEquals(stamp, builtView.getSStamp());
        assertEquals("BOX1/5", builtView.getBoxpos());
    }

    @Test
    @DisplayName("Should set and get sId correctly")
    void setSId_WithValidId_StoresCorrectly() {
        view.setSId("S123");
        assertEquals("S123", view.getSId());
    }

    @Test
    @DisplayName("Should set and get sStamp correctly")
    void setSStamp_WithValidTimestamp_StoresCorrectly() {
        LocalDateTime stamp = LocalDateTime.of(2025, 12, 8, 14, 30);
        view.setSStamp(stamp);
        assertEquals(stamp, view.getSStamp());
    }

    @Test
    @DisplayName("Should set and get boxpos correctly")
    void setBoxpos_WithValidString_StoresCorrectly() {
        view.setBoxpos("BOX2/3");
        assertEquals("BOX2/3", view.getBoxpos());
    }

    @Test
    @DisplayName("Should handle single box position format")
    void setBoxpos_WithSinglePosition_StoresCorrectly() {
        view.setBoxpos("BOX1/5");
        assertEquals("BOX1/5", view.getBoxpos());
        assertTrue(view.getBoxpos().contains("/"));
    }

    @Test
    @DisplayName("Should handle multiple box positions format")
    void setBoxpos_WithMultiplePositions_StoresCorrectly() {
        view.setBoxpos("BOX1!");
        assertEquals("BOX1!", view.getBoxpos());
        assertTrue(view.getBoxpos().contains("!"));
    }

    @Test
    @DisplayName("Should handle dash for no positions")
    void setBoxpos_WithNoPositions_StoresDash() {
        view.setBoxpos("-");
        assertEquals("-", view.getBoxpos());
    }

    @Test
    @DisplayName("Should create empty view with no-args constructor")
    void noArgsConstructor_CreatesEmptyView() {
        SampleBoxPosView emptyView = new SampleBoxPosView();
        assertNotNull(emptyView);
        assertNull(emptyView.getSId());
        assertNull(emptyView.getSStamp());
        assertNull(emptyView.getBoxpos());
    }

    @Test
    @DisplayName("Should support equals and hashCode")
    void equalsAndHashCode_WorkCorrectly() {
        LocalDateTime stamp = LocalDateTime.of(2025, 12, 8, 10, 0);

        SampleBoxPosView view1 = SampleBoxPosView.builder()
                .sId("S001")
                .sStamp(stamp)
                .boxpos("BOX1/5")
                .build();

        SampleBoxPosView view2 = SampleBoxPosView.builder()
                .sId("S001")
                .sStamp(stamp)
                .boxpos("BOX1/5")
                .build();

        assertEquals(view1, view2);
        assertEquals(view1.hashCode(), view2.hashCode());
    }

    @Test
    @DisplayName("Should support toString")
    void toString_GeneratesCorrectString() {
        view.setSId("S001");
        view.setSStamp(LocalDateTime.of(2025, 12, 8, 10, 0));
        view.setBoxpos("BOX1/5");

        String result = view.toString();

        assertNotNull(result);
        assertTrue(result.contains("S001"));
        assertTrue(result.contains("BOX1/5"));
    }

    @Test
    @DisplayName("Should handle null boxpos")
    void setBoxpos_WithNull_AcceptsNull() {
        view.setBoxpos(null);
        assertNull(view.getBoxpos());
    }

    @Test
    @DisplayName("Should preserve LocalDateTime precision")
    void setSStamp_PreservesPrecision() {
        LocalDateTime preciseTime = LocalDateTime.of(2025, 12, 8, 14, 30, 45, 123456789);
        view.setSStamp(preciseTime);
        assertEquals(preciseTime, view.getSStamp());
    }
}
