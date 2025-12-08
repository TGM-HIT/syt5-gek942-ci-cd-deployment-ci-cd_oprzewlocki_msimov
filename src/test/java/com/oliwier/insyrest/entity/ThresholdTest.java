package com.oliwier.insyrest.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Threshold Entity Unit Tests")
class ThresholdTest {

    private Threshold threshold;

    @BeforeEach
    void setUp() {
        threshold = new Threshold();
    }

    @Test
    @DisplayName("Should create threshold with all fields")
    void constructor_WithAllFields_SetsAllProperties() {
        LocalDateTime dateChanged = LocalDateTime.of(2025, 12, 8, 10, 0);

        Threshold fullThreshold = new Threshold(
                "TH001",
                new BigDecimal("10.50"),
                new BigDecimal("100.75"),
                dateChanged
        );

        assertEquals("TH001", fullThreshold.getThId());
        assertEquals(new BigDecimal("10.50"), fullThreshold.getValueMin());
        assertEquals(new BigDecimal("100.75"), fullThreshold.getValueMax());
        assertEquals(dateChanged, fullThreshold.getDateChanged());
    }

    @Test
    @DisplayName("Should set and get thId correctly")
    void setThId_WithValidId_StoresCorrectly() {
        threshold.setThId("TH123");
        assertEquals("TH123", threshold.getThId());
    }

    @Test
    @DisplayName("Should set and get valueMin correctly")
    void setValueMin_WithValidValue_StoresCorrectly() {
        BigDecimal min = new BigDecimal("5.25");
        threshold.setValueMin(min);
        assertEquals(min, threshold.getValueMin());
    }

    @Test
    @DisplayName("Should set and get valueMax correctly")
    void setValueMax_WithValidValue_StoresCorrectly() {
        BigDecimal max = new BigDecimal("150.00");
        threshold.setValueMax(max);
        assertEquals(max, threshold.getValueMax());
    }

    @Test
    @DisplayName("Should set and get dateChanged correctly")
    void setDateChanged_WithValidDate_StoresCorrectly() {
        LocalDateTime date = LocalDateTime.of(2025, 12, 8, 14, 30);
        threshold.setDateChanged(date);
        assertEquals(date, threshold.getDateChanged());
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void setOptionalFields_WithNull_AcceptsNull() {
        threshold.setValueMin(null);
        threshold.setValueMax(null);
        threshold.setDateChanged(null);

        assertNull(threshold.getValueMin());
        assertNull(threshold.getValueMax());
        assertNull(threshold.getDateChanged());
    }

    @Test
    @DisplayName("Should create empty threshold with no-args constructor")
    void noArgsConstructor_CreatesEmptyThreshold() {
        Threshold emptyThreshold = new Threshold();
        assertNotNull(emptyThreshold);
        assertNull(emptyThreshold.getThId());
        assertNull(emptyThreshold.getValueMin());
        assertNull(emptyThreshold.getValueMax());
    }

    @Test
    @DisplayName("Should handle max length for thId")
    void setThId_WithMaxLength_StoresCorrectly() {
        String maxId = "A".repeat(10);
        threshold.setThId(maxId);
        assertEquals(maxId, threshold.getThId());
        assertEquals(10, threshold.getThId().length());
    }

    @Test
    @DisplayName("Should handle BigDecimal precision for values")
    void setValues_WithPrecision_StoresCorrectly() {
        BigDecimal preciseMin = new BigDecimal("12345.67");
        BigDecimal preciseMax = new BigDecimal("98765.43");

        threshold.setValueMin(preciseMin);
        threshold.setValueMax(preciseMax);

        assertEquals(preciseMin, threshold.getValueMin());
        assertEquals(preciseMax, threshold.getValueMax());
    }

    @Test
    @DisplayName("Should handle negative values")
    void setValues_WithNegativeNumbers_StoresCorrectly() {
        BigDecimal negativeMin = new BigDecimal("-50.00");
        threshold.setValueMin(negativeMin);
        assertEquals(negativeMin, threshold.getValueMin());
    }

    @Test
    @DisplayName("Should handle zero values")
    void setValues_WithZero_StoresCorrectly() {
        BigDecimal zero = BigDecimal.ZERO;
        threshold.setValueMin(zero);
        threshold.setValueMax(zero);

        assertEquals(zero, threshold.getValueMin());
        assertEquals(zero, threshold.getValueMax());
    }

    @Test
    @DisplayName("Should preserve LocalDateTime precision")
    void setDateChanged_PreservesPrecision() {
        LocalDateTime preciseTime = LocalDateTime.of(2025, 12, 8, 14, 30, 45, 123456789);
        threshold.setDateChanged(preciseTime);
        assertEquals(preciseTime, threshold.getDateChanged());
    }

    @Test
    @DisplayName("Should handle valueMin greater than valueMax")
    void setValues_WithMinGreaterThanMax_StoresWithoutValidation() {
        threshold.setValueMin(new BigDecimal("100.00"));
        threshold.setValueMax(new BigDecimal("50.00"));

        assertTrue(threshold.getValueMin().compareTo(threshold.getValueMax()) > 0);
    }
}
