package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.ThresholdRequest;
import com.oliwier.insyrest.dto.response.ThresholdResponse;
import com.oliwier.insyrest.entity.Threshold;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ThresholdMapper Unit Tests")
class ThresholdMapperTest {

    private ThresholdMapper thresholdMapper;
    private Threshold testThreshold;
    private ThresholdRequest testThresholdRequest;

    @BeforeEach
    void setUp() {
        thresholdMapper = new ThresholdMapper();

        testThreshold = new Threshold();
        testThreshold.setThId("TH001");
        testThreshold.setValueMin(new BigDecimal("10.50"));
        testThreshold.setValueMax(new BigDecimal("100.75"));
        testThreshold.setDateChanged(LocalDateTime.of(2025, 12, 8, 10, 0));

        testThresholdRequest = new ThresholdRequest();
        testThresholdRequest.setThId("TH001");
        testThresholdRequest.setValueMin(new BigDecimal("10.50"));
        testThresholdRequest.setValueMax(new BigDecimal("100.75"));
        testThresholdRequest.setDateChanged(LocalDateTime.of(2025, 12, 8, 10, 0));
    }

    @Test
    @DisplayName("Should map entity to response with all fields")
    void toResponse_WithFullEntity_MapsAllFields() {
        ThresholdResponse result = thresholdMapper.toResponse(testThreshold);

        assertNotNull(result);
        assertEquals(testThreshold.getThId(), result.getThId());
        assertEquals(testThreshold.getValueMin(), result.getValueMin());
        assertEquals(testThreshold.getValueMax(), result.getValueMax());
        assertEquals(testThreshold.getDateChanged(), result.getDateChanged());
    }

    @Test
    @DisplayName("Should map request to entity with all fields")
    void toEntity_WithValidRequest_MapsAllFields() {
        Threshold result = thresholdMapper.toEntity(testThresholdRequest);

        assertNotNull(result);
        assertEquals(testThresholdRequest.getThId(), result.getThId());
        assertEquals(testThresholdRequest.getValueMin(), result.getValueMin());
        assertEquals(testThresholdRequest.getValueMax(), result.getValueMax());
        assertEquals(testThresholdRequest.getDateChanged(), result.getDateChanged());
    }

    @Test
    @DisplayName("Should update existing entity from request")
    void updateEntity_WithRequest_UpdatesAllFieldsExceptId() {
        String originalId = testThreshold.getThId();

        ThresholdRequest updateRequest = new ThresholdRequest();
        updateRequest.setThId("TH999");
        updateRequest.setValueMin(new BigDecimal("20.00"));
        updateRequest.setValueMax(new BigDecimal("200.00"));
        updateRequest.setDateChanged(LocalDateTime.of(2025, 12, 9, 10, 0));

        thresholdMapper.updateEntity(testThreshold, updateRequest);

        assertEquals(originalId, testThreshold.getThId());
        assertEquals(new BigDecimal("20.00"), testThreshold.getValueMin());
        assertEquals(new BigDecimal("200.00"), testThreshold.getValueMax());
        assertEquals(LocalDateTime.of(2025, 12, 9, 10, 0), testThreshold.getDateChanged());
    }

    @Test
    @DisplayName("Should handle null optional fields in request")
    void toEntity_WithNullOptionalFields_HandlesGracefully() {
        testThresholdRequest.setValueMin(null);
        testThresholdRequest.setValueMax(null);
        testThresholdRequest.setDateChanged(null);

        Threshold result = thresholdMapper.toEntity(testThresholdRequest);

        assertNotNull(result);
        assertEquals("TH001", result.getThId());
        assertNull(result.getValueMin());
        assertNull(result.getValueMax());
        assertNull(result.getDateChanged());
    }

    @Test
    @DisplayName("Should handle null optional fields in entity")
    void toResponse_WithNullOptionalFields_HandlesGracefully() {
        testThreshold.setValueMin(null);
        testThreshold.setValueMax(null);
        testThreshold.setDateChanged(null);

        ThresholdResponse result = thresholdMapper.toResponse(testThreshold);

        assertNotNull(result);
        assertEquals("TH001", result.getThId());
        assertNull(result.getValueMin());
        assertNull(result.getValueMax());
        assertNull(result.getDateChanged());
    }

    @Test
    @DisplayName("Should preserve BigDecimal precision in mapping")
    void toResponse_PreservesBigDecimalPrecision() {
        BigDecimal preciseValue = new BigDecimal("12345.67");
        testThreshold.setValueMin(preciseValue);

        ThresholdResponse result = thresholdMapper.toResponse(testThreshold);

        assertEquals(preciseValue, result.getValueMin());
    }

    @Test
    @DisplayName("Should preserve LocalDateTime in mapping")
    void toResponse_PreservesLocalDateTime() {
        LocalDateTime specificTime = LocalDateTime.of(2025, 12, 8, 14, 30, 45);
        testThreshold.setDateChanged(specificTime);

        ThresholdResponse result = thresholdMapper.toResponse(testThreshold);

        assertEquals(specificTime, result.getDateChanged());
    }

    @Test
    @DisplayName("Should handle negative values in mapping")
    void toEntity_WithNegativeValues_MapsCorrectly() {
        testThresholdRequest.setValueMin(new BigDecimal("-10.00"));
        testThresholdRequest.setValueMax(new BigDecimal("-5.00"));

        Threshold result = thresholdMapper.toEntity(testThresholdRequest);

        assertEquals(new BigDecimal("-10.00"), result.getValueMin());
        assertEquals(new BigDecimal("-5.00"), result.getValueMax());
    }

    @Test
    @DisplayName("Should handle zero values in mapping")
    void toEntity_WithZeroValues_MapsCorrectly() {
        testThresholdRequest.setValueMin(BigDecimal.ZERO);
        testThresholdRequest.setValueMax(BigDecimal.ZERO);

        Threshold result = thresholdMapper.toEntity(testThresholdRequest);

        assertEquals(BigDecimal.ZERO, result.getValueMin());
        assertEquals(BigDecimal.ZERO, result.getValueMax());
    }
}
