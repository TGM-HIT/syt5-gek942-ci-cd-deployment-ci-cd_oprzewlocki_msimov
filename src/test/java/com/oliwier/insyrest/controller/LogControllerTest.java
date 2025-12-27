package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.config.TestSecurityConfig;
import com.oliwier.insyrest.dto.request.LogRequest;
import com.oliwier.insyrest.dto.response.LogResponse;
import com.oliwier.insyrest.entity.Log;
import com.oliwier.insyrest.mapper.LogMapper;
import com.oliwier.insyrest.service.LogService;
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

@WebMvcTest(LogController.class)
@DisplayName("LogController Unit Tests")
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LogService logService;

    @MockitoBean
    private LogMapper logMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Log testLog;
    private LogRequest testLogRequest;
    private LogResponse testLogResponse;

    @BeforeEach
    void setUp() {
        testLog = new Log();
        testLog.setLogId(1L);
        testLog.setDateCreated(LocalDateTime.of(2025, 12, 8, 10, 0));
        testLog.setLevel("INFO");
        testLog.setInfo("Test log message");

        testLogRequest = new LogRequest();
        testLogRequest.setDateCreated(LocalDateTime.of(2025, 12, 8, 10, 0));
        testLogRequest.setLevel("INFO");
        testLogRequest.setInfo("Test log message");

        testLogResponse = new LogResponse();
        testLogResponse.setLogId(1L);
        testLogResponse.setDateCreated(LocalDateTime.of(2025, 12, 8, 10, 0));
        testLogResponse.setLevel("INFO");
        testLogResponse.setInfo("Test log message");
    }

    @Test
    @DisplayName("GET /api/logs/{id} - Should return log when found")
    void getLogById_WhenExists_ReturnsLog() throws Exception {
        when(logService.findById(1L)).thenReturn(Optional.of(testLog));
        when(logMapper.toResponse(testLog)).thenReturn(testLogResponse);

        mockMvc.perform(get("/api/logs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.logId", is(1)))
                .andExpect(jsonPath("$.level", is("INFO")))
                .andExpect(jsonPath("$.info", is("Test log message")));

        verify(logService, times(1)).findById(1L);
        verify(logMapper, times(1)).toResponse(testLog);
    }

    @Test
    @DisplayName("GET /api/logs/{id} - Should return 404 when not found")
    void getLogById_WhenNotFound_Returns404() throws Exception {
        when(logService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/logs/999"))
                .andExpect(status().isNotFound());

        verify(logService, times(1)).findById(999L);
        verify(logMapper, never()).toResponse(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("GET /api/logs - Should return all logs")
    void getAllLogs_ReturnsLogsList() throws Exception {
        Log log2 = new Log();
        log2.setLogId(2L);

        LogResponse response2 = new LogResponse();
        response2.setLogId(2L);

        Page<Log> logPage = new PageImpl<>(Arrays.asList(testLog, log2));
        Page<LogResponse> responsePage = new PageImpl<>(Arrays.asList(testLogResponse, response2));

        when(logService.findAllWithFilters(
                org.mockito.ArgumentMatchers.any(PageRequest.class),
                anyMap(),
                anyMap()
        )).thenReturn(logPage);

        when(logMapper.toResponsePage(org.mockito.ArgumentMatchers.any(Page.class)))
                .thenReturn(responsePage);

        mockMvc.perform(get("/api/logs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].logId", is(1)))
                .andExpect(jsonPath("$.content[1].logId", is(2)));
    }

    @Test
    @DisplayName("POST /api/logs - Should create new log")
    void createLog_WithValidData_ReturnsCreated() throws Exception {
        when(logMapper.toEntity(org.mockito.ArgumentMatchers.any(LogRequest.class)))
                .thenReturn(testLog);
        when(logService.save(org.mockito.ArgumentMatchers.any(Log.class)))
                .thenReturn(testLog);
        when(logMapper.toResponse(testLog)).thenReturn(testLogResponse);

        mockMvc.perform(post("/api/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLogRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.logId", is(1)))
                .andExpect(jsonPath("$.level", is("INFO")));

        verify(logService, times(1)).save(org.mockito.ArgumentMatchers.any(Log.class));
    }

    @Test
    @DisplayName("PUT /api/logs/{id} - Should update existing log")
    void updateLog_WhenExists_ReturnsUpdated() throws Exception {
        when(logService.findById(1L)).thenReturn(Optional.of(testLog));
        doNothing().when(logMapper).updateEntity(
                org.mockito.ArgumentMatchers.any(Log.class),
                org.mockito.ArgumentMatchers.any(LogRequest.class)
        );
        when(logService.save(org.mockito.ArgumentMatchers.any(Log.class)))
                .thenReturn(testLog);
        when(logMapper.toResponse(testLog)).thenReturn(testLogResponse);

        mockMvc.perform(put("/api/logs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLogRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logId", is(1)));

        verify(logService, times(1)).findById(1L);
        verify(logService, times(1)).save(org.mockito.ArgumentMatchers.any(Log.class));
    }

    @Test
    @DisplayName("DELETE /api/logs/{id} - Should delete log")
    void deleteLog_WhenExists_ReturnsNoContent() throws Exception {
        when(logService.findById(1L)).thenReturn(Optional.of(testLog));
        doNothing().when(logService).deleteById(1L);

        mockMvc.perform(delete("/api/logs/1"))
                .andExpect(status().isNoContent());

        verify(logService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("PUT /api/logs/{id} - Should return 404 when not found")
    void updateLog_WhenNotFound_Returns404() throws Exception {
        when(logService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/logs/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLogRequest)))
                .andExpect(status().isNotFound());

        verify(logService, times(1)).findById(999L);
        verify(logService, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
