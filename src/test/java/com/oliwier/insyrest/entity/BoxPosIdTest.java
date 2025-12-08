package com.oliwier.insyrest.entity.id;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BoxPosId Composite Key Unit Tests")
class BoxPosIdTest {

    @Test
    @DisplayName("Should create BoxPosId with all fields")
    void constructor_WithAllFields_SetsAllProperties() {
        BoxPosId id = new BoxPosId(5, "BOX1");

        assertEquals(5, id.getBposId());
        assertEquals("BOX1", id.getBId());
    }

    @Test
    @DisplayName("Should set and get bposId correctly")
    void setBposId_WithValidId_StoresCorrectly() {
        BoxPosId id = new BoxPosId();
        id.setBposId(10);
        assertEquals(10, id.getBposId());
    }

    @Test
    @DisplayName("Should set and get bId correctly")
    void setBId_WithValidId_StoresCorrectly() {
        BoxPosId id = new BoxPosId();
        id.setBId("BOX2");
        assertEquals("BOX2", id.getBId());
    }

    @Test
    @DisplayName("Should be equal when all fields match")
    void equals_WithSameValues_ReturnsTrue() {
        BoxPosId id1 = new BoxPosId(1, "BOX1");
        BoxPosId id2 = new BoxPosId(1, "BOX1");

        assertEquals(id1, id2);
    }

    @Test
    @DisplayName("Should not be equal when bposId differs")
    void equals_WithDifferentBposId_ReturnsFalse() {
        BoxPosId id1 = new BoxPosId(1, "BOX1");
        BoxPosId id2 = new BoxPosId(2, "BOX1");

        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("Should not be equal when bId differs")
    void equals_WithDifferentBId_ReturnsFalse() {
        BoxPosId id1 = new BoxPosId(1, "BOX1");
        BoxPosId id2 = new BoxPosId(1, "BOX2");

        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("Should be equal to itself")
    void equals_WithSameInstance_ReturnsTrue() {
        BoxPosId id = new BoxPosId(1, "BOX1");
        assertEquals(id, id);
    }

    @Test
    @DisplayName("Should not be equal to null")
    void equals_WithNull_ReturnsFalse() {
        BoxPosId id = new BoxPosId(1, "BOX1");
        assertNotEquals(null, id);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void equals_WithDifferentClass_ReturnsFalse() {
        BoxPosId id = new BoxPosId(1, "BOX1");
        assertNotEquals("BOX1", id);
    }

    @Test
    @DisplayName("Should have same hashCode when equal")
    void hashCode_WithEqualObjects_ReturnsSameHashCode() {
        BoxPosId id1 = new BoxPosId(1, "BOX1");
        BoxPosId id2 = new BoxPosId(1, "BOX1");

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    @DisplayName("Should have different hashCode when not equal")
    void hashCode_WithDifferentObjects_ReturnsDifferentHashCode() {
        BoxPosId id1 = new BoxPosId(1, "BOX1");
        BoxPosId id2 = new BoxPosId(2, "BOX2");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    @DisplayName("Should handle null fields in equals")
    void equals_WithNullFields_HandlesGracefully() {
        BoxPosId id1 = new BoxPosId(null, null);
        BoxPosId id2 = new BoxPosId(null, null);

        assertEquals(id1, id2);
    }

    @Test
    @DisplayName("Should create empty BoxPosId with no-args constructor")
    void noArgsConstructor_CreatesEmptyId() {
        BoxPosId id = new BoxPosId();
        assertNotNull(id);
        assertNull(id.getBposId());
        assertNull(id.getBId());
    }
}
