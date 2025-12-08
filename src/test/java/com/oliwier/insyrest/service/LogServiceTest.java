package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Log;
import com.oliwier.insyrest.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LogService Unit Tests")
class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    private LogService logService;

    private Log testLog;

    @BeforeEach
    void setUp() {
        logService = new LogService(logRepository);

        testLog = new Log();
        testLog.setLogId(1L);
        testLog.setLevel("INFO");
        testLog.setInfo("Test log");
        testLog.setDateCreated(LocalDateTime.of(2025, 12, 8, 10, 0));
    }

    @Test
    @DisplayName("Should find log by ID")
    void findById_WhenLogExists_ReturnsLog() {
        when(logRepository.findById(1L)).thenReturn(Optional.of(testLog));

        Optional<Log> result = logService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testLog, result.get());
        verify(logRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when log not found")
    void findById_WhenLogNotFound_ReturnsEmpty() {
        when(logRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Log> result = logService.findById(999L);

        assertFalse(result.isPresent());
        verify(logRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should save log")
    void save_WithLog_SavesAndReturnsLog() {
        when(logRepository.save(testLog)).thenReturn(testLog);

        Log result = logService.save(testLog);

        assertNotNull(result);
        assertEquals(testLog, result);
        verify(logRepository, times(1)).save(testLog);
    }

    @Test
    @DisplayName("Should delete log by ID")
    void deleteById_WithValidId_DeletesLog() {
        doNothing().when(logRepository).deleteById(1L);

        logService.deleteById(1L);

        verify(logRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should find all logs with pagination")
    void findAll_WithPageable_ReturnsPageOfLogs() {
        Log log2 = new Log();
        log2.setLogId(2L);

        List<Log> logs = Arrays.asList(testLog, log2);
        Page<Log> logPage = new PageImpl<>(logs);

        when(logRepository.findAll(any(Pageable.class))).thenReturn(logPage);

        Page<Log> result = logService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(logRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should handle empty result set")
    void findAll_WhenNoLogs_ReturnsEmptyPage() {
        Page<Log> emptyPage = Page.empty();

        when(logRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        Page<Log> result = logService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}
