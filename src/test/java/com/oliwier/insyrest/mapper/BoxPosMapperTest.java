package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.BoxPosRequest;
import com.oliwier.insyrest.dto.response.BoxPosResponse;
import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.BoxPosId;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.BoxRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BoxPosMapper Unit Tests")
class BoxPosMapperTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private SampleRepository sampleRepository;

    private BoxPosMapper boxPosMapper;
    private BoxPos testBoxPos;
    private BoxPosRequest testBoxPosRequest;
    private Box testBox;
    private Sample testSample;
    private BoxPosId testBoxPosId;
    private SampleId testSampleId;

    @BeforeEach
    void setUp() {
        boxPosMapper = new BoxPosMapper(boxRepository, sampleRepository);

        testBoxPosId = new BoxPosId(1, "BOX1");

        testBox = new Box();
        testBox.setBId("BOX1");
        testBox.setName("Test Box");

        testSampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(testSampleId);

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
    }

    @Test
    @DisplayName("Should map entity to response with all fields")
    void toResponse_WithFullEntity_MapsAllFields() {
        BoxPosResponse result = boxPosMapper.toResponse(testBoxPos);

        assertNotNull(result);
        assertEquals(testBoxPos.getId().getBposId(), result.getBposId());
        assertEquals(testBoxPos.getId().getBId(), result.getBId());
        assertEquals(testBoxPos.getSample().getId().getsId(), result.getSId());
        assertEquals(testBoxPos.getSample().getId().getsStamp(), result.getSStamp());
        assertEquals(testBoxPos.getDateExported(), result.getDateExported());
    }

    @Test
    @DisplayName("Should map request to entity with repository lookups")
    void toEntity_WithValidRequest_MapsAndLookupsReferences() {
        when(boxRepository.findById("BOX1")).thenReturn(Optional.of(testBox));
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        BoxPos result = boxPosMapper.toEntity(testBoxPosRequest);

        assertNotNull(result);
        assertEquals(testBoxPosRequest.getBposId(), result.getId().getBposId());
        assertEquals(testBoxPosRequest.getBId(), result.getId().getBId());
        assertEquals(testBox, result.getBox());
        assertEquals(testSample, result.getSample());
        assertEquals(testBoxPosRequest.getDateExported(), result.getDateExported());

        verify(boxRepository, times(1)).findById("BOX1");
        verify(sampleRepository, times(1)).findById(any(SampleId.class));
    }

    @Test
    @DisplayName("Should throw exception when box not found")
    void toEntity_WhenBoxNotFound_ThrowsException() {
        when(boxRepository.findById("BOX1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            boxPosMapper.toEntity(testBoxPosRequest);
        });

        verify(boxRepository, times(1)).findById("BOX1");
    }

    @Test
    @DisplayName("Should throw exception when sample not found")
    void toEntity_WhenSampleNotFound_ThrowsException() {
        when(boxRepository.findById("BOX1")).thenReturn(Optional.of(testBox));
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            boxPosMapper.toEntity(testBoxPosRequest);
        });

        verify(sampleRepository, times(1)).findById(any(SampleId.class));
    }

    @Test
    @DisplayName("Should update existing entity from request")
    void updateEntity_WithRequest_UpdatesAllFieldsExceptId() {
        Box newBox = new Box();
        newBox.setBId("BOX2");

        SampleId newSampleId = new SampleId("S002", LocalDateTime.of(2025, 12, 8, 11, 0, 0));
        Sample newSample = new Sample();
        newSample.setId(newSampleId);

        BoxPosRequest updateRequest = new BoxPosRequest();
        updateRequest.setBposId(999);
        updateRequest.setBId("BOX2");
        updateRequest.setSId("S002");
        updateRequest.setSStamp(LocalDateTime.of(2025, 12, 8, 11, 0, 0));
        updateRequest.setDateExported(LocalDateTime.of(2025, 12, 8, 14, 0));

        when(boxRepository.findById("BOX2")).thenReturn(Optional.of(newBox));
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(newSample));

        boxPosMapper.updateEntity(testBoxPos, updateRequest);

        assertEquals(testBoxPosId, testBoxPos.getId());
        assertEquals(newBox, testBoxPos.getBox());
        assertEquals(newSample, testBoxPos.getSample());
        assertEquals(updateRequest.getDateExported(), testBoxPos.getDateExported());

        verify(boxRepository, times(1)).findById("BOX2");
        verify(sampleRepository, times(1)).findById(any(SampleId.class));
    }

    @Test
    @DisplayName("Should handle null dateExported in entity")
    void toResponse_WithNullDateExported_HandlesGracefully() {
        testBoxPos.setDateExported(null);

        BoxPosResponse result = boxPosMapper.toResponse(testBoxPos);

        assertNotNull(result);
        assertNull(result.getDateExported());
    }

    @Test
    @DisplayName("Should handle null dateExported in request")
    void toEntity_WithNullDateExported_HandlesGracefully() {
        testBoxPosRequest.setDateExported(null);

        when(boxRepository.findById("BOX1")).thenReturn(Optional.of(testBox));
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        BoxPos result = boxPosMapper.toEntity(testBoxPosRequest);

        assertNotNull(result);
        assertNull(result.getDateExported());
    }

    @Test
    @DisplayName("Should preserve LocalDateTime in mapping")
    void toResponse_PreservesLocalDateTime() {
        LocalDateTime specificTime = LocalDateTime.of(2025, 12, 8, 14, 30, 45);
        testBoxPos.setDateExported(specificTime);

        BoxPosResponse result = boxPosMapper.toResponse(testBoxPos);

        assertEquals(specificTime, result.getDateExported());
    }

    @Test
    @DisplayName("Should preserve composite key values")
    void toResponse_PreservesCompositeKey() {
        BoxPosId id = new BoxPosId(42, "BOX5");
        testBoxPos.setId(id);

        BoxPosResponse result = boxPosMapper.toResponse(testBoxPos);

        assertEquals(42, result.getBposId());
        assertEquals("BOX5", result.getBId());
    }

    @Test
    @DisplayName("Should preserve sample composite key values")
    void toResponse_PreservesSampleCompositeKey() {
        LocalDateTime sampleTime = LocalDateTime.of(2025, 12, 8, 9, 15, 30);
        SampleId sampleId = new SampleId("S999", sampleTime);
        testSample.setId(sampleId);

        BoxPosResponse result = boxPosMapper.toResponse(testBoxPos);

        assertEquals("S999", result.getSId());
        assertEquals(sampleTime, result.getSStamp());
    }
}
