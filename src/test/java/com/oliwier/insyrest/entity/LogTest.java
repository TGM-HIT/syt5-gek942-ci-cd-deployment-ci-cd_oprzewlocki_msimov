package com.oliwier.insyrest.entity;

import com.oliwier.insyrest.entity.id.SampleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Log Entity Unit Tests")
class LogTest {

    private Log log;
    private Sample testSample;
    private Analysis testAnalysis;

    @BeforeEach
    void setUp() {
        log = new Log();

        SampleId sampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(sampleId);

        testAnalysis = new Analysis();
        testAnalysis.setAId(1L);
    }

    @Test
    @DisplayName("Should create log with all fields")
    void constructor_WithAllFields_SetsAllProperties() {
        LocalDateTime created = LocalDateTime.of(2025, 12, 8, 10, 0);
        LocalDateTime exported = LocalDateTime.of(2025, 12, 8, 12, 0);

        Log fullLog = new Log(1L, created, "INFO", "Test log", testSample, testAnalysis, exported);

        assertEquals(1L, fullLog.getLogId());
        assertEquals(created, fullLog.getDateCreated());
        assertEquals("INFO", fullLog.getLevel());
        assertEquals("Test log", fullLog.getInfo());
        assertEquals(testSample, fullLog.getSample());
        assertEquals(testAnalysis, fullLog.getAnalysis());
        assertEquals(exported, fullLog.getDateExported());
    }

    @Test
    @DisplayName("Should set and get logId correctly")
    void setLogId_WithValidId_StoresCorrectly() {
        log.setLogId(42L);
        assertEquals(42L, log.getLogId());
    }

    @Test
    @DisplayName("Should set and get dateCreated correctly")
    void setDateCreated_WithValidDate_StoresCorrectly() {
        LocalDateTime date = LocalDateTime.of(2025, 12, 8, 10, 30);
        log.setDateCreated(date);
        assertEquals(date, log.getDateCreated());
    }

    @Test
    @DisplayName("Should set and get level correctly")
    void setLevel_WithValidLevel_StoresCorrectly() {
        log.setLevel("ERROR");
        assertEquals("ERROR", log.getLevel());
    }

    @Test
    @DisplayName("Should set and get info correctly")
    void setInfo_WithValidInfo_StoresCorrectly() {
        log.setInfo("Test message");
        assertEquals("Test message", log.getInfo());
    }

    @Test
    @DisplayName("Should set and get sample correctly")
    void setSample_WithValidSample_StoresCorrectly() {
        log.setSample(testSample);
        assertEquals(testSample, log.getSample());
        assertEquals("S001", log.getSample().getId().getsId());
    }

    @Test
    @DisplayName("Should set and get analysis correctly")
    void setAnalysis_WithValidAnalysis_StoresCorrectly() {
        log.setAnalysis(testAnalysis);
        assertEquals(testAnalysis, log.getAnalysis());
        assertEquals(1L, log.getAnalysis().getAId());
    }

    @Test
    @DisplayName("Should set and get dateExported correctly")
    void setDateExported_WithValidDate_StoresCorrectly() {
        LocalDateTime exportDate = LocalDateTime.of(2025, 12, 8, 12, 0);
        log.setDateExported(exportDate);
        assertEquals(exportDate, log.getDateExported());
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void setOptionalFields_WithNull_AcceptsNull() {
        log.setSample(null);
        log.setAnalysis(null);
        log.setDateExported(null);

        assertNull(log.getSample());
        assertNull(log.getAnalysis());
        assertNull(log.getDateExported());
    }

    @Test
    @DisplayName("Should handle max length for level")
    void setLevel_WithMaxLength_StoresCorrectly() {
        String maxLevel = "A".repeat(10);
        log.setLevel(maxLevel);
        assertEquals(maxLevel, log.getLevel());
        assertEquals(10, log.getLevel().length());
    }

    @Test
    @DisplayName("Should handle max length for info")
    void setInfo_WithMaxLength_StoresCorrectly() {
        String maxInfo = "a".repeat(255);
        log.setInfo(maxInfo);
        assertEquals(maxInfo, log.getInfo());
        assertEquals(255, log.getInfo().length());
    }

    @Test
    @DisplayName("Should create empty log with no-args constructor")
    void noArgsConstructor_CreatesEmptyLog() {
        Log emptyLog = new Log();
        assertNotNull(emptyLog);
        assertNull(emptyLog.getLogId());
        assertNull(emptyLog.getDateCreated());
        assertNull(emptyLog.getLevel());
    }

    @Test
    @DisplayName("Should support different log levels")
    void setLevel_WithDifferentLevels_StoresCorrectly() {
        String[] levels = {"DEBUG", "INFO", "WARN", "ERROR", "FATAL"};

        for (String level : levels) {
            log.setLevel(level);
            assertEquals(level, log.getLevel());
        }
    }
}
