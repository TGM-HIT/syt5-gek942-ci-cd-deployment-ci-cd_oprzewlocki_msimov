package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oliwier.insyrest.config.TestSecurityConfig;
import com.oliwier.insyrest.dto.request.AnalysisRequest;
import com.oliwier.insyrest.dto.response.AnalysisResponse;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.mapper.AnalysisMapper;
import com.oliwier.insyrest.security.JwtService;
import com.oliwier.insyrest.service.AnalysisService;
import com.oliwier.insyrest.service.SampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(AnalysisController.class)
@DisplayName("AnalysisController Unit Tests")
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class AnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AnalysisService analysisService;

    @MockitoBean
    private AnalysisMapper analysisMapper;

    @MockitoBean
    private SampleService sampleService;

    private ObjectMapper objectMapper;
    private Analysis testAnalysis;
    private AnalysisRequest testAnalysisRequest;
    private AnalysisResponse testAnalysisResponse;
    private Sample testSample;
    private SampleId testSampleId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testSampleId = new SampleId("CTRL123", LocalDateTime.of(2025, 12, 4, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(testSampleId);
        testSample.setName("Controller Test Sample");
        testSample.setBoxPositions(new HashSet<>());

        testAnalysis = new Analysis();
        testAnalysis.setAId(1L);
        testAnalysis.setSample(testSample);
        testAnalysis.setPol(new BigDecimal("15.50"));
        testAnalysis.setNat(new BigDecimal("10.20"));
        testAnalysis.setDateIn(LocalDateTime.of(2025, 12, 4, 10, 0, 0));
        testAnalysis.setComment("Test comment");

        testAnalysisRequest = new AnalysisRequest();
        testAnalysisRequest.setSId("CTRL123");
        testAnalysisRequest.setSStamp(LocalDateTime.of(2025, 12, 4, 10, 0, 0));
        testAnalysisRequest.setPol(new BigDecimal("15.50"));
        testAnalysisRequest.setNat(new BigDecimal("10.20"));
        testAnalysisRequest.setComment("Test comment");

        testAnalysisResponse = new AnalysisResponse();
        testAnalysisResponse.setAId(1L);
        testAnalysisResponse.setSId("CTRL123");
        testAnalysisResponse.setSStamp(LocalDateTime.of(2025, 12, 4, 10, 0, 0));
        testAnalysisResponse.setPol(new BigDecimal("15.50"));
        testAnalysisResponse.setNat(new BigDecimal("10.20"));
        testAnalysisResponse.setComment("Test comment");
        testAnalysisResponse.setBoxposString("-");
    }

    @Test
    @DisplayName("GET /api/analysis/{id} - Should return analysis when found")
    void getAnalysisById_WhenExists_ReturnsAnalysis() throws Exception {
        when(analysisService.findById(1L)).thenReturn(Optional.of(testAnalysis));
        when(analysisMapper.toResponse(testAnalysis)).thenReturn(testAnalysisResponse);

        mockMvc.perform(get("/api/analysis/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.aId", is(1)))
                .andExpect(jsonPath("$.pol", is(15.50)))
                .andExpect(jsonPath("$.nat", is(10.20)))
                .andExpect(jsonPath("$.comment", is("Test comment")));

        verify(analysisService, times(1)).findById(1L);
        verify(analysisMapper, times(1)).toResponse(testAnalysis);
    }

    @Test
    @DisplayName("GET /api/analysis/{id} - Should return 404 when not found")
    void getAnalysisById_WhenNotFound_Returns404() throws Exception {
        when(analysisService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/analysis/999"))
                .andExpect(status().isNotFound());

        verify(analysisService, times(1)).findById(999L);
        verify(analysisMapper, never()).toResponse(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("GET /api/analysis - Should return all analyses")
    void getAllAnalyses_ReturnsAnalysesList() throws Exception {
        SampleId id1 = new SampleId("S001", LocalDateTime.of(2025, 12, 1, 10, 0, 0));
        Sample sample1 = new Sample();
        sample1.setId(id1);
        sample1.setBoxPositions(new HashSet<>());

        Analysis analysis1 = new Analysis();
        analysis1.setAId(1L);
        analysis1.setSample(sample1);

        Analysis analysis2 = new Analysis();
        analysis2.setAId(2L);

        AnalysisResponse response1 = new AnalysisResponse();
        response1.setAId(1L);
        AnalysisResponse response2 = new AnalysisResponse();
        response2.setAId(2L);

        List<Analysis> analysisList = Arrays.asList(analysis1, analysis2);
        Page<Analysis> analysisPage = new PageImpl<>(analysisList);

        Page<AnalysisResponse> responsePage = new PageImpl<>(Arrays.asList(response1, response2));

        when(analysisService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(analysisPage);

        when(analysisMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(responsePage);

        mockMvc.perform(get("/api/analysis"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].aId", is(1)))
                .andExpect(jsonPath("$.content[1].aId", is(2)))
                .andExpect(jsonPath("$.total_elements", is(2)))
                .andExpect(jsonPath("$.total_pages", is(1)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.last", is(true)))
                .andExpect(jsonPath("$.empty", is(false)));

        verify(analysisService, times(1)).findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        );
    }



    @Test
    @DisplayName("POST /api/analysis - Should create new analysis")
    void createAnalysis_WithValidData_ReturnsCreated() throws Exception {
        when(analysisMapper.toEntity(org.mockito.ArgumentMatchers.any(AnalysisRequest.class)))
                .thenReturn(testAnalysis);
        when(analysisService.save(org.mockito.ArgumentMatchers.any(Analysis.class)))
                .thenReturn(testAnalysis);
        when(analysisMapper.toResponse(testAnalysis)).thenReturn(testAnalysisResponse);

        mockMvc.perform(post("/api/analysis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAnalysisRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.aId", is(1)))
                .andExpect(jsonPath("$.pol", is(15.50)));

        verify(analysisService, times(1)).save(org.mockito.ArgumentMatchers.any(Analysis.class));
    }

    @Test
    @DisplayName("PUT /api/analysis/{id} - Should update existing analysis")
    void updateAnalysis_WhenExists_ReturnsUpdated() throws Exception {
        when(analysisService.findById(1L)).thenReturn(Optional.of(testAnalysis));
        doNothing().when(analysisMapper).updateEntity(
                org.mockito.ArgumentMatchers.any(Analysis.class),
                org.mockito.ArgumentMatchers.any(AnalysisRequest.class)
        );
        when(analysisService.save(org.mockito.ArgumentMatchers.any(Analysis.class)))
                .thenReturn(testAnalysis);
        when(analysisMapper.toResponse(testAnalysis)).thenReturn(testAnalysisResponse);

        mockMvc.perform(put("/api/analysis/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAnalysisRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aId", is(1)));

        verify(analysisService, times(1)).findById(1L);
        verify(analysisMapper, times(1)).updateEntity(eq(testAnalysis),
                org.mockito.ArgumentMatchers.any(AnalysisRequest.class));
        verify(analysisService, times(1)).save(org.mockito.ArgumentMatchers.any(Analysis.class));
    }

    @Test
    @DisplayName("DELETE /api/analysis/{id} - Should delete analysis")
    void deleteAnalysis_WhenExists_ReturnsNoContent() throws Exception {
        when(analysisService.findById(1L)).thenReturn(Optional.of(testAnalysis));
        doNothing().when(analysisService).deleteById(1L);

        mockMvc.perform(delete("/api/analysis/1"))
                .andExpect(status().isNoContent());

        verify(analysisService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("GET /api/analysis/validate-sample - Should validate existing sample")
    void validateSample_WhenExists_ReturnsValid() throws Exception {
        when(sampleService.findById(org.mockito.ArgumentMatchers.any(SampleId.class)))
                .thenReturn(Optional.of(testSample));

        mockMvc.perform(get("/api/analysis/validate-sample")
                        .param("s_id", "CTRL123")
                        .param("s_stamp", "2025-12-04T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid", is(true)));

        verify(sampleService, times(1)).findById(org.mockito.ArgumentMatchers.any(SampleId.class));
    }

    @Test
    @DisplayName("GET /api/analysis/validate-sample - Should return invalid for non-existent sample")
    void validateSample_WhenNotExists_ReturnsInvalid() throws Exception {
        when(sampleService.findById(org.mockito.ArgumentMatchers.any(SampleId.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/analysis/validate-sample")
                        .param("s_id", "INVALID")
                        .param("s_stamp", "2025-12-04T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid", is(false)));
    }

    @Test
    @DisplayName("GET /api/analysis/validate-sample - Should return error for invalid timestamp")
    void validateSample_WithInvalidTimestamp_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/analysis/validate-sample")
                        .param("s_id", "TEST123")
                        .param("s_stamp", "invalid-timestamp"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Invalid timestamp format")));
    }

    @Test
    @DisplayName("PUT /api/analysis/{id} - Should return 404 when not found")
    void updateAnalysis_WhenNotFound_Returns404() throws Exception {
        when(analysisService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/analysis/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAnalysisRequest)))
                .andExpect(status().isNotFound());

        verify(analysisService, times(1)).findById(999L);
        verify(analysisService, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
