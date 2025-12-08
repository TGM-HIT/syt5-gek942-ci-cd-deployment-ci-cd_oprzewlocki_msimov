package com.oliwier.insyrest.entity;

import com.oliwier.insyrest.entity.id.SampleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Analysis Entity Unit Tests")
class AnalysisTest {

    private Analysis analysis;
    private Sample sample;
    private SampleId sampleId;

    @BeforeEach
    void setUp() {
        analysis = new Analysis();

        sampleId = new SampleId("TEST123", LocalDateTime.of(2025, 12, 4, 10, 0, 0));
        sample = new Sample();
        sample.setId(sampleId);
        sample.setName("Test Sample");
    }

    @Test
    @DisplayName("Should create analysis with all fields")
    void constructor_WithAllFields_SetsAllProperties() {
        LocalDateTime now = LocalDateTime.now();
        BigDecimal pol = new BigDecimal("15.50");
        BigDecimal nat = new BigDecimal("10.20");

        Analysis fullAnalysis = new Analysis(
                1L, sample, pol, nat, new BigDecimal("5.30"),
                new BigDecimal("2.10"), new BigDecimal("8.40"),
                new BigDecimal("3.20"), now, now.plusHours(1),
                new BigDecimal("100.50"), new BigDecimal("101.20"),
                new BigDecimal("99.80"), new BigDecimal("0.70"),
                new BigDecimal("1.05"), "FLAGS", 1,
                "Test comment", now.plusHours(2)
        );

        assertEquals(1L, fullAnalysis.getAId());
        assertEquals(sample, fullAnalysis.getSample());
        assertEquals(pol, fullAnalysis.getPol());
        assertEquals(nat, fullAnalysis.getNat());
    }

    @Test
    @DisplayName("Should set and get aId correctly")
    void setAId_WithValidId_StoresCorrectly() {
        analysis.setAId(42L);

        assertEquals(42L, analysis.getAId());
    }

    @Test
    @DisplayName("Should set and get sample correctly")
    void setSample_WithValidSample_StoresCorrectly() {
        analysis.setSample(sample);

        assertEquals(sample, analysis.getSample());
        assertSame(sample, analysis.getSample());
    }

    @Test
    @DisplayName("Should handle null sample")
    void setSample_WithNull_AcceptsNull() {
        analysis.setSample(null);

        assertNull(analysis.getSample());
    }

    @Test
    @DisplayName("Should access sample's embedded ID correctly")
    void getSample_ReturnsCorrectSampleId() {
        analysis.setSample(sample);

        Sample retrievedSample = analysis.getSample();
        SampleId retrievedId = retrievedSample.getId();

        assertNotNull(retrievedId);
        assertEquals("TEST123", retrievedId.getsId());
        assertEquals(LocalDateTime.of(2025, 12, 4, 10, 0, 0), retrievedId.getsStamp());
    }

    @Test
    @DisplayName("Should set and get pol value correctly")
    void setPol_WithValidValue_StoresCorrectly() {
        BigDecimal pol = new BigDecimal("15.50");

        analysis.setPol(pol);

        assertEquals(pol, analysis.getPol());
        assertEquals(0, pol.compareTo(analysis.getPol()));
    }

    @Test
    @DisplayName("Should set and get nat value correctly")
    void setNat_WithValidValue_StoresCorrectly() {
        BigDecimal nat = new BigDecimal("10.20");

        analysis.setNat(nat);

        assertEquals(nat, analysis.getNat());
    }

    @Test
    @DisplayName("Should set and get kal value correctly")
    void setKal_WithValidValue_StoresCorrectly() {
        BigDecimal kal = new BigDecimal("5.30");

        analysis.setKal(kal);

        assertEquals(kal, analysis.getKal());
    }

    @Test
    @DisplayName("Should handle precision correctly for BigDecimal fields")
    void setBigDecimalFields_WithPrecision_MaintainsPrecision() {
        BigDecimal preciseValue = new BigDecimal("12.34");

        analysis.setAn(preciseValue);
        analysis.setGlu(preciseValue);
        analysis.setDry(preciseValue);

        assertEquals(preciseValue, analysis.getAn());
        assertEquals(preciseValue, analysis.getGlu());
        assertEquals(preciseValue, analysis.getDry());
    }

    @Test
    @DisplayName("Should set and get weight measurements correctly")
    void setWeightFields_WithValidValues_StoresCorrectly() {
        BigDecimal weightMea = new BigDecimal("100.50");
        BigDecimal weightNrm = new BigDecimal("101.20");
        BigDecimal weightCur = new BigDecimal("99.80");
        BigDecimal weightDif = new BigDecimal("0.70");

        analysis.setWeightMea(weightMea);
        analysis.setWeightNrm(weightNrm);
        analysis.setWeightCur(weightCur);
        analysis.setWeightDif(weightDif);

        assertEquals(weightMea, analysis.getWeightMea());
        assertEquals(weightNrm, analysis.getWeightNrm());
        assertEquals(weightCur, analysis.getWeightCur());
        assertEquals(weightDif, analysis.getWeightDif());
    }

    @Test
    @DisplayName("Should set and get density correctly")
    void setDensity_WithValidValue_StoresCorrectly() {
        BigDecimal density = new BigDecimal("1.05");

        analysis.setDensity(density);

        assertEquals(density, analysis.getDensity());
    }

