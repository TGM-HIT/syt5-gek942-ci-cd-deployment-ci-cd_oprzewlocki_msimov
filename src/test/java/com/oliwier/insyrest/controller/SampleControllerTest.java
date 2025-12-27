package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.config.TestSecurityConfig;
import com.oliwier.insyrest.dto.request.SampleRequest;
import com.oliwier.insyrest.dto.response.SampleResponse;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.mapper.SampleMapper;
import com.oliwier.insyrest.service.*;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(SampleController.class)
@DisplayName("SampleController Unit Tests")
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SampleService sampleService;

    @MockitoBean
    private SampleMapper sampleMapper;

    @MockitoBean
    private AnalysisService analysisService;

    @MockitoBean
    private BoxPosService boxPosService;

    @MockitoBean
    private BoxService boxService;

    @Autowired
    private ObjectMapper objectMapper;

    private Sample testSample;
    private SampleRequest testSampleRequest;
    private SampleResponse testSampleResponse;
    private SampleId testSampleId;

    @BeforeEach
    void setUp() {
        testSampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));

        testSample = new Sample();
        testSample.setId(testSampleId);
        testSample.setName("Test Sample");
        testSample.setWeightNet(new BigDecimal("100.50"));

        testSampleRequest = new SampleRequest();
        testSampleRequest.setS_id("S001");
        testSampleRequest.setS_stamp(LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSampleRequest.setName("Test Sample");
        testSampleRequest.setWeightNet(new BigDecimal("100.50"));

        testSampleResponse = new SampleResponse();
        testSampleResponse.setS_id("S001");
        testSampleResponse.setS_stamp(LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSampleResponse.setName("Test Sample");
        testSampleResponse.setWeightNet(new BigDecimal("100.50"));
    }

    @Test
    @DisplayName("GET /api/samples - Should return all samples")
    void getAllSamples_ReturnsSamplesList() throws Exception {
        Sample sample2 = new Sample();
        SampleResponse response2 = new SampleResponse();

        Page<Sample> samplePage = new PageImpl<>(Arrays.asList(testSample, sample2));
        Page<SampleResponse> responsePage = new PageImpl<>(Arrays.asList(testSampleResponse, response2));

        when(sampleService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(samplePage);

        when(sampleMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(responsePage);

        mockMvc.perform(get("/api/samples"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @DisplayName("POST /api/samples - Should create new sample")
    void createSample_WithValidData_ReturnsCreated() throws Exception {
        when(sampleMapper.toEntity(org.mockito.ArgumentMatchers.any(SampleRequest.class)))
                .thenReturn(testSample);
        when(sampleService.save(org.mockito.ArgumentMatchers.any(Sample.class)))
                .thenReturn(testSample);
        when(sampleMapper.toResponse(testSample)).thenReturn(testSampleResponse);

        mockMvc.perform(post("/api/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.s_id", is("S001")))
                .andExpect(jsonPath("$.name", is("Test Sample")));

        verify(sampleService, times(1)).save(org.mockito.ArgumentMatchers.any(Sample.class));
    }

    @Test
    @DisplayName("POST /api/samples - Should validate required fields")
    void createSample_WithMissingFields_ReturnsBadRequest() throws Exception {
        SampleRequest invalidRequest = new SampleRequest();

        mockMvc.perform(post("/api/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(sampleService, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("GET /api/samples - Should return empty page when no samples")
    void getAllSamples_WhenEmpty_ReturnsEmptyPage() throws Exception {
        Page<Sample> emptyPage = Page.empty();
        Page<SampleResponse> emptyResponsePage = Page.empty();

        when(sampleService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(emptyPage);

        when(sampleMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(emptyResponsePage);

        mockMvc.perform(get("/api/samples"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.empty", is(true)));
    }
}
