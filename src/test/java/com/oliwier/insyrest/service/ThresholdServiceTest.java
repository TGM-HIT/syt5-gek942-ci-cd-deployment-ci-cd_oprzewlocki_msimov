package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Threshold;
import com.oliwier.insyrest.repository.ThresholdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ThresholdService Unit Tests")
class ThresholdServiceTest {

    @Mock
    private ThresholdRepository thresholdRepository;

    private ThresholdService thresholdService;

    private Threshold testThreshold;

    @BeforeEach
    void setUp() {
        thresholdService = new ThresholdService(thresholdRepository);

        testThreshold = new Threshold();
        testThreshold.setThId("TH001");
        testThreshold.setValueMin(new BigDecimal("10.50"));
        testThreshold.setValueMax(new BigDecimal("100.75"));
        testThreshold.setDateChanged(LocalDateTime.of(2025, 12, 8, 10, 0));
    }

    @Test
    @DisplayName("Should find threshold by ID")
    void findById_WhenThresholdExists_ReturnsThreshold() {
        when(thresholdRepository.findById("TH001")).thenReturn(Optional.of(testThreshold));

        Optional<Threshold> result = thresholdService.findById("TH001");

        assertTrue(result.isPresent());
        assertEquals(testThreshold, result.get());
        verify(thresholdRepository, times(1)).findById("TH001");
    }

    @Test
    @DisplayName("Should return empty when threshold not found")
    void findById_WhenThresholdNotFound_ReturnsEmpty() {
        when(thresholdRepository.findById("TH999")).thenReturn(Optional.empty());

        Optional<Threshold> result = thresholdService.findById("TH999");

        assertFalse(result.isPresent());
        verify(thresholdRepository, times(1)).findById("TH999");
    }

    @Test
    @DisplayName("Should save threshold")
    void save_WithThreshold_SavesAndReturnsThreshold() {
        when(thresholdRepository.save(testThreshold)).thenReturn(testThreshold);

        Threshold result = thresholdService.save(testThreshold);

        assertNotNull(result);
        assertEquals(testThreshold, result);
        verify(thresholdRepository, times(1)).save(testThreshold);
    }

    @Test
    @DisplayName("Should delete threshold by ID")
    void deleteById_WithValidId_DeletesThreshold() {
        doNothing().when(thresholdRepository).deleteById("TH001");

        thresholdService.deleteById("TH001");

        verify(thresholdRepository, times(1)).deleteById("TH001");
    }

    @Test
    @DisplayName("Should find all thresholds with pagination")
    void findAll_WithPageable_ReturnsPageOfThresholds() {
        Threshold threshold2 = new Threshold();
        threshold2.setThId("TH002");

        List<Threshold> thresholds = Arrays.asList(testThreshold, threshold2);
        Page<Threshold> thresholdPage = new PageImpl<>(thresholds);

        when(thresholdRepository.findAll(any(Pageable.class))).thenReturn(thresholdPage);

        Page<Threshold> result = thresholdService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(thresholdRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle empty result set")
    void findAll_WhenNoThresholds_ReturnsEmptyPage() {
        Page<Threshold> emptyPage = Page.empty();

        when(thresholdRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<Threshold> result = thresholdService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}
