package com.oliwier.insyrest.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Box Entity Unit Tests")
class BoxTest {

    private Box box;

    @BeforeEach
    void setUp() {
        box = new Box();
    }

    @Test
    @DisplayName("Should create box with all fields")
    void constructor_WithAllFields_SetsAllProperties() {
        LocalDateTime now = LocalDateTime.now();
        Box fullBox = new Box("BOX1", "Test Box", 96, 1, "Test comment", now);

        assertEquals("BOX1", fullBox.getBId());
        assertEquals("Test Box", fullBox.getName());
        assertEquals(96, fullBox.getNumMax());
        assertEquals(1, fullBox.getType());
        assertEquals("Test comment", fullBox.getComment());
        assertEquals(now, fullBox.getDateExported());
    }

    @Test
    @DisplayName("Should set and get bId correctly")
    void setBId_WithValidId_StoresCorrectly() {
        box.setBId("BOX1");
        assertEquals("BOX1", box.getBId());
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void setName_WithValidName_StoresCorrectly() {
        box.setName("Storage Box A");
        assertEquals("Storage Box A", box.getName());
    }

    @Test
    @DisplayName("Should set and get numMax correctly")
    void setNumMax_WithValidNumber_StoresCorrectly() {
        box.setNumMax(96);
        assertEquals(96, box.getNumMax());
    }

    @Test
    @DisplayName("Should set and get type correctly")
    void setType_WithValidType_StoresCorrectly() {
        box.setType(1);
        assertEquals(1, box.getType());
    }

    @Test
    @DisplayName("Should set and get comment correctly")
    void setComment_WithValidComment_StoresCorrectly() {
        box.setComment("High priority samples");
        assertEquals("High priority samples", box.getComment());
    }

    @Test
    @DisplayName("Should set and get dateExported correctly")
    void setDateExported_WithValidDate_StoresCorrectly() {
        LocalDateTime exportDate = LocalDateTime.of(2025, 12, 8, 10, 0);
        box.setDateExported(exportDate);
        assertEquals(exportDate, box.getDateExported());
    }

    @Test
    @DisplayName("Should handle null values for optional fields")
    void setOptionalFields_WithNull_AcceptsNull() {
        box.setName(null);
        box.setComment(null);
        box.setDateExported(null);

        assertNull(box.getName());
        assertNull(box.getComment());
        assertNull(box.getDateExported());
    }

    @Test
    @DisplayName("Should handle max length for bId")
    void setBId_WithMaxLength_StoresCorrectly() {
        String maxId = "ABCD";
        box.setBId(maxId);
        assertEquals(maxId, box.getBId());
        assertEquals(4, box.getBId().length());
    }

    @Test
    @DisplayName("Should handle max length for name")
    void setName_WithMaxLength_StoresCorrectly() {
        String maxName = "a".repeat(255);
        box.setName(maxName);
        assertEquals(maxName, box.getName());
        assertEquals(255, box.getName().length());
    }

    @Test
    @DisplayName("Should handle max length for comment")
    void setComment_WithMaxLength_StoresCorrectly() {
        String maxComment = "a".repeat(255);
        box.setComment(maxComment);
        assertEquals(maxComment, box.getComment());
        assertEquals(255, box.getComment().length());
    }

    @Test
    @DisplayName("Should create empty box with no-args constructor")
    void noArgsConstructor_CreatesEmptyBox() {
        Box emptyBox = new Box();
        assertNotNull(emptyBox);
        assertNull(emptyBox.getBId());
        assertNull(emptyBox.getName());
        assertNull(emptyBox.getNumMax());
        assertNull(emptyBox.getType());
    }

    @Test
    @DisplayName("Should handle zero for numMax")
    void setNumMax_WithZero_StoresZero() {
        box.setNumMax(0);
        assertEquals(0, box.getNumMax());
    }

    @Test
    @DisplayName("Should handle negative type values")
    void setType_WithNegativeValue_StoresCorrectly() {
        box.setType(-1);
        assertEquals(-1, box.getType());
    }

    @Test
    @DisplayName("Should handle large numMax values")
    void setNumMax_WithLargeValue_StoresCorrectly() {
        box.setNumMax(999999);
        assertEquals(999999, box.getNumMax());
    }
}
