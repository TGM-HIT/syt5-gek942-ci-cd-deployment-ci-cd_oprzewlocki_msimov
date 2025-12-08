package com.oliwier.insyrest.entity;

import com.oliwier.insyrest.entity.id.BoxPosId;
import com.oliwier.insyrest.entity.id.SampleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BoxPos Entity Unit Tests")
class BoxPosTest {

    private BoxPos boxPos;
    private BoxPosId boxPosId;
    private Box testBox;
    private Sample testSample;

    @BeforeEach
    void setUp() {
        boxPosId = new BoxPosId(1, "BOX1");

        testBox = new Box();
        testBox.setBId("BOX1");
        testBox.setName("Test Box");

        SampleId sampleId = new SampleId("S001", LocalDateTime.of(2025, 12, 8, 10, 0, 0));
        testSample = new Sample();
        testSample.setId(sampleId);

        boxPos = new BoxPos();
    }

    @Test
    @DisplayName("Should create boxpos with all fields")
    void constructor_WithAllFields_SetsAllProperties() {
        LocalDateTime now = LocalDateTime.now();
        BoxPos fullBoxPos = new BoxPos(boxPosId, testBox, testSample, now);

        assertEquals(boxPosId, fullBoxPos.getId());
        assertEquals(testBox, fullBoxPos.getBox());
        assertEquals(testSample, fullBoxPos.getSample());
        assertEquals(now, fullBoxPos.getDateExported());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void setId_WithValidId_StoresCorrectly() {
        boxPos.setId(boxPosId);
        assertEquals(boxPosId, boxPos.getId());
        assertEquals(1, boxPos.getId().getBposId());
        assertEquals("BOX1", boxPos.getId().getBId());
    }

    @Test
    @DisplayName("Should set and get box correctly")
    void setBox_WithValidBox_StoresCorrectly() {
        boxPos.setBox(testBox);
        assertEquals(testBox, boxPos.getBox());
        assertEquals("BOX1", boxPos.getBox().getBId());
    }

    @Test
    @DisplayName("Should set and get sample correctly")
    void setSample_WithValidSample_StoresCorrectly() {
        boxPos.setSample(testSample);
        assertEquals(testSample, boxPos.getSample());
        assertEquals("S001", boxPos.getSample().getId().getsId());
    }

    @Test
    @DisplayName("Should set and get dateExported correctly")
    void setDateExported_WithValidDate_StoresCorrectly() {
        LocalDateTime exportDate = LocalDateTime.of(2025, 12, 8, 10, 0);
        boxPos.setDateExported(exportDate);
        assertEquals(exportDate, boxPos.getDateExported());
    }

    @Test
    @DisplayName("Should handle null dateExported")
    void setDateExported_WithNull_AcceptsNull() {
        boxPos.setDateExported(null);
        assertNull(boxPos.getDateExported());
    }

    @Test
    @DisplayName("Should create empty boxpos with no-args constructor")
    void noArgsConstructor_CreatesEmptyBoxPos() {
        BoxPos emptyBoxPos = new BoxPos();
        assertNotNull(emptyBoxPos);
        assertNull(emptyBoxPos.getId());
        assertNull(emptyBoxPos.getBox());
        assertNull(emptyBoxPos.getSample());
        assertNull(emptyBoxPos.getDateExported());
    }

    @Test
    @DisplayName("Should maintain box reference integrity")
    void setBox_MaintainsReferenceIntegrity() {
        Box box1 = new Box();
        box1.setBId("BOX1");
        Box box2 = new Box();
        box2.setBId("BOX2");

        boxPos.setBox(box1);
        assertEquals("BOX1", boxPos.getBox().getBId());

        boxPos.setBox(box2);
        assertEquals("BOX2", boxPos.getBox().getBId());
    }

    @Test
    @DisplayName("Should maintain sample reference integrity")
    void setSample_MaintainsReferenceIntegrity() {
        SampleId id1 = new SampleId("S001", LocalDateTime.now());
        Sample sample1 = new Sample();
        sample1.setId(id1);

        SampleId id2 = new SampleId("S002", LocalDateTime.now());
        Sample sample2 = new Sample();
        sample2.setId(id2);

        boxPos.setSample(sample1);
        assertEquals("S001", boxPos.getSample().getId().getsId());

        boxPos.setSample(sample2);
        assertEquals("S002", boxPos.getSample().getId().getsId());
    }

    @Test
    @DisplayName("Should handle composite key correctly")
    void compositeKey_WorksCorrectly() {
        BoxPosId id1 = new BoxPosId(1, "BOX1");
        BoxPosId id2 = new BoxPosId(1, "BOX1");

        boxPos.setId(id1);
        assertEquals(id1, boxPos.getId());
        assertEquals(id2, boxPos.getId());
    }
}
