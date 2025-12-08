package com.oliwier.insyrest.mapper;

import com.oliwier.insyrest.dto.request.BoxRequest;
import com.oliwier.insyrest.dto.response.BoxResponse;
import com.oliwier.insyrest.entity.Box;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BoxMapper Unit Tests")
class BoxMapperTest {

    private BoxMapper boxMapper;
    private Box testBox;
    private BoxRequest testBoxRequest;

    @BeforeEach
    void setUp() {
        boxMapper = new BoxMapper();

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
        testBoxRequest.setDateExported(LocalDateTime.of(2025, 12, 8, 10, 0));
    }

    @Test
    @DisplayName("Should map entity to response with all fields")
    void toResponse_WithFullEntity_MapsAllFields() {
        BoxResponse result = boxMapper.toResponse(testBox);

        assertNotNull(result);
        assertEquals(testBox.getBId(), result.getBId());
        assertEquals(testBox.getName(), result.getName());
        assertEquals(testBox.getNumMax(), result.getNumMax());
        assertEquals(testBox.getType(), result.getType());
        assertEquals(testBox.getComment(), result.getComment());
        assertEquals(testBox.getDateExported(), result.getDateExported());
    }

    @Test
    @DisplayName("Should map request to entity with all fields")
    void toEntity_WithFullRequest_MapsAllFields() {
        Box result = boxMapper.toEntity(testBoxRequest);

        assertNotNull(result);
        assertEquals(testBoxRequest.getBId(), result.getBId());
        assertEquals(testBoxRequest.getName(), result.getName());
        assertEquals(testBoxRequest.getNumMax(), result.getNumMax());
        assertEquals(testBoxRequest.getType(), result.getType());
        assertEquals(testBoxRequest.getComment(), result.getComment());
        assertEquals(testBoxRequest.getDateExported(), result.getDateExported());
    }

    @Test
    @DisplayName("Should update existing entity from request")
    void updateEntity_WithRequest_UpdatesAllFieldsExceptId() {
        Box existingBox = new Box();
        existingBox.setBId("BOX1");
        existingBox.setName("Old Name");
        existingBox.setNumMax(48);

        boxMapper.updateEntity(existingBox, testBoxRequest);

        assertEquals("BOX1", existingBox.getBId());
        assertEquals(testBoxRequest.getName(), existingBox.getName());
        assertEquals(testBoxRequest.getNumMax(), existingBox.getNumMax());
        assertEquals(testBoxRequest.getType(), existingBox.getType());
        assertEquals(testBoxRequest.getComment(), existingBox.getComment());
        assertEquals(testBoxRequest.getDateExported(), existingBox.getDateExported());
    }

    @Test
    @DisplayName("Should handle null optional fields in entity")
    void toResponse_WithNullOptionalFields_HandlesGracefully() {
        testBox.setName(null);
        testBox.setComment(null);
        testBox.setDateExported(null);

        BoxResponse result = boxMapper.toResponse(testBox);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getComment());
        assertNull(result.getDateExported());
    }

    @Test
    @DisplayName("Should handle null optional fields in request")
    void toEntity_WithNullOptionalFields_HandlesGracefully() {
        testBoxRequest.setName(null);
        testBoxRequest.setComment(null);
        testBoxRequest.setDateExported(null);

        Box result = boxMapper.toEntity(testBoxRequest);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getComment());
        assertNull(result.getDateExported());
    }

    @Test
    @DisplayName("Should convert list of entities to response list")
    void toResponseList_WithMultipleEntities_MapsAll() {
        Box box1 = new Box();
        box1.setBId("BOX1");
        box1.setName("Box 1");

        Box box2 = new Box();
        box2.setBId("BOX2");
        box2.setName("Box 2");

        List<Box> boxes = Arrays.asList(box1, box2);

        List<BoxResponse> result = boxMapper.toResponseList(boxes);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("BOX1", result.get(0).getBId());
        assertEquals("BOX2", result.get(1).getBId());
    }

    @Test
    @DisplayName("Should preserve all numeric values")
    void toResponse_PreservesNumericValues() {
        testBox.setNumMax(999);
        testBox.setType(5);

        BoxResponse result = boxMapper.toResponse(testBox);

        assertEquals(999, result.getNumMax());
        assertEquals(5, result.getType());
    }

    @Test
    @DisplayName("Should preserve LocalDateTime in mapping")
    void toResponse_PreservesLocalDateTime() {
        LocalDateTime specificTime = LocalDateTime.of(2025, 12, 8, 14, 30, 45);
        testBox.setDateExported(specificTime);

        BoxResponse result = boxMapper.toResponse(testBox);

        assertEquals(specificTime, result.getDateExported());
    }

    @Test
    @DisplayName("Should update entity without changing ID")
    void updateEntity_PreservesEntityId() {
        Box existingBox = new Box();
        existingBox.setBId("ORIG");

        BoxRequest updateRequest = new BoxRequest();
        updateRequest.setBId("NEW");
        updateRequest.setName("Updated");

        boxMapper.updateEntity(existingBox, updateRequest);

        assertEquals("ORIG", existingBox.getBId());
        assertEquals("Updated", existingBox.getName());
    }
}
