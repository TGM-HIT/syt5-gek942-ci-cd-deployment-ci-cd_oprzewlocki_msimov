package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.AnalysisRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnalysisService Unit Tests")
class AnalysisServiceTest {

    @Mock
    private AnalysisRepository analysisRepository;

    @Mock
    private SampleRepository sampleRepository;

    @InjectMocks
    private AnalysisService analysisService;

    private Sample testSample;
    private SampleId testSampleId;
    private Analysis testAnalysis;

    @BeforeEach
    void setUp() {
        testSampleId = new SampleId("TEST123", LocalDateTime.of(2025, 12, 4, 10, 0, 0));

        testSample = new Sample();
        testSample.setId(testSampleId);
        testSample.setName("Test Sample");
        testSample.setWeightNet(new BigDecimal("100.50"));

        testAnalysis = new Analysis();
        testAnalysis.setAId(1L);
        testAnalysis.setSample(testSample);
        testAnalysis.setPol(new BigDecimal("15.50"));
        testAnalysis.setNat(new BigDecimal("10.20"));
        testAnalysis.setKal(new BigDecimal("5.30"));
        testAnalysis.setDateIn(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should delete all analyses when sample exists")
    void deleteAllBySample_WhenSampleExists_DeletesAllAnalyses() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        doNothing().when(analysisRepository).deleteAllBySample(testSample);

        analysisService.deleteAllBySample(testSampleId);

        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(analysisRepository, times(1)).deleteAllBySample(testSample);
    }

    @Test
    @DisplayName("Should not call deleteAllBySample when sample does not exist")
    void deleteAllBySample_WhenSampleDoesNotExist_DoesNothing() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.empty());

        analysisService.deleteAllBySample(testSampleId);

        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(analysisRepository, never()).deleteAllBySample(any());
    }

    @Test
    @DisplayName("Should detach sample references when sample exists")
    void detachSampleReferences_WhenSampleExists_DetachesAndSaves() {
        Analysis analysis1 = new Analysis();
        analysis1.setAId(1L);
        analysis1.setSample(testSample);

        Analysis analysis2 = new Analysis();
        analysis2.setAId(2L);
        analysis2.setSample(testSample);

        List<Analysis> analyses = Arrays.asList(analysis1, analysis2);

        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(analysisRepository.findBySample(testSample)).thenReturn(analyses);
        when(analysisRepository.saveAll(analyses)).thenReturn(analyses);

        analysisService.detachSampleReferences(testSampleId);

        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(analysisRepository, times(1)).findBySample(testSample);
        verify(analysisRepository, times(1)).saveAll(analyses);

        assertNull(analysis1.getSample());
        assertNull(analysis2.getSample());
    }

    @Test
    @DisplayName("Should not detach references when sample does not exist")
    void detachSampleReferences_WhenSampleDoesNotExist_DoesNothing() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.empty());

        analysisService.detachSampleReferences(testSampleId);

        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(analysisRepository, never()).findBySample(any());
        verify(analysisRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Should handle empty analysis list when detaching references")
    void detachSampleReferences_WithEmptyList_HandlesGracefully() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(analysisRepository.findBySample(testSample)).thenReturn(Collections.emptyList());

        analysisService.detachSampleReferences(testSampleId);

        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(analysisRepository, times(1)).findBySample(testSample);
        verify(analysisRepository, times(1)).saveAll(Collections.emptyList());
    }

    @Test
    @DisplayName("Should return true when analyses exist for sample")
    void existsForSample_WhenAnalysesExist_ReturnsTrue() {
        when(analysisRepository.existsBySampleId(testSampleId)).thenReturn(true);

        boolean result = analysisService.existsForSample(testSampleId);

        assertTrue(result);
        verify(analysisRepository, times(1)).existsBySampleId(testSampleId);
    }

    @Test
    @DisplayName("Should return false when no analyses exist for sample")
    void existsForSample_WhenNoAnalysesExist_ReturnsFalse() {
        when(analysisRepository.existsBySampleId(testSampleId)).thenReturn(false);

        boolean result = analysisService.existsForSample(testSampleId);

        assertFalse(result);
        verify(analysisRepository, times(1)).existsBySampleId(testSampleId);
    }

    @Test
    @DisplayName("Should handle null sample ID gracefully")
    void deleteAllBySample_WithNullId_HandlesGracefully() {
        when(sampleRepository.findById(null)).thenReturn(Optional.empty());

        analysisService.deleteAllBySample(null);

        verify(analysisRepository, never()).deleteAllBySample(any());
    }

    @Test
    @DisplayName("Should preserve analysis properties when detaching sample")
    void detachSampleReferences_PreservesAnalysisProperties() {
        Analysis analysis = new Analysis();
        analysis.setAId(1L);
        analysis.setSample(testSample);
        analysis.setPol(new BigDecimal("15.50"));
        analysis.setNat(new BigDecimal("10.20"));
        analysis.setComment("Test comment");

        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(analysisRepository.findBySample(testSample)).thenReturn(List.of(analysis));
        when(analysisRepository.saveAll(anyList())).thenReturn(List.of(analysis));

        analysisService.detachSampleReferences(testSampleId);

        assertNull(analysis.getSample());
        assertEquals(new BigDecimal("15.50"), analysis.getPol());
        assertEquals(new BigDecimal("10.20"), analysis.getNat());
        assertEquals("Test comment", analysis.getComment());
    }

    @Test
    @DisplayName("Should handle composite key correctly")
    void deleteAllBySample_WithCompositeKey_WorksCorrectly() {
        SampleId differentId = new SampleId("TEST456", LocalDateTime.of(2025, 12, 5, 15, 30, 0));
        Sample differentSample = new Sample();
        differentSample.setId(differentId);

        when(sampleRepository.findById(differentId)).thenReturn(Optional.of(differentSample));
        doNothing().when(analysisRepository).deleteAllBySample(differentSample);

        analysisService.deleteAllBySample(differentId);

        verify(sampleRepository, times(1)).findById(differentId);
        verify(analysisRepository, times(1)).deleteAllBySample(differentSample);
    }

    @Test
    @DisplayName("Should correctly check existence with composite key")
    void existsForSample_WithCompositeKey_ChecksCorrectly() {
        SampleId compositeId = new SampleId("COMP789", LocalDateTime.of(2025, 12, 4, 14, 0, 0));
        when(analysisRepository.existsBySampleId(compositeId)).thenReturn(true);

        boolean result = analysisService.existsForSample(compositeId);

        assertTrue(result);
        verify(analysisRepository, times(1)).existsBySampleId(compositeId);
    }
}
