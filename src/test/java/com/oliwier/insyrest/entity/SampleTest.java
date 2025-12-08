package com.oliwier.insyrest.entity;

import com.oliwier.insyrest.entity.id.SampleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Sample Entity Unit Tests")
class SampleTest {

    private Sample sample;
    private SampleId sampleId;

    @BeforeEach
    void setUp() {
        sample = new Sample();
        sampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
    }

    @Test
    @DisplayName("Should create sample with all fields")
    void constructor_WithAllFields_SetsAllProperties() {
        Set<BoxPos> positions = new HashSet<>();

        Sample fullSample = new Sample(
                sampleId,
                "Test Sample",
                new BigDecimal("100.50"),
                new BigDecimal("110.50"),
                new BigDecimal("10.00"),
                5,
                new BigDecimal("15.75"),
                LocalDateTime.of(2025, 12, 8, 11, 0),
                "FLAG1",
                3,
                "Test comment",
                LocalDateTime.of(2025, 12, 8, 12, 0),
                positions
        );

        assertEquals(sampleId, fullSample.getId());
        assertEquals("Test Sample", fullSample.getName());
        assertEquals(new BigDecimal("100.50"), fullSample.getWeightNet());
        assertEquals(5, fullSample.getQuantity());
        assertEquals("FLAG1", fullSample.getSFlags());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void setId_WithValidId_StoresCorrectly() {
        sample.setId(sampleId);
        assertEquals(sampleId, sample.getId());
        assertEquals("S001", sample.getId().getsId());
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void setName_WithValidName_StoresCorrectly() {
        sample.setName("Sample Name");
        assertEquals("Sample Name", sample.getName());
    }

    @Test
    @DisplayName("Should set and get weightNet correctly")
    void setWeightNet_WithValidWeight_StoresCorrectly() {
        BigDecimal weight = new BigDecimal("123.45");
        sample.setWeightNet(weight);
        assertEquals(weight, sample.getWeightNet());
    }

    @Test
    @DisplayName("Should set and get weightBru correctly")
    void setWeightBru_WithValidWeight_StoresCorrectly() {
        BigDecimal weight = new BigDecimal("150.00");
        sample.setWeightBru(weight);
        assertEquals(weight, sample.getWeightBru());
    }

    @Test
    @DisplayName("Should set and get weightTar correctly")
    void setWeightTar_WithValidWeight_StoresCorrectly() {
        BigDecimal weight = new BigDecimal("26.55");
        sample.setWeightTar(weight);
        assertEquals(weight, sample.getWeightTar());
    }

    @Test
    @DisplayName("Should set and get quantity correctly")
    void setQuantity_WithValidQuantity_StoresCorrectly() {
        sample.setQuantity(10);
        assertEquals(10, sample.getQuantity());
    }

    @Test
    @DisplayName("Should set and get distance correctly")
    void setDistance_WithValidDistance_StoresCorrectly() {
        BigDecimal distance = new BigDecimal("99.99");
        sample.setDistance(distance);
        assertEquals(distance, sample.getDistance());
    }

    @Test
    @DisplayName("Should set and get dateCrumbled correctly")
    void setDateCrumbled_WithValidDate_StoresCorrectly() {
        LocalDateTime date = LocalDateTime.of(2025, 12, 8, 14, 30);
        sample.setDateCrumbled(date);
        assertEquals(date, sample.getDateCrumbled());
    }

    @Test
    @DisplayName("Should set and get sFlags correctly")
    void setSFlags_WithValidFlags_StoresCorrectly() {
        sample.setSFlags("FLAG123");
        assertEquals("FLAG123", sample.getSFlags());
    }

    @Test
    @DisplayName("Should set and get lane correctly")
    void setLane_WithValidLane_StoresCorrectly() {
        sample.setLane(5);
        assertEquals(5, sample.getLane());
    }

    @Test
    @DisplayName("Should set and get comment correctly")
    void setComment_WithValidComment_StoresCorrectly() {
        sample.setComment("This is a test comment");
        assertEquals("This is a test comment", sample.getComment());
    }

    @Test
    @DisplayName("Should set and get dateExported correctly")
    void setDateExported_WithValidDate_StoresCorrectly() {
        LocalDateTime date = LocalDateTime.of(2025, 12, 8, 15, 0);
        sample.setDateExported(date);
        assertEquals(date, sample.getDateExported());
    }

    @Test
    @DisplayName("Should set and get boxPositions correctly")
    void setBoxPositions_WithValidSet_StoresCorrectly() {
        Set<BoxPos> positions = new HashSet<>();
        BoxPos pos = new BoxPos();
        positions.add(pos);

        sample.setBoxPositions(positions);
        assertEquals(1, sample.getBoxPositions().size());
        assertTrue(sample.getBoxPositions().contains(pos));
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void setOptionalFields_WithNull_AcceptsNull() {
        sample.setName(null);
        sample.setWeightNet(null);
        sample.setQuantity(null);
        sample.setComment(null);
        sample.setDateExported(null);

        assertNull(sample.getName());
        assertNull(sample.getWeightNet());
        assertNull(sample.getQuantity());
        assertNull(sample.getComment());
        assertNull(sample.getDateExported());
    }

    @Test
    @DisplayName("Should create empty sample with no-args constructor")
    void noArgsConstructor_CreatesEmptySample() {
        Sample emptySample = new Sample();
        assertNotNull(emptySample);
        assertNull(emptySample.getId());
        assertNull(emptySample.getName());
    }

    @Test
    @DisplayName("Should handle max length for name")
    void setName_WithMaxLength_StoresCorrectly() {
        String maxName = "a".repeat(500);
        sample.setName(maxName);
        assertEquals(maxName, sample.getName());
        assertEquals(500, sample.getName().length());
    }

    @Test
    @DisplayName("Should handle max length for sFlags")
    void setSFlags_WithMaxLength_StoresCorrectly() {
        String maxFlags = "F".repeat(10);
        sample.setSFlags(maxFlags);
        assertEquals(maxFlags, sample.getSFlags());
        assertEquals(10, sample.getSFlags().length());
    }

    @Test
    @DisplayName("Should handle max length for comment")
    void setComment_WithMaxLength_StoresCorrectly() {
        String maxComment = "c".repeat(1000);
        sample.setComment(maxComment);
        assertEquals(maxComment, sample.getComment());
        assertEquals(1000, sample.getComment().length());
    }

    @Test
    @DisplayName("Should handle BigDecimal precision for weights")
    void setWeights_WithPrecision_StoresCorrectly() {
        BigDecimal preciseWeight = new BigDecimal("12345.67");
        sample.setWeightNet(preciseWeight);
        assertEquals(preciseWeight, sample.getWeightNet());
    }

    @Test
    @DisplayName("Should initialize boxPositions as empty set")
    void boxPositions_DefaultsToEmptySet() {
        Sample newSample = new Sample();
        assertNotNull(newSample.getBoxPositions());
        assertTrue(newSample.getBoxPositions().isEmpty());
        assertEquals(0, newSample.getBoxPositions().size());
    }

}
