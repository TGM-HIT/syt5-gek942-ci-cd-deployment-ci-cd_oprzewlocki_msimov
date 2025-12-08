package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.SampleRequest;
import com.oliwier.insyrest.dto.response.SampleResponse;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.BoxPosId;
import com.oliwier.insyrest.entity.id.SampleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SampleMapper Unit Tests")
class SampleMapperTest {

    private SampleMapper sampleMapper;
    private Sample testSample;
    private SampleRequest testSampleRequest;
    private SampleId testSampleId;

    @BeforeEach
    void setUp() {
        sampleMapper = new SampleMapper();

        testSampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));

        testSample = new Sample();
        testSample.setId(testSampleId);
        testSample.setName("Test Sample");
        testSample.setWeightNet(new BigDecimal("100.50"));
        testSample.setWeightBru(new BigDecimal("110.50"));
        testSample.setWeightTar(new BigDecimal("10.00"));
        testSample.setQuantity(5);
        testSample.setDistance(new BigDecimal("15.75"));
        testSample.setDateCrumbled(LocalDateTime.of(2025, 12, 8, 11, 0));
        testSample.setSFlags("FLAG1");
        testSample.setLane(3);
        testSample.setComment("Test comment");
        testSample.setDateExported(LocalDateTime.of(2025, 12, 8, 12, 0));
        testSample.setBoxPositions(new HashSet<>());

        testSampleRequest = new SampleRequest();
        testSampleRequest.setS_id("S001");
        testSampleRequest.setS_stamp(LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSampleRequest.setName("Test Sample");
        testSampleRequest.setWeightNet(new BigDecimal("100.50"));
        testSampleRequest.setWeightBru(new BigDecimal("110.50"));
        testSampleRequest.setWeightTar(new BigDecimal("10.00"));
        testSampleRequest.setQuantity(5);
        testSampleRequest.setDistance(new BigDecimal("15.75"));
        testSampleRequest.setDateCrumbled(LocalDateTime.of(2025, 12, 8, 11, 0));
        testSampleRequest.setSFlags("FLAG1");
        testSampleRequest.setLane(3);
        testSampleRequest.setComment("Test comment");
        testSampleRequest.setDateExported(LocalDateTime.of(2025, 12, 8, 12, 0));
    }

    @Test
    @DisplayName("Should map entity to response with all fields")
    void toResponse_WithFullEntity_MapsAllFields() {
        SampleResponse result = sampleMapper.toResponse(testSample);

        assertNotNull(result);
        assertEquals(testSample.getId().getsId(), result.getS_id());
        assertEquals(testSample.getId().getsStamp(), result.getS_stamp());
        assertEquals(testSample.getName(), result.getName());
        assertEquals(testSample.getWeightNet(), result.getWeightNet());
        assertEquals(testSample.getWeightBru(), result.getWeightBru());
        assertEquals(testSample.getWeightTar(), result.getWeightTar());
        assertEquals(testSample.getQuantity(), result.getQuantity());
        assertEquals(testSample.getDistance(), result.getDistance());
        assertEquals(testSample.getDateCrumbled(), result.getDateCrumbled());
        assertEquals(testSample.getSFlags(), result.getSFlags());
        assertEquals(testSample.getLane(), result.getLane());
        assertEquals(testSample.getComment(), result.getComment());
        assertEquals(testSample.getDateExported(), result.getDateExported());
        assertEquals("-", result.getBoxposString());
    }

    @Test
    @DisplayName("Should map request to entity with all fields")
    void toEntity_WithValidRequest_MapsAllFields() {
        Sample result = sampleMapper.toEntity(testSampleRequest);

        assertNotNull(result);
        assertEquals(testSampleRequest.getS_id(), result.getId().getsId());
        assertEquals(testSampleRequest.getS_stamp(), result.getId().getsStamp());
        assertEquals(testSampleRequest.getName(), result.getName());
        assertEquals(testSampleRequest.getWeightNet(), result.getWeightNet());
        assertEquals(testSampleRequest.getWeightBru(), result.getWeightBru());
        assertEquals(testSampleRequest.getWeightTar(), result.getWeightTar());
        assertEquals(testSampleRequest.getQuantity(), result.getQuantity());
        assertEquals(testSampleRequest.getDistance(), result.getDistance());
        assertEquals(testSampleRequest.getDateCrumbled(), result.getDateCrumbled());
        assertEquals(testSampleRequest.getSFlags(), result.getSFlags());
        assertEquals(testSampleRequest.getLane(), result.getLane());
        assertEquals(testSampleRequest.getComment(), result.getComment());
        assertEquals(testSampleRequest.getDateExported(), result.getDateExported());
    }

    @Test
    @DisplayName("Should update existing entity from request")
    void updateEntity_WithRequest_UpdatesAllFieldsExceptId() {
        SampleId originalId = testSample.getId();

        SampleRequest updateRequest = new SampleRequest();
        updateRequest.setS_id("S999");
        updateRequest.setS_stamp(LocalDateTime.of(2025, 12, 9, 10, 0, 0));
        updateRequest.setName("Updated Name");
        updateRequest.setWeightNet(new BigDecimal("200.00"));
        updateRequest.setQuantity(10);

        sampleMapper.updateEntity(testSample, updateRequest);

        assertEquals(originalId, testSample.getId());
        assertEquals("Updated Name", testSample.getName());
        assertEquals(new BigDecimal("200.00"), testSample.getWeightNet());
        assertEquals(10, testSample.getQuantity());
    }

    @Test
    @DisplayName("Should compute boxposString with single position")
    void toResponse_WithSingleBoxPos_ReturnsSlashFormat() {
        BoxPos pos = new BoxPos();
        pos.setId(new BoxPosId(5, "BOX1"));

        Set<BoxPos> positions = new HashSet<>();
        positions.add(pos);
        testSample.setBoxPositions(positions);

        SampleResponse result = sampleMapper.toResponse(testSample);

        assertEquals("BOX1/5", result.getBoxposString());
    }

    @Test
    @DisplayName("Should compute boxposString with multiple positions")
    void toResponse_WithMultipleBoxPos_ReturnsExclamationFormat() {
        BoxPos pos1 = new BoxPos();
        pos1.setId(new BoxPosId(5, "BOX1"));
        BoxPos pos2 = new BoxPos();
        pos2.setId(new BoxPosId(3, "BOX1"));

        Set<BoxPos> positions = new HashSet<>();
        positions.add(pos1);
        positions.add(pos2);
        testSample.setBoxPositions(positions);

        SampleResponse result = sampleMapper.toResponse(testSample);

        assertTrue(result.getBoxposString().contains("!"));
        assertTrue(result.getBoxposString().startsWith("BOX1"));
    }

    @Test
    @DisplayName("Should compute boxposString as dash when no positions")
    void toResponse_WithNoBoxPos_ReturnsDash() {
        testSample.setBoxPositions(new HashSet<>());

        SampleResponse result = sampleMapper.toResponse(testSample);

        assertEquals("-", result.getBoxposString());
    }

    @Test
    @DisplayName("Should compute boxposString as dash when positions null")
    void toResponse_WithNullBoxPos_ReturnsDash() {
        testSample.setBoxPositions(null);

        SampleResponse result = sampleMapper.toResponse(testSample);

        assertEquals("-", result.getBoxposString());
    }

    @Test
    @DisplayName("Should handle null optional fields in request")
    void toEntity_WithNullOptionalFields_HandlesGracefully() {
        testSampleRequest.setName(null);
        testSampleRequest.setWeightNet(null);
        testSampleRequest.setQuantity(null);
        testSampleRequest.setComment(null);

        Sample result = sampleMapper.toEntity(testSampleRequest);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getWeightNet());
        assertNull(result.getQuantity());
        assertNull(result.getComment());
    }

    @Test
    @DisplayName("Should preserve BigDecimal precision in mapping")
    void toResponse_PreservesBigDecimalPrecision() {
        BigDecimal preciseWeight = new BigDecimal("12345.67");
        testSample.setWeightNet(preciseWeight);

        SampleResponse result = sampleMapper.toResponse(testSample);

        assertEquals(preciseWeight, result.getWeightNet());
    }

    @Test
    @DisplayName("Should preserve LocalDateTime in mapping")
    void toResponse_PreservesLocalDateTime() {
        LocalDateTime specificTime = LocalDateTime.of(2025, 12, 8, 14, 30, 45);
        testSample.setDateCrumbled(specificTime);

        SampleResponse result = sampleMapper.toResponse(testSample);

        assertEquals(specificTime, result.getDateCrumbled());
    }
}
