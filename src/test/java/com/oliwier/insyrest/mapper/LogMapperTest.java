package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.LogRequest;
import com.oliwier.insyrest.dto.response.LogResponse;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.Log;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.AnalysisRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LogMapper Unit Tests")
class LogMapperTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private AnalysisRepository analysisRepository;

    private LogMapper logMapper;
    private Log testLog;
    private LogRequest testLogRequest;
    private Sample testSample;
    private Analysis testAnalysis;
    private SampleId testSampleId;

    @BeforeEach
    void setUp() {
        logMapper = new LogMapper(sampleRepository, analysisRepository);

        testSampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(testSampleId);

        testAnalysis = new Analysis();
        testAnalysis.setAId(1L);

        testLog = new Log();
        testLog.setLogId(1L);
        testLog.setDateCreated(LocalDateTime.of(2025, 12, 8, 10, 0));
        testLog.setLevel("INFO");
        testLog.setInfo("Test log message");
        testLog.setSample(testSample);
        testLog.setAnalysis(testAnalysis);
        testLog.setDateExported(LocalDateTime.of(2025, 12, 8, 12, 0));

        testLogRequest = new LogRequest();
        testLogRequest.setDateCreated(LocalDateTime.of(2025, 12, 8, 10, 0));
        testLogRequest.setLevel("INFO");
        testLogRequest.setInfo("Test log message");
        testLogRequest.setSId("S001");
        testLogRequest.setSStamp(LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testLogRequest.setAId(1L);
        testLogRequest.setDateExported(LocalDateTime.of(2025, 12, 8, 12, 0));
    }

    @Test
    @DisplayName("Should map entity to response with all fields")
    void toResponse_WithFullEntity_MapsAllFields() {
        LogResponse result = logMapper.toResponse(testLog);

        assertNotNull(result);
        assertEquals(testLog.getLogId(), result.getLogId());
        assertEquals(testLog.getDateCreated(), result.getDateCreated());
        assertEquals(testLog.getLevel(), result.getLevel());
        assertEquals(testLog.getInfo(), result.getInfo());
        assertEquals(testLog.getSample().getId().getsId(), result.getSId());
        assertEquals(testLog.getSample().getId().getsStamp(), result.getSStamp());
        assertEquals(testLog.getAnalysis().getAId(), result.getAId());
        assertEquals(testLog.getDateExported(), result.getDateExported());
    }

    @Test
    @DisplayName("Should map request to entity with repository lookups")
    void toEntity_WithValidRequest_MapsAndLookupsReferences() {
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));
        when(analysisRepository.findById(1L)).thenReturn(Optional.of(testAnalysis));

        Log result = logMapper.toEntity(testLogRequest);

        assertNotNull(result);
        assertEquals(testLogRequest.getDateCreated(), result.getDateCreated());
        assertEquals(testLogRequest.getLevel(), result.getLevel());
        assertEquals(testLogRequest.getInfo(), result.getInfo());
        assertEquals(testSample, result.getSample());
        assertEquals(testAnalysis, result.getAnalysis());
        assertEquals(testLogRequest.getDateExported(), result.getDateExported());

        verify(sampleRepository, times(1)).findById(any(SampleId.class));
        verify(analysisRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should handle missing sample reference")
    void toEntity_WhenSampleNotFound_SetsNullSample() {
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.empty());
        when(analysisRepository.findById(1L)).thenReturn(Optional.of(testAnalysis));

        Log result = logMapper.toEntity(testLogRequest);

        assertNotNull(result);
        assertNull(result.getSample());
        assertEquals(testAnalysis, result.getAnalysis());
    }

    @Test
    @DisplayName("Should handle missing analysis reference")
    void toEntity_WhenAnalysisNotFound_SetsNullAnalysis() {
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));
        when(analysisRepository.findById(1L)).thenReturn(Optional.empty());

        Log result = logMapper.toEntity(testLogRequest);

        assertNotNull(result);
        assertEquals(testSample, result.getSample());
        assertNull(result.getAnalysis());
    }

    @Test
    @DisplayName("Should handle null sample ID in request")
    void toEntity_WithNullSampleId_DoesNotLookupSample() {
        testLogRequest.setSId(null);
        testLogRequest.setSStamp(null);

        when(analysisRepository.findById(1L)).thenReturn(Optional.of(testAnalysis));

        Log result = logMapper.toEntity(testLogRequest);

        assertNotNull(result);
        assertNull(result.getSample());
        verify(sampleRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should handle null analysis ID in request")
    void toEntity_WithNullAnalysisId_DoesNotLookupAnalysis() {
        testLogRequest.setAId(null);

        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        Log result = logMapper.toEntity(testLogRequest);

        assertNotNull(result);
        assertNull(result.getAnalysis());
        verify(analysisRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should update existing entity from request")
    void updateEntity_WithRequest_UpdatesAllFields() {
        Log existingLog = new Log();
        existingLog.setLogId(99L);

        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));
        when(analysisRepository.findById(1L)).thenReturn(Optional.of(testAnalysis));

        logMapper.updateEntity(existingLog, testLogRequest);

        assertEquals(99L, existingLog.getLogId());
        assertEquals(testLogRequest.getDateCreated(), existingLog.getDateCreated());
        assertEquals(testLogRequest.getLevel(), existingLog.getLevel());
        assertEquals(testLogRequest.getInfo(), existingLog.getInfo());
        assertEquals(testSample, existingLog.getSample());
        assertEquals(testAnalysis, existingLog.getAnalysis());
    }

    @Test
    @DisplayName("Should handle entity with null sample in response")
    void toResponse_WithNullSample_HandlesGracefully() {
        testLog.setSample(null);

        LogResponse result = logMapper.toResponse(testLog);

        assertNotNull(result);
        assertNull(result.getSId());
        assertNull(result.getSStamp());
        assertEquals(testLog.getAnalysis().getAId(), result.getAId());
    }

    @Test
    @DisplayName("Should handle entity with null analysis in response")
    void toResponse_WithNullAnalysis_HandlesGracefully() {
        testLog.setAnalysis(null);

        LogResponse result = logMapper.toResponse(testLog);

        assertNotNull(result);
        assertNull(result.getAId());
        assertEquals(testLog.getSample().getId().getsId(), result.getSId());
    }

    @Test
    @DisplayName("Should preserve LocalDateTime in mapping")
    void toResponse_PreservesLocalDateTime() {
        LocalDateTime specificTime = LocalDateTime.of(2025, 12, 8, 14, 30, 45);
        testLog.setDateCreated(specificTime);

        LogResponse result = logMapper.toResponse(testLog);

        assertEquals(specificTime, result.getDateCreated());
    }

    @Test
    @DisplayName("Should handle different log levels")
    void toEntity_WithDifferentLevels_MapsCorrectly() {
        String[] levels = {"DEBUG", "INFO", "WARN", "ERROR"};

        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));
        when(analysisRepository.findById(1L)).thenReturn(Optional.of(testAnalysis));

        for (String level : levels) {
            testLogRequest.setLevel(level);
            Log result = logMapper.toEntity(testLogRequest);
            assertEquals(level, result.getLevel());
        }
    }
}
