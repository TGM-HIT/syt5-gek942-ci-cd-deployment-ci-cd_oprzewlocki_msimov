package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.AnalysisRequest;
import com.oliwier.insyrest.dto.response.AnalysisResponse;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.BoxPosId;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnalysisMapper Unit Tests")
class AnalysisMapperTest {

    @Mock
    private SampleRepository sampleRepository;

    private AnalysisMapper analysisMapper;
    private Analysis testAnalysis;
    private AnalysisRequest testAnalysisRequest;
    private Sample testSample;
    private SampleId testSampleId;

    @BeforeEach
    void setUp() {
        analysisMapper = new AnalysisMapper(sampleRepository);

        testSampleId = new SampleId("TEST123", LocalDateTime.of(2025, 12, 4, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(testSampleId);
        testSample.setName("Test Sample");
        testSample.setWeightNet(new BigDecimal("100.50"));
        testSample.setBoxPositions(new HashSet<>());

        testAnalysis = new Analysis();
        testAnalysis.setAId(1L);
        testAnalysis.setSample(testSample);
        testAnalysis.setPol(new BigDecimal("15.50"));
        testAnalysis.setNat(new BigDecimal("10.20"));
        testAnalysis.setKal(new BigDecimal("5.30"));
        testAnalysis.setAn(new BigDecimal("2.10"));
        testAnalysis.setGlu(new BigDecimal("8.40"));
        testAnalysis.setDry(new BigDecimal("3.20"));
        testAnalysis.setDateIn(LocalDateTime.of(2025, 12, 1, 10, 0));
        testAnalysis.setDateOut(LocalDateTime.of(2025, 12, 1, 12, 0));
        testAnalysis.setWeightMea(new BigDecimal("100.50"));
        testAnalysis.setWeightNrm(new BigDecimal("101.20"));
        testAnalysis.setWeightCur(new BigDecimal("99.80"));
        testAnalysis.setWeightDif(new BigDecimal("0.70"));
        testAnalysis.setDensity(new BigDecimal("1.05"));
        testAnalysis.setAFlags("ABC123");
        testAnalysis.setLane(5);
        testAnalysis.setComment("Test comment");
        testAnalysis.setDateExported(LocalDateTime.of(2025, 12, 1, 14, 0));

        testAnalysisRequest = new AnalysisRequest();
        testAnalysisRequest.setSId("TEST123");
        testAnalysisRequest.setSStamp(LocalDateTime.of(2025, 12, 4, 10, 0, 0));
        testAnalysisRequest.setPol(new BigDecimal("15.50"));
        testAnalysisRequest.setNat(new BigDecimal("10.20"));
        testAnalysisRequest.setKal(new BigDecimal("5.30"));
        testAnalysisRequest.setComment("Test comment");
    }

    @Test
    @DisplayName("Should map entity to response with all fields")
    void toResponse_WithFullEntity_MapsAllFields() {
        AnalysisResponse result = analysisMapper.toResponse(testAnalysis);

        assertNotNull(result);
        assertEquals(testAnalysis.getAId(), result.getAId());
        assertEquals(testAnalysis.getSample().getId().getsId(), result.getSId());
        assertEquals(testAnalysis.getSample().getId().getsStamp(), result.getSStamp());
        assertEquals(testAnalysis.getPol(), result.getPol());
        assertEquals(testAnalysis.getNat(), result.getNat());
        assertEquals(testAnalysis.getKal(), result.getKal());
        assertEquals(testAnalysis.getAn(), result.getAn());
        assertEquals(testAnalysis.getGlu(), result.getGlu());
        assertEquals(testAnalysis.getDry(), result.getDry());
        assertEquals(testAnalysis.getDateIn(), result.getDateIn());
        assertEquals(testAnalysis.getDateOut(), result.getDateOut());
        assertEquals(testAnalysis.getWeightMea(), result.getWeightMea());
        assertEquals(testAnalysis.getWeightNrm(), result.getWeightNrm());
        assertEquals(testAnalysis.getWeightCur(), result.getWeightCur());
        assertEquals(testAnalysis.getWeightDif(), result.getWeightDif());
        assertEquals(testAnalysis.getDensity(), result.getDensity());
        assertEquals(testAnalysis.getAFlags(), result.getAFlags());
        assertEquals(testAnalysis.getLane(), result.getLane());
        assertEquals(testAnalysis.getComment(), result.getComment());
        assertEquals(testAnalysis.getDateExported(), result.getDateExported());
    }

    @Test
    @DisplayName("Should map request to entity with sample lookup")
    void toEntity_WithValidRequest_MapsAndLookupSample() {
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        Analysis result = analysisMapper.toEntity(testAnalysisRequest);

        assertNotNull(result);
        assertEquals(testSample, result.getSample());
        assertEquals(testAnalysisRequest.getPol(), result.getPol());
        assertEquals(testAnalysisRequest.getNat(), result.getNat());
        assertEquals(testAnalysisRequest.getKal(), result.getKal());
        assertEquals(testAnalysisRequest.getComment(), result.getComment());
        verify(sampleRepository, times(1)).findById(any(SampleId.class));
    }

    @Test
    @DisplayName("Should handle sample not found when mapping request to entity")
    void toEntity_WhenSampleNotFound_SetsSampleToNull() {
        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.empty());

        Analysis result = analysisMapper.toEntity(testAnalysisRequest);

        assertNotNull(result);
        assertNull(result.getSample());
        assertEquals(testAnalysisRequest.getPol(), result.getPol());
        verify(sampleRepository, times(1)).findById(any(SampleId.class));
    }

    @Test
    @DisplayName("Should handle null sample ID in request")
    void toEntity_WithNullSampleId_DoesNotLookupSample() {
        testAnalysisRequest.setSId(null);
        testAnalysisRequest.setSStamp(null);

        Analysis result = analysisMapper.toEntity(testAnalysisRequest);

        assertNotNull(result);
        assertNull(result.getSample());
        assertEquals(testAnalysisRequest.getPol(), result.getPol());
        verify(sampleRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should update existing entity from request")
    void updateEntity_WithRequest_UpdatesAllFields() {
        Analysis existingAnalysis = new Analysis();
        existingAnalysis.setAId(99L);
        existingAnalysis.setPol(new BigDecimal("99.99"));

        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        analysisMapper.updateEntity(existingAnalysis, testAnalysisRequest);

        assertEquals(99L, existingAnalysis.getAId());
        assertEquals(testSample, existingAnalysis.getSample());
        assertEquals(testAnalysisRequest.getPol(), existingAnalysis.getPol());
        assertEquals(testAnalysisRequest.getNat(), existingAnalysis.getNat());
        assertEquals(testAnalysisRequest.getKal(), existingAnalysis.getKal());
        assertEquals(testAnalysisRequest.getComment(), existingAnalysis.getComment());
        verify(sampleRepository, times(1)).findById(any(SampleId.class));
    }

    @Test
    @DisplayName("Should compute boxposString with single position")
    void toResponse_WithSingleBoxPosition_ComputesCorrectString() {
        BoxPosId boxPosId = new BoxPosId();
        boxPosId.setBId("BOX1");
        boxPosId.setBposId(5);

        BoxPos boxPos = new BoxPos();
        boxPos.setId(boxPosId);

        Set<BoxPos> positions = new HashSet<>();
        positions.add(boxPos);
        testSample.setBoxPositions(positions);

        AnalysisResponse result = analysisMapper.toResponse(testAnalysis);

        assertNotNull(result);
        assertNotNull(result.getBoxposString());
        assertTrue(result.getBoxposString().contains("/"));
        assertTrue(result.getBoxposString().contains("BOX1"));
    }

    @Test
    @DisplayName("Should compute boxposString with multiple positions")
    void toResponse_WithMultipleBoxPositions_UsesExclamationMark() {
        BoxPosId boxPosId1 = new BoxPosId();
        boxPosId1.setBId("BOX1");
        boxPosId1.setBposId(5);
        BoxPos boxPos1 = new BoxPos();
        boxPos1.setId(boxPosId1);

        BoxPosId boxPosId2 = new BoxPosId();
        boxPosId2.setBId("BOX1");
        boxPosId2.setBposId(6);
        BoxPos boxPos2 = new BoxPos();
        boxPos2.setId(boxPosId2);

        Set<BoxPos> positions = new HashSet<>();
        positions.add(boxPos1);
        positions.add(boxPos2);
        testSample.setBoxPositions(positions);

        AnalysisResponse result = analysisMapper.toResponse(testAnalysis);

        assertNotNull(result);
        assertNotNull(result.getBoxposString());
        assertTrue(result.getBoxposString().contains("!"));
        assertTrue(result.getBoxposString().contains("BOX1"));
    }

    @Test
    @DisplayName("Should return dash when no box positions")
    void toResponse_WithNoBoxPositions_ReturnsDash() {
        testSample.setBoxPositions(new HashSet<>());

        AnalysisResponse result = analysisMapper.toResponse(testAnalysis);

        assertEquals("-", result.getBoxposString());
    }

    @Test
    @DisplayName("Should return dash when sample has null box positions")
    void toResponse_WithNullBoxPositions_ReturnsDash() {
        testSample.setBoxPositions(null);

        AnalysisResponse result = analysisMapper.toResponse(testAnalysis);

        assertEquals("-", result.getBoxposString());
    }

    @Test
    @DisplayName("Should handle entity with null sample")
    void toResponse_WithNullSample_HandlesGracefully() {
        testAnalysis.setSample(null);

        AnalysisResponse result = analysisMapper.toResponse(testAnalysis);

        assertNotNull(result);
        assertNull(result.getSId());
        assertNull(result.getSStamp());
        assertEquals("-", result.getBoxposString());
        assertEquals(testAnalysis.getAId(), result.getAId());
        assertEquals(testAnalysis.getPol(), result.getPol());
    }

    @Test
    @DisplayName("Should convert list of entities to response list")
    void toResponseList_WithMultipleEntities_MapsAll() {
        SampleId id1 = new SampleId("S001", LocalDateTime.of(2025, 12, 1, 10, 0, 0));
        Sample sample1 = new Sample();
        sample1.setId(id1);
        sample1.setBoxPositions(new HashSet<>());

        SampleId id2 = new SampleId("S002", LocalDateTime.of(2025, 12, 2, 10, 0, 0));
        Sample sample2 = new Sample();
        sample2.setId(id2);
        sample2.setBoxPositions(new HashSet<>());

        Analysis analysis1 = new Analysis();
        analysis1.setAId(1L);
        analysis1.setSample(sample1);
        analysis1.setPol(new BigDecimal("15.50"));

        Analysis analysis2 = new Analysis();
        analysis2.setAId(2L);
        analysis2.setSample(sample2);
        analysis2.setPol(new BigDecimal("20.30"));

        List<Analysis> analyses = List.of(analysis1, analysis2);

        List<AnalysisResponse> result = analysisMapper.toResponseList(analyses);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getAId());
        assertEquals(2L, result.get(1).getAId());
        assertEquals("S001", result.get(0).getSId());
        assertEquals("S002", result.get(1).getSId());
    }

    @Test
    @DisplayName("Should preserve BigDecimal precision in mapping")
    void toResponse_PreservesBigDecimalPrecision() {
        BigDecimal preciseValue = new BigDecimal("123.456789");
        testAnalysis.setPol(preciseValue);

        AnalysisResponse result = analysisMapper.toResponse(testAnalysis);

        assertEquals(0, preciseValue.compareTo(result.getPol()));
    }

    @Test
    @DisplayName("Should preserve LocalDateTime in mapping")
    void toResponse_PreservesLocalDateTime() {
        LocalDateTime specificTime = LocalDateTime.of(2025, 12, 4, 10, 30, 45);
        testAnalysis.setDateIn(specificTime);

        AnalysisResponse result = analysisMapper.toResponse(testAnalysis);

        assertEquals(specificTime, result.getDateIn());
    }

    @Test
    @DisplayName("Should map all BigDecimal fields correctly")
    void toEntity_MapsAllBigDecimalFields() {
        testAnalysisRequest.setWeightMea(new BigDecimal("100.11"));
        testAnalysisRequest.setWeightNrm(new BigDecimal("100.22"));
        testAnalysisRequest.setWeightCur(new BigDecimal("100.33"));
        testAnalysisRequest.setWeightDif(new BigDecimal("0.11"));
        testAnalysisRequest.setDensity(new BigDecimal("1.11"));

        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        Analysis result = analysisMapper.toEntity(testAnalysisRequest);

        assertEquals(new BigDecimal("100.11"), result.getWeightMea());
        assertEquals(new BigDecimal("100.22"), result.getWeightNrm());
        assertEquals(new BigDecimal("100.33"), result.getWeightCur());
        assertEquals(new BigDecimal("0.11"), result.getWeightDif());
        assertEquals(new BigDecimal("1.11"), result.getDensity());
    }

    @Test
    @DisplayName("Should map all date fields correctly")
    void toEntity_MapsAllDateFields() {
        LocalDateTime dateIn = LocalDateTime.of(2025, 12, 1, 8, 0);
        LocalDateTime dateOut = LocalDateTime.of(2025, 12, 1, 16, 0);
        LocalDateTime dateExported = LocalDateTime.of(2025, 12, 2, 10, 0);

        testAnalysisRequest.setDateIn(dateIn);
        testAnalysisRequest.setDateOut(dateOut);
        testAnalysisRequest.setDateExported(dateExported);

        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        Analysis result = analysisMapper.toEntity(testAnalysisRequest);

        assertEquals(dateIn, result.getDateIn());
        assertEquals(dateOut, result.getDateOut());
        assertEquals(dateExported, result.getDateExported());
    }

    @Test
    @DisplayName("Should update entity without changing ID")
    void updateEntity_PreservesEntityId() {
        Analysis existingAnalysis = new Analysis();
        existingAnalysis.setAId(42L);

        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        analysisMapper.updateEntity(existingAnalysis, testAnalysisRequest);

        assertEquals(42L, existingAnalysis.getAId());
    }

    @Test
    @DisplayName("Should handle null optional fields in request")
    void toEntity_WithNullOptionalFields_HandlesGracefully() {
        testAnalysisRequest.setComment(null);
        testAnalysisRequest.setAFlags(null);
        testAnalysisRequest.setDateExported(null);

        when(sampleRepository.findById(any(SampleId.class))).thenReturn(Optional.of(testSample));

        Analysis result = analysisMapper.toEntity(testAnalysisRequest);

        assertNotNull(result);
        assertNull(result.getComment());
        assertNull(result.getAFlags());
        assertNull(result.getDateExported());
    }
}
