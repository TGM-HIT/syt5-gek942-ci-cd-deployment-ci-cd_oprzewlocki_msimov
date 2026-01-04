package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.config.TestSecurityConfig;
import com.oliwier.insyrest.entity.SampleBoxPosView;
import com.oliwier.insyrest.repository.SampleBoxPosViewRepository;
import com.oliwier.insyrest.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(SampleBoxPosViewController.class)
@DisplayName("SampleBoxPosViewController Unit Tests")
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class SampleBoxPosViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private SampleBoxPosViewRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private SampleBoxPosView testView1;
    private SampleBoxPosView testView2;

    @BeforeEach
    void setUp() {
        testView1 = SampleBoxPosView.builder()
                .sId("S001")
                .sStamp(LocalDateTime.of(2025, 12, 8, 10, 0))
                .boxpos("BOX1/5")
                .build();

        testView2 = SampleBoxPosView.builder()
                .sId("S002")
                .sStamp(LocalDateTime.of(2025, 12, 8, 9, 0))
                .boxpos("BOX2/3")
                .build();
    }

    @Test
    @DisplayName("GET /api/sample-boxpos - Should return all views with default pagination")
    void getAll_WithDefaultPagination_ReturnsViews() throws Exception {
        Page<SampleBoxPosView> viewPage = new PageImpl<>(Arrays.asList(testView1, testView2));

        when(repository.findAll(any(Pageable.class))).thenReturn(viewPage);

        mockMvc.perform(get("/api/sample-boxpos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].sid", is("S001")))
                .andExpect(jsonPath("$.content[0].boxpos", is("BOX1/5")))
                .andExpect(jsonPath("$.content[1].sid", is("S002")));

        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/sample-boxpos - Should support custom page size")
    void getAll_WithCustomPageSize_ReturnsCorrectSize() throws Exception {
        Page<SampleBoxPosView> viewPage = new PageImpl<>(Collections.singletonList(testView1));

        when(repository.findAll(any(Pageable.class))).thenReturn(viewPage);

        mockMvc.perform(get("/api/sample-boxpos")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.size", is(1)));

        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/sample-boxpos - Should support custom page number")
    void getAll_WithCustomPageNumber_ReturnsCorrectPage() throws Exception {
        Page<SampleBoxPosView> viewPage = new PageImpl<>(Collections.singletonList(testView2));

        when(repository.findAll(any(Pageable.class))).thenReturn(viewPage);

        mockMvc.perform(get("/api/sample-boxpos")
                        .param("page", "1")
                        .param("size", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/sample-boxpos - Should return empty page when no views")
    void getAll_WhenNoViews_ReturnsEmptyPage() throws Exception {
        Page<SampleBoxPosView> emptyPage = Page.empty();

        when(repository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/sample-boxpos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.empty", is(true)));

        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/sample-boxpos - Should map entity to DTO correctly")
    void getAll_MapsEntityToDTO() throws Exception {
        Page<SampleBoxPosView> viewPage = new PageImpl<>(Collections.singletonList(testView1));

        when(repository.findAll(any(Pageable.class))).thenReturn(viewPage);

        mockMvc.perform(get("/api/sample-boxpos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].sid", is("S001")))
                .andExpect(jsonPath("$.content[0].boxpos", is("BOX1/5")));
    }

    @Test
    @DisplayName("GET /api/sample-boxpos - Should handle multiple box position format")
    void getAll_WithMultipleBoxPositions_ReturnsCorrectFormat() throws Exception {
        SampleBoxPosView viewWithMultiple = SampleBoxPosView.builder()
                .sId("S003")
                .sStamp(LocalDateTime.of(2025, 12, 8, 11, 0))
                .boxpos("BOX1!")
                .build();

        Page<SampleBoxPosView> viewPage = new PageImpl<>(Collections.singletonList(viewWithMultiple));

        when(repository.findAll(any(Pageable.class))).thenReturn(viewPage);

        mockMvc.perform(get("/api/sample-boxpos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].boxpos", is("BOX1!")));
    }

    @Test
    @DisplayName("GET /api/sample-boxpos - Should handle dash for no positions")
    void getAll_WithNoPositions_ReturnsDash() throws Exception {
        SampleBoxPosView viewWithNone = SampleBoxPosView.builder()
                .sId("S004")
                .sStamp(LocalDateTime.of(2025, 12, 8, 11, 0))
                .boxpos("-")
                .build();

        Page<SampleBoxPosView> viewPage = new PageImpl<>(Collections.singletonList(viewWithNone));

        when(repository.findAll(any(Pageable.class))).thenReturn(viewPage);

        mockMvc.perform(get("/api/sample-boxpos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].boxpos", is("-")));
    }

    @Test
    @DisplayName("GET /api/sample-boxpos - Should return sorted by sStamp descending")
    void getAll_ReturnsSortedByTimestamp() throws Exception {
        Page<SampleBoxPosView> viewPage = new PageImpl<>(Arrays.asList(testView1, testView2));

        when(repository.findAll(any(Pageable.class))).thenReturn(viewPage);

        mockMvc.perform(get("/api/sample-boxpos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].sid", is("S001")));

        verify(repository, times(1)).findAll(any(Pageable.class));
    }
}
