package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.BoxPosRepository;
import com.oliwier.insyrest.repository.BoxRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BoxService Unit Tests")
class BoxServiceTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private BoxPosRepository boxPosRepository;

    @Mock
    private SampleRepository sampleRepository;

    private BoxService boxService;

    private Box testBox;
    private Sample testSample;
    private SampleId testSampleId;

    @BeforeEach
    void setUp() {
        boxService = new BoxService(boxRepository, boxRepository, boxPosRepository, sampleRepository);

        testBox = new Box();
        testBox.setBId("BOX1");
        testBox.setName("Test Box");

        testSampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(testSampleId);
    }

    @Test
    @DisplayName("Should return true when box is linked to sample")
    void existsLinkedToSample_WhenLinked_ReturnsTrue() {
        when(boxPosRepository.existsBySampleId(testSampleId)).thenReturn(true);

        boolean result = boxService.existsLinkedToSample(testSampleId);

        assertTrue(result);
        verify(boxPosRepository, times(1)).existsBySampleId(testSampleId);
    }

    @Test
    @DisplayName("Should return false when box is not linked to sample")
    void existsLinkedToSample_WhenNotLinked_ReturnsFalse() {
        when(boxPosRepository.existsBySampleId(testSampleId)).thenReturn(false);

        boolean result = boxService.existsLinkedToSample(testSampleId);

        assertFalse(result);
        verify(boxPosRepository, times(1)).existsBySampleId(testSampleId);
    }

    @Test
    @DisplayName("Should delete empty boxes")
    void deleteEmptyBoxes_WithEmptyBoxes_DeletesThem() {
        Box box1 = new Box();
        box1.setBId("BOX1");
        Box box2 = new Box();
        box2.setBId("BOX2");
        Box box3 = new Box();
        box3.setBId("BOX3");

        when(boxRepository.findAll()).thenReturn(Arrays.asList(box1, box2, box3));
        when(boxPosRepository.countByBox(box1)).thenReturn(0L);
        when(boxPosRepository.countByBox(box2)).thenReturn(5L);
        when(boxPosRepository.countByBox(box3)).thenReturn(0L);

        boxService.deleteEmptyBoxes();

        verify(boxRepository, times(1)).delete(box1);
        verify(boxRepository, never()).delete(box2);
        verify(boxRepository, times(1)).delete(box3);
    }

    @Test
    @DisplayName("Should not delete boxes when all have positions")
    void deleteEmptyBoxes_WithAllNonEmpty_DeletesNothing() {
        Box box1 = new Box();
        box1.setBId("BOX1");

        when(boxRepository.findAll()).thenReturn(Collections.singletonList(box1));
        when(boxPosRepository.countByBox(box1)).thenReturn(10L);

        boxService.deleteEmptyBoxes();

        verify(boxRepository, never()).delete((Box) any());
    }

    @Test
    @DisplayName("Should delete boxes by sample")
    void deleteBoxesBySample_WhenSampleExists_DeletesBoxesAndPositions() {
        BoxPos boxPos1 = new BoxPos();
        boxPos1.setBox(testBox);

        BoxPos boxPos2 = new BoxPos();
        boxPos2.setBox(testBox);

        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(boxPosRepository.findBySample(testSample)).thenReturn(Arrays.asList(boxPos1, boxPos2));
        when(boxPosRepository.countByBox(testBox)).thenReturn(0L);

        boxService.deleteBoxesBySample(testSampleId);

        verify(boxPosRepository, times(1)).deleteAll(Arrays.asList(boxPos1, boxPos2));
        verify(boxRepository, times(1)).delete(testBox);
    }

    @Test
    @DisplayName("Should not delete box when it still has other positions")
    void deleteBoxesBySample_WhenBoxHasOtherPositions_DoesNotDeleteBox() {
        BoxPos boxPos = new BoxPos();
        boxPos.setBox(testBox);

        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(boxPosRepository.findBySample(testSample)).thenReturn(Collections.singletonList(boxPos));
        when(boxPosRepository.countByBox(testBox)).thenReturn(5L);

        boxService.deleteBoxesBySample(testSampleId);

        verify(boxPosRepository, times(1)).deleteAll(Collections.singletonList(boxPos));
        verify(boxRepository, never()).delete(testBox);
    }

    @Test
    @DisplayName("Should throw exception when sample not found")
    void deleteBoxesBySample_WhenSampleNotFound_ThrowsException() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            boxService.deleteBoxesBySample(testSampleId);
        });

        verify(boxPosRepository, never()).deleteAll(any());
        verify(boxRepository, never()).delete((Box) any());
    }

    @Test
    @DisplayName("Should handle empty box positions list")
    void deleteBoxesBySample_WithNoBoxPositions_DoesNothing() {
        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(boxPosRepository.findBySample(testSample)).thenReturn(Collections.emptyList());

        boxService.deleteBoxesBySample(testSampleId);

        verify(boxPosRepository, never()).deleteAll(any());
        verify(boxRepository, never()).delete((Box) any());
    }

    @Test
    @DisplayName("Should return true when box has positions")
    void hasBoxPos_WhenBoxHasPositions_ReturnsTrue() {
        when(boxPosRepository.findAllByBoxId("BOX1")).thenReturn(Collections.singletonList(new BoxPos()));

        boolean result = boxService.hasBoxPos("BOX1");

        assertTrue(result);
        verify(boxPosRepository, times(1)).findAllByBoxId("BOX1");
    }

    @Test
    @DisplayName("Should return false when box has no positions")
    void hasBoxPos_WhenBoxHasNoPositions_ReturnsFalse() {
        when(boxPosRepository.findAllByBoxId("BOX1")).thenReturn(Collections.emptyList());

        boolean result = boxService.hasBoxPos("BOX1");

        assertFalse(result);
        verify(boxPosRepository, times(1)).findAllByBoxId("BOX1");
    }

    @Test
    @DisplayName("Should cascade delete box and all positions")
    void cascadeDeleteBox_DeletesPositionsAndBox() {
        doNothing().when(boxPosRepository).deleteAllByBoxId("BOX1");
        doNothing().when(boxRepository).deleteById("BOX1");

        boxService.cascadeDeleteBox("BOX1");

        verify(boxPosRepository, times(1)).deleteAllByBoxId("BOX1");
        verify(boxRepository, times(1)).deleteById("BOX1");
    }

    @Test
    @DisplayName("Should delete multiple boxes with distinct positions")
    void deleteBoxesBySample_WithMultipleBoxes_DeletesAllEmpty() {
        Box box2 = new Box();
        box2.setBId("BOX2");

        BoxPos pos1 = new BoxPos();
        pos1.setBox(testBox);
        BoxPos pos2 = new BoxPos();
        pos2.setBox(box2);

        when(sampleRepository.findById(testSampleId)).thenReturn(Optional.of(testSample));
        when(boxPosRepository.findBySample(testSample)).thenReturn(Arrays.asList(pos1, pos2));
        when(boxPosRepository.countByBox(testBox)).thenReturn(0L);
        when(boxPosRepository.countByBox(box2)).thenReturn(0L);

        boxService.deleteBoxesBySample(testSampleId);

        verify(boxRepository, times(1)).delete(testBox);
        verify(boxRepository, times(1)).delete(box2);
    }
}
