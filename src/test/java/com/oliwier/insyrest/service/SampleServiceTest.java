package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.SampleRepository;
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
@DisplayName("SampleService Unit Tests")
class SampleServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    private SampleService sampleService;

    private Sample testSample;
    private SampleId testSampleId;

    @BeforeEach
    void setUp() {
        sampleService = new SampleService(sampleRepository);

        testSampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(testSampleId);
        testSample.setName("Test Sample");
        testSample.setWeightNet(new BigDecimal("100.50"));
    }

    @Test
    @DisplayName("Should find sample by ID")
    void findById_WhenSampleExists_ReturnsSample() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));

        Optional<Sample> result = sampleService.findById(testSampleId);

        assertTrue(result.isPresent());
        assertEquals(testSample, result.get());
        verify(sampleRepository, times(1)).findById(testSampleId);
    }

    @Test
    @DisplayName("Should return empty when sample not found")
    void findById_WhenSampleNotFound_ReturnsEmpty() {
        SampleId nonExistentId = new SampleId("S999", LocalDateTime.now());
        when(sampleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<Sample> result = sampleService.findById(nonExistentId);

        assertFalse(result.isPresent());
        verify(sampleRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should save sample")
    void save_WithSample_SavesAndReturnsSample() {
        when(sampleRepository.save(testSample)).thenReturn(testSample);

        Sample result = sampleService.save(testSample);

        assertNotNull(result);
        assertEquals(testSample, result);
        verify(sampleRepository, times(1)).save(testSample);
    }

    @Test
    @DisplayName("Should delete sample by ID")
    void deleteById_WithValidId_DeletesSample() {
        doNothing().when(sampleRepository).deleteById(testSampleId);

        sampleService.deleteById(testSampleId);

        verify(sampleRepository, times(1)).deleteById(testSampleId);
    }

    @Test
    @DisplayName("Should find all samples with pagination")
    void findAll_WithPageable_ReturnsPageOfSamples() {
        Sample sample2 = new Sample();
        SampleId id2 = new SampleId("S002", LocalDateTime.of(2025, 12, 8, 11, 0, 0));
        sample2.setId(id2);

        List<Sample> samples = Arrays.asList(testSample, sample2);
        Page<Sample> samplePage = new PageImpl<>(samples);

        when(sampleRepository.findAll(any(Pageable.class))).thenReturn(samplePage);

        Page<Sample> result = sampleService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(sampleRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle empty result set")
    void findAll_WhenNoSamples_ReturnsEmptyPage() {
        Page<Sample> emptyPage = Page.empty();

        when(sampleRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<Sample> result = sampleService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Should handle composite key correctly")
    void findById_WithCompositeKey_WorksCorrectly() {
        LocalDateTime timestamp = LocalDateTime.of(2025, 12, 8, 14, 30);
        SampleId compositeId = new SampleId("S123", timestamp);
        Sample sampleWithCompositeId = new Sample();
        sampleWithCompositeId.setId(compositeId);

        when(sampleRepository.findById(compositeId)).thenReturn(Optional.of(sampleWithCompositeId));

        Optional<Sample> result = sampleService.findById(compositeId);

        assertTrue(result.isPresent());
        assertEquals("S123", result.get().getId().getsId());
        assertEquals(timestamp, result.get().getId().getsStamp());
    }
}
