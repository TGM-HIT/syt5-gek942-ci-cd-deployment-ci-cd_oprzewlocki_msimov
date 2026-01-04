package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.config.TestSecurityConfig;
import com.oliwier.insyrest.dto.request.ThresholdRequest;
import com.oliwier.insyrest.dto.response.ThresholdResponse;
import com.oliwier.insyrest.entity.Threshold;
import com.oliwier.insyrest.mapper.ThresholdMapper;
import com.oliwier.insyrest.security.JwtService;
import com.oliwier.insyrest.service.ThresholdService;
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

@WebMvcTest(ThresholdController.class)
@DisplayName("ThresholdController Unit Tests")
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ThresholdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private ThresholdService thresholdService;

    @MockitoBean
    private ThresholdMapper thresholdMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Threshold testThreshold;
    private ThresholdRequest testThresholdRequest;
    private ThresholdResponse testThresholdResponse;

    @BeforeEach
    void setUp() {
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

        testThresholdResponse = new ThresholdResponse();
        testThresholdResponse.setThId("TH001");
        testThresholdResponse.setValueMin(new BigDecimal("10.50"));
        testThresholdResponse.setValueMax(new BigDecimal("100.75"));
        testThresholdResponse.setDateChanged(LocalDateTime.of(2025, 12, 8, 10, 0));
    }

    @Test
    @DisplayName("GET /api/thresholds/{id} - Should return threshold when found")
    void getThresholdById_WhenExists_ReturnsThreshold() throws Exception {
        when(thresholdService.findById("TH001")).thenReturn(Optional.of(testThreshold));
        when(thresholdMapper.toResponse(testThreshold)).thenReturn(testThresholdResponse);

        mockMvc.perform(get("/api/thresholds/TH001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.thId", is("TH001")))
                .andExpect(jsonPath("$.valueMin", is(10.50)))
                .andExpect(jsonPath("$.valueMax", is(100.75)));

        verify(thresholdService, times(1)).findById("TH001");
        verify(thresholdMapper, times(1)).toResponse(testThreshold);
    }

    @Test
    @DisplayName("GET /api/thresholds/{id} - Should return 404 when not found")
    void getThresholdById_WhenNotFound_Returns404() throws Exception {
        when(thresholdService.findById("TH999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/thresholds/TH999"))
                .andExpect(status().isNotFound());

        verify(thresholdService, times(1)).findById("TH999");
        verify(thresholdMapper, never()).toResponse(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("GET /api/thresholds - Should return all thresholds")
    void getAllThresholds_ReturnsThresholdsList() throws Exception {
        Threshold threshold2 = new Threshold();
        threshold2.setThId("TH002");

        ThresholdResponse response2 = new ThresholdResponse();
        response2.setThId("TH002");

        Page<Threshold> thresholdPage = new PageImpl<>(Arrays.asList(testThreshold, threshold2));
        Page<ThresholdResponse> responsePage = new PageImpl<>(Arrays.asList(testThresholdResponse, response2));

        when(thresholdService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(thresholdPage);

        when(thresholdMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(responsePage);

        mockMvc.perform(get("/api/thresholds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].thId", is("TH001")))
                .andExpect(jsonPath("$.content[1].thId", is("TH002")));
    }

    @Test
    @DisplayName("POST /api/thresholds - Should create new threshold")
    void createThreshold_WithValidData_ReturnsCreated() throws Exception {
        when(thresholdMapper.toEntity(org.mockito.ArgumentMatchers.any(ThresholdRequest.class)))
                .thenReturn(testThreshold);
        when(thresholdService.save(org.mockito.ArgumentMatchers.any(Threshold.class)))
                .thenReturn(testThreshold);
        when(thresholdMapper.toResponse(testThreshold)).thenReturn(testThresholdResponse);

        mockMvc.perform(post("/api/thresholds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testThresholdRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.thId", is("TH001")))
                .andExpect(jsonPath("$.valueMin", is(10.50)));

        verify(thresholdService, times(1)).save(org.mockito.ArgumentMatchers.any(Threshold.class));
    }

    @Test
    @DisplayName("PUT /api/thresholds/{id} - Should update existing threshold")
    void updateThreshold_WhenExists_ReturnsUpdated() throws Exception {
        when(thresholdService.findById("TH001")).thenReturn(Optional.of(testThreshold));
        doNothing().when(thresholdMapper).updateEntity(
                org.mockito.ArgumentMatchers.any(Threshold.class),
                org.mockito.ArgumentMatchers.any(ThresholdRequest.class)
        );
        when(thresholdService.save(org.mockito.ArgumentMatchers.any(Threshold.class)))
                .thenReturn(testThreshold);
        when(thresholdMapper.toResponse(testThreshold)).thenReturn(testThresholdResponse);

        mockMvc.perform(put("/api/thresholds/TH001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testThresholdRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.thId", is("TH001")));

        verify(thresholdService, times(1)).findById("TH001");
        verify(thresholdService, times(1)).save(org.mockito.ArgumentMatchers.any(Threshold.class));
    }

    @Test
    @DisplayName("DELETE /api/thresholds/{id} - Should delete threshold")
    void deleteThreshold_WhenExists_ReturnsNoContent() throws Exception {
        when(thresholdService.findById("TH001")).thenReturn(Optional.of(testThreshold));
        doNothing().when(thresholdService).deleteById("TH001");

        mockMvc.perform(delete("/api/thresholds/TH001"))
                .andExpect(status().isNoContent());

        verify(thresholdService, times(1)).deleteById("TH001");
    }

    @Test
    @DisplayName("PUT /api/thresholds/{id} - Should return 404 when not found")
    void updateThreshold_WhenNotFound_Returns404() throws Exception {
        when(thresholdService.findById("TH999")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/thresholds/TH999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testThresholdRequest)))
                .andExpect(status().isNotFound());

        verify(thresholdService, times(1)).findById("TH999");
        verify(thresholdService, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("POST /api/thresholds - Should validate required fields")
    void createThreshold_WithMissingFields_ReturnsBadRequest() throws Exception {
        ThresholdRequest invalidRequest = new ThresholdRequest();

        mockMvc.perform(post("/api/thresholds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(thresholdService, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("GET /api/thresholds - Should return empty page when no thresholds")
    void getAllThresholds_WhenEmpty_ReturnsEmptyPage() throws Exception {
        Page<Threshold> emptyPage = Page.empty();
        Page<ThresholdResponse> emptyResponsePage = Page.empty();

        when(thresholdService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(emptyPage);

        when(thresholdMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(emptyResponsePage);

        mockMvc.perform(get("/api/thresholds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.empty", is(true)));
    }
}
