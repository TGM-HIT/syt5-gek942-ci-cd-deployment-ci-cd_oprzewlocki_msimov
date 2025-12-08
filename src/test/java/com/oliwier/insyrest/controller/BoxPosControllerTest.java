package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oliwier.insyrest.dto.request.BoxPosRequest;
import com.oliwier.insyrest.dto.response.BoxPosResponse;
import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.BoxPosId;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.mapper.BoxPosMapper;
import com.oliwier.insyrest.service.BoxPosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(BoxPosController.class)
@DisplayName("BoxPosController Unit Tests")
class BoxPosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoxPosService boxPosService;

    @MockitoBean
    private BoxPosMapper boxPosMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private BoxPos testBoxPos;
    private BoxPosRequest testBoxPosRequest;
    private BoxPosResponse testBoxPosResponse;
    private BoxPosId testBoxPosId;

    @BeforeEach
    void setUp() {
        testBoxPosId = new BoxPosId(1, "BOX1");

        Box testBox = new Box();
        testBox.setBId("BOX1");

        SampleId sampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        Sample testSample = new Sample();
        testSample.setId(sampleId);

        testBoxPos = new BoxPos();
        testBoxPos.setId(testBoxPosId);
        testBoxPos.setBox(testBox);
        testBoxPos.setSample(testSample);
        testBoxPos.setDateExported(LocalDateTime.of(2025, 12, 8, 12, 0));

        testBoxPosRequest = new BoxPosRequest();
        testBoxPosRequest.setBposId(1);
        testBoxPosRequest.setBId("BOX1");
        testBoxPosRequest.setSId("S001");
        testBoxPosRequest.setSStamp(LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testBoxPosRequest.setDateExported(LocalDateTime.of(2025, 12, 8, 12, 0));

        testBoxPosResponse = new BoxPosResponse();
        testBoxPosResponse.setBposId(1);
        testBoxPosResponse.setBId("BOX1");
        testBoxPosResponse.setSId("S001");
        testBoxPosResponse.setSStamp(LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testBoxPosResponse.setDateExported(LocalDateTime.of(2025, 12, 8, 12, 0));
    }

    @Test
    @DisplayName("GET /api/boxpos - Should return all box positions")
    void getAllBoxPos_ReturnsBoxPosList() throws Exception {
        BoxPos boxPos2 = new BoxPos();
        BoxPosResponse response2 = new BoxPosResponse();

        Page<BoxPos> boxPosPage = new PageImpl<>(Arrays.asList(testBoxPos, boxPos2));
        Page<BoxPosResponse> responsePage = new PageImpl<>(Arrays.asList(testBoxPosResponse, response2));

        when(boxPosService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(boxPosPage);

        when(boxPosMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(responsePage);

        mockMvc.perform(get("/api/boxpos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @DisplayName("POST /api/boxpos - Should create new box position")
    void createBoxPos_WithValidData_ReturnsCreated() throws Exception {
        when(boxPosMapper.toEntity(org.mockito.ArgumentMatchers.any(BoxPosRequest.class)))
                .thenReturn(testBoxPos);
        when(boxPosService.save(org.mockito.ArgumentMatchers.any(BoxPos.class)))
                .thenReturn(testBoxPos);
        when(boxPosMapper.toResponse(testBoxPos)).thenReturn(testBoxPosResponse);

        mockMvc.perform(post("/api/boxpos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBoxPosRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bposId", is(1)))
                .andExpect(jsonPath("$.bId", is("BOX1")));

        verify(boxPosService, times(1)).save(org.mockito.ArgumentMatchers.any(BoxPos.class));
    }

    @Test
    @DisplayName("GET /api/boxpos - Should return empty page when no box positions")
    void getAllBoxPos_WhenEmpty_ReturnsEmptyPage() throws Exception {
        Page<BoxPos> emptyPage = Page.empty();
        Page<BoxPosResponse> emptyResponsePage = Page.empty();

        when(boxPosService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(emptyPage);

        when(boxPosMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(emptyResponsePage);

        mockMvc.perform(get("/api/boxpos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.empty", is(true)));
    }

    @Test
    @DisplayName("POST /api/boxpos - Should validate required fields")
    void createBoxPos_WithMissingFields_ReturnsBadRequest() throws Exception {
        BoxPosRequest invalidRequest = new BoxPosRequest();

        mockMvc.perform(post("/api/boxpos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(boxPosService, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