    @Test
    @DisplayName("Should set and get dates correctly")
    void setDates_WithValidDates_StoresCorrectly() {
        LocalDateTime dateIn = LocalDateTime.now();
        LocalDateTime dateOut = dateIn.plusHours(2);
        LocalDateTime dateExported = dateIn.plusHours(3);

        analysis.setDateIn(dateIn);
        analysis.setDateOut(dateOut);
        analysis.setDateExported(dateExported);

        assertEquals(dateIn, analysis.getDateIn());
        assertEquals(dateOut, analysis.getDateOut());
        assertEquals(dateExported, analysis.getDateExported());
    }

    @Test
    @DisplayName("Should set and get aFlags correctly")
    void setAFlags_WithValidFlags_StoresCorrectly() {
        String flags = "ABC123XYZ";

        analysis.setAFlags(flags);

        assertEquals(flags, analysis.getAFlags());
    }

    @Test
    @DisplayName("Should handle max length for aFlags")
    void setAFlags_WithMaxLength_StoresCorrectly() {
        String flags = "123456789012345";

        analysis.setAFlags(flags);

        assertEquals(flags, analysis.getAFlags());
        assertEquals(15, analysis.getAFlags().length());
    }

    @Test
    @DisplayName("Should set and get lane correctly")
    void setLane_WithValidLane_StoresCorrectly() {
        analysis.setLane(5);

        assertEquals(5, analysis.getLane());
    }

    @Test
    @DisplayName("Should set and get comment correctly")
    void setComment_WithValidComment_StoresCorrectly() {
        String comment = "This is a test comment";

        analysis.setComment(comment);

        assertEquals(comment, analysis.getComment());
    }

    @Test
    @DisplayName("Should handle max length for comment")
    void setComment_WithMaxLength_StoresCorrectly() {
        String comment = "a".repeat(255);

        analysis.setComment(comment);

        assertEquals(comment, analysis.getComment());
        assertEquals(255, analysis.getComment().length());
    }

    @Test
    @DisplayName("Should handle null values for optional fields")
    void setOptionalFields_WithNull_AcceptsNull() {
        analysis.setPol(null);
        analysis.setNat(null);
        analysis.setComment(null);
        analysis.setDateExported(null);

        assertNull(analysis.getPol());
        assertNull(analysis.getNat());
        assertNull(analysis.getComment());
        assertNull(analysis.getDateExported());
    }

    @Test
    @DisplayName("Should create empty analysis with no-args constructor")
    void noArgsConstructor_CreatesEmptyAnalysis() {
        Analysis emptyAnalysis = new Analysis();

        assertNotNull(emptyAnalysis);
        assertNull(emptyAnalysis.getAId());
        assertNull(emptyAnalysis.getSample());
        assertNull(emptyAnalysis.getPol());
    }

    @Test
    @DisplayName("Should handle zero values for BigDecimal fields")
    void setBigDecimalFields_WithZero_StoresZero() {
        BigDecimal zero = BigDecimal.ZERO;

        analysis.setPol(zero);
        analysis.setNat(zero);
        analysis.setWeightDif(zero);

        assertEquals(BigDecimal.ZERO, analysis.getPol());
        assertEquals(BigDecimal.ZERO, analysis.getNat());
        assertEquals(BigDecimal.ZERO, analysis.getWeightDif());
    }

    @Test
    @DisplayName("Should handle negative values for weight difference")
    void setWeightDif_WithNegativeValue_StoresCorrectly() {
        BigDecimal negativeValue = new BigDecimal("-5.50");

        analysis.setWeightDif(negativeValue);

        assertEquals(negativeValue, analysis.getWeightDif());
        assertTrue(analysis.getWeightDif().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("Should work with different sample IDs")
    void setSample_WithDifferentSampleIds_WorksCorrectly() {
        SampleId id1 = new SampleId("SAMPLE1", LocalDateTime.of(2025, 1, 1, 10, 0, 0));
        Sample sample1 = new Sample();
        sample1.setId(id1);

        SampleId id2 = new SampleId("SAMPLE2", LocalDateTime.of(2025, 2, 1, 10, 0, 0));
        Sample sample2 = new Sample();
        sample2.setId(id2);

        analysis.setSample(sample1);
        assertEquals(sample1, analysis.getSample());

        analysis.setSample(sample2);
        assertEquals(sample2, analysis.getSample());

        assertEquals("SAMPLE2", analysis.getSample().getId().getsId());
    }

    @Test
    @DisplayName("Should handle sample with composite key correctly")
    void analysisWithSampleCompositeKey_WorksCorrectly() {
        SampleId compositeKey = new SampleId("COMP123", LocalDateTime.of(2025, 12, 4, 14, 30, 0));
        Sample sampleWithCompositeKey = new Sample();
        sampleWithCompositeKey.setId(compositeKey);
        sampleWithCompositeKey.setName("Composite Sample");
        sampleWithCompositeKey.setWeightNet(new BigDecimal("50.25"));

        analysis.setSample(sampleWithCompositeKey);
        analysis.setPol(new BigDecimal("20.50"));
        analysis.setComment("Analysis with composite key");

        assertNotNull(analysis.getSample());
        assertEquals("COMP123", analysis.getSample().getId().getsId());
        assertEquals(LocalDateTime.of(2025, 12, 4, 14, 30, 0),
                analysis.getSample().getId().getsStamp());
        assertEquals("Composite Sample", analysis.getSample().getName());
        assertEquals(new BigDecimal("20.50"), analysis.getPol());
    }
}
