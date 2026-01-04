package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oliwier.insyrest.config.TestSecurityConfig;
import com.oliwier.insyrest.dto.request.BoxRequest;
import com.oliwier.insyrest.dto.response.BoxResponse;
import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.mapper.BoxMapper;
import com.oliwier.insyrest.security.JwtService;
import com.oliwier.insyrest.service.BoxService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(BoxController.class)
@DisplayName("BoxController Unit Tests")
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class BoxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private BoxService boxService;

    @MockitoBean
    private BoxMapper boxMapper;

    private ObjectMapper objectMapper;
    private Box testBox;
    private BoxRequest testBoxRequest;
    private BoxResponse testBoxResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testBox = new Box();
        testBox.setBId("BOX1");
        testBox.setName("Test Box");
        testBox.setNumMax(96);
        testBox.setType(1);
        testBox.setComment("Test comment");
        testBox.setDateExported(LocalDateTime.of(2025, 12, 8, 10, 0));

        testBoxRequest = new BoxRequest();
        testBoxRequest.setBId("BOX1");
        testBoxRequest.setName("Test Box");
        testBoxRequest.setNumMax(96);
        testBoxRequest.setType(1);
        testBoxRequest.setComment("Test comment");

        testBoxResponse = new BoxResponse();
        testBoxResponse.setBId("BOX1");
        testBoxResponse.setName("Test Box");
        testBoxResponse.setNumMax(96);
        testBoxResponse.setType(1);
        testBoxResponse.setComment("Test comment");
    }

    @Test
    @DisplayName("GET /api/boxes/{id} - Should return box when found")
    void getBoxById_WhenExists_ReturnsBox() throws Exception {
        when(boxService.findById("BOX1")).thenReturn(Optional.of(testBox));
        when(boxMapper.toResponse(testBox)).thenReturn(testBoxResponse);

        mockMvc.perform(get("/api/boxes/BOX1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bId", is("BOX1")))
                .andExpect(jsonPath("$.name", is("Test Box")))
                .andExpect(jsonPath("$.numMax", is(96)));

        verify(boxService, times(1)).findById("BOX1");
        verify(boxMapper, times(1)).toResponse(testBox);
    }

    @Test
    @DisplayName("GET /api/boxes/{id} - Should return 404 when not found")
    void getBoxById_WhenNotFound_Returns404() throws Exception {
        when(boxService.findById("INVALID")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/boxes/INVALID"))
                .andExpect(status().isNotFound());

        verify(boxService, times(1)).findById("INVALID");
        verify(boxMapper, never()).toResponse(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("GET /api/boxes - Should return all boxes")
    void getAllBoxes_ReturnsBoxesList() throws Exception {
        Box box2 = new Box();
        box2.setBId("BOX2");

        BoxResponse response2 = new BoxResponse();
        response2.setBId("BOX2");

        Page<Box> boxPage = new PageImpl<>(Arrays.asList(testBox, box2));
        Page<BoxResponse> responsePage = new PageImpl<>(Arrays.asList(testBoxResponse, response2));

        when(boxService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(boxPage);

        when(boxMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(responsePage);

        mockMvc.perform(get("/api/boxes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].bId", is("BOX1")))
                .andExpect(jsonPath("$.content[1].bId", is("BOX2")));

        verify(boxService, times(1)).findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        );
    }

    @Test
    @DisplayName("POST /api/boxes - Should create new box")
    void createBox_WithValidData_ReturnsCreated() throws Exception {
        when(boxMapper.toEntity(org.mockito.ArgumentMatchers.any(BoxRequest.class)))
                .thenReturn(testBox);
        when(boxService.save(org.mockito.ArgumentMatchers.any(Box.class)))
                .thenReturn(testBox);
        when(boxMapper.toResponse(testBox)).thenReturn(testBoxResponse);

        mockMvc.perform(post("/api/boxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBoxRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bId", is("BOX1")))
                .andExpect(jsonPath("$.name", is("Test Box")));

        verify(boxService, times(1)).save(org.mockito.ArgumentMatchers.any(Box.class));
    }

    @Test
    @DisplayName("PUT /api/boxes/{id} - Should update existing box")
    void updateBox_WhenExists_ReturnsUpdated() throws Exception {
        when(boxService.findById("BOX1")).thenReturn(Optional.of(testBox));
        doNothing().when(boxMapper).updateEntity(
                org.mockito.ArgumentMatchers.any(Box.class),
                org.mockito.ArgumentMatchers.any(BoxRequest.class)
        );
        when(boxService.save(org.mockito.ArgumentMatchers.any(Box.class)))
                .thenReturn(testBox);
        when(boxMapper.toResponse(testBox)).thenReturn(testBoxResponse);

        mockMvc.perform(put("/api/boxes/BOX1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBoxRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bId", is("BOX1")));

        verify(boxService, times(1)).findById("BOX1");
        verify(boxService, times(1)).save(org.mockito.ArgumentMatchers.any(Box.class));
    }

    @Test
    @DisplayName("DELETE /api/boxes/{id} - Should delete box")
    void deleteBox_WhenExists_ReturnsNoContent() throws Exception {
        when(boxService.findById("BOX1")).thenReturn(Optional.of(testBox));
        doNothing().when(boxService).deleteById("BOX1");

        mockMvc.perform(delete("/api/boxes/BOX1"))
                .andExpect(status().isNoContent());

        verify(boxService, times(1)).deleteById("BOX1");
    }

    @Test
    @DisplayName("PUT /api/boxes/{id} - Should return 404 when not found")
    void updateBox_WhenNotFound_Returns404() throws Exception {
        when(boxService.findById("INVALID")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/boxes/INVALID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBoxRequest)))
                .andExpect(status().isNotFound());

        verify(boxService, times(1)).findById("INVALID");
        verify(boxService, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
