package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.BoxPosId;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.BoxPosRepository;
import com.oliwier.insyrest.repository.BoxRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BoxPosService Unit Tests")
class BoxPosServiceTest {

    @Mock
    private BoxPosRepository boxPosRepository;

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private BoxRepository boxRepository;

    private BoxPosService boxPosService;

    private BoxPos testBoxPos;
    private Sample testSample;
    private SampleId testSampleId;
    private Box testBox;

    @BeforeEach
    void setUp() {
        boxPosService = new BoxPosService(boxPosRepository, boxPosRepository, sampleRepository, boxRepository);

        testSampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(testSampleId);

        testBox = new Box();
        testBox.setBId("BOX1");

        BoxPosId boxPosId = new BoxPosId(1, "BOX1");
        testBoxPos = new BoxPos();
        testBoxPos.setId(boxPosId);
        testBoxPos.setBox(testBox);
        testBoxPos.setSample(testSample);
    }

    @Test
    @DisplayName("Should delete all box positions by sample")
    void deleteAllBySample_WhenSampleExists_DeletesAllPositions() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        doNothing().when(boxPosRepository).deleteAllBySample(testSample);

        boxPosService.deleteAllBySample(testSampleId);

        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(boxPosRepository, times(1)).deleteAllBySample(testSample);
    }

    @Test
    @DisplayName("Should not delete when sample not found")
    void deleteAllBySample_WhenSampleNotFound_DoesNothing() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.empty());

        boxPosService.deleteAllBySample(testSampleId);

        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(boxPosRepository, never()).deleteAllBySample(any());
    }

    @Test
    @DisplayName("Should return true when box positions exist for sample")
    void existsForSample_WhenPositionsExist_ReturnsTrue() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(boxPosRepository.findBySample(testSample)).thenReturn(Collections.singletonList(testBoxPos));

        boolean result = boxPosService.existsForSample(testSampleId);

        assertTrue(result);
        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(boxPosRepository, times(1)).findBySample(testSample);
    }

    @Test
    @DisplayName("Should return false when no box positions exist for sample")
    void existsForSample_WhenNoPositions_ReturnsFalse() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(boxPosRepository.findBySample(testSample)).thenReturn(Collections.emptyList());

        boolean result = boxPosService.existsForSample(testSampleId);

        assertFalse(result);
        verify(boxPosRepository, times(1)).findBySample(testSample);
    }

    @Test
    @DisplayName("Should return false when sample not found")
    void existsForSample_WhenSampleNotFound_ReturnsFalse() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.empty());

        boolean result = boxPosService.existsForSample(testSampleId);

        assertFalse(result);
        verify(sampleRepository, times(1)).findById(testSampleId);
        verify(boxPosRepository, never()).findBySample(any());
    }

    @Test
    @DisplayName("Should save box position with complete data")
    void save_WithCompleteBoxPos_SavesSuccessfully() {
        when(boxRepository.findById("BOX1")).thenReturn(Optional.of(testBox));
        when(boxPosRepository.save(testBoxPos)).thenReturn(testBoxPos);

        BoxPos result = boxPosService.save(testBoxPos);

        assertNotNull(result);
        assertEquals(testBoxPos, result);
        verify(boxRepository, times(1)).findById("BOX1");
        verify(boxPosRepository, times(1)).save(testBoxPos);
    }

    @Test
    @DisplayName("Should throw exception when box not found during save")
    void save_WhenBoxNotFound_ThrowsException() {
        when(boxRepository.findById("BOX1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            boxPosService.save(testBoxPos);
        });

        verify(boxRepository, times(1)).findById("BOX1");
        verify(boxPosRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle multiple box positions for same sample")
    void existsForSample_WithMultiplePositions_ReturnsTrue() {
        BoxPos pos1 = new BoxPos();
        BoxPos pos2 = new BoxPos();

        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(boxPosRepository.findBySample(testSample)).thenReturn(Arrays.asList(pos1, pos2));

        boolean result = boxPosService.existsForSample(testSampleId);

        assertTrue(result);
    }
}
