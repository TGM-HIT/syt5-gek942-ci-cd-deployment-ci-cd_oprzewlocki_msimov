package com.oliwier.insyrest.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.oliwier.insyrest.entity.*;
import com.oliwier.insyrest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SampleRepository sampleRepository;
    private final AnalysisRepository analysisRepository;
    private final BoxRepository boxRepository;

    // --- Aesthetic Colors (Notion-like) ---
    private static final Color TEXT_COLOR = new Color(55, 53, 47);      // Dark Gray Text
    private static final Color HEADER_BG = new Color(247, 247, 245);    // Very Light Gray Header
    private static final Color BORDER_COLOR = new Color(227, 226, 224); // Subtle Border

    /**
     * Main entry point to generate a report.
     */
    public byte[] generateReport(String type, LocalDateTime start, LocalDateTime end) {
        switch (type.toLowerCase()) {
            case "samples":
                return generateSampleReport(start, end);
            case "analysis":
                return generateAnalysisReport(start, end);
            case "box":
                return generateBoxReport(start, end);
            default:
                throw new IllegalArgumentException("Unknown report type: " + type);
        }
    }

    // --- Report Definitions ---

    private byte[] generateSampleReport(LocalDateTime start, LocalDateTime end) {
        List<Sample> data = sampleRepository.findSamplesInTimeRange(start, end);

        List<ColumnDef> columns = Arrays.asList(
                new ColumnDef("Sample ID", s -> ((Sample) s).getId().getsId()),
                new ColumnDef("Timestamp", s -> formatTime(((Sample) s).getId().getsStamp())),
                new ColumnDef("Name", s -> str(((Sample) s).getName())),
                new ColumnDef("Net Wgt", s -> String.valueOf(((Sample) s).getWeightNet())),
                new ColumnDef("Lane", s -> String.valueOf(((Sample) s).getLane())),
                new ColumnDef("Comment", s -> str(((Sample) s).getComment()))
        );

        return createAestheticPdf("Sample Report", start, end, data, columns);
    }

    private byte[] generateAnalysisReport(LocalDateTime start, LocalDateTime end) {
        List<Analysis> data = analysisRepository.findAllByDateInBetween(start, end);

        List<ColumnDef> columns = Arrays.asList(
                new ColumnDef("ID", a -> String.valueOf(((Analysis) a).getAId())),
                new ColumnDef("Sample ID", a -> ((Analysis) a).getSample().getId().getsId()),
                new ColumnDef("Pol", a -> String.valueOf(((Analysis) a).getPol())),
                new ColumnDef("Nat", a -> String.valueOf(((Analysis) a).getNat())),
                new ColumnDef("Date In", a -> formatTime(((Analysis) a).getDateIn()))
        );

        return createAestheticPdf("Analysis Report", start, end, data, columns);
    }

    private byte[] generateBoxReport(LocalDateTime start, LocalDateTime end) {
        List<Box> data = boxRepository.findAllByDateExportedBetween(start, end);

        List<ColumnDef> columns = Arrays.asList(
                new ColumnDef("Box ID", b -> ((Box) b).getBId()),
                new ColumnDef("Name", b -> str(((Box) b).getName())),
                new ColumnDef("Type", b -> String.valueOf(((Box) b).getType())),
                new ColumnDef("Max", b -> String.valueOf(((Box) b).getNumMax())),
                new ColumnDef("Exported", b -> formatTime(((Box) b).getDateExported()))
        );

        return createAestheticPdf("Box Report", start, end, data, columns);
    }

    // --- Generic PDF Generator Engine ---

    private byte[] createAestheticPdf(String title, LocalDateTime start, LocalDateTime end, List<?> data, List<ColumnDef> columns) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4.rotate(), 30, 30, 40, 40);
            PdfWriter.getInstance(doc, out);
            doc.open();

            // 1. Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, TEXT_COLOR);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setSpacingAfter(8);
            doc.add(titlePara);

            // 2. Metadata (Date Range)
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            String dateRange = String.format("%s  →  %s",
                    start.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                    end.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

            Paragraph metaPara = new Paragraph("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")) + "  |  Range: " + dateRange, metaFont);
            metaPara.setSpacingAfter(20);
            doc.add(metaPara);

            // Empty Check
            if (data.isEmpty()) {
                Paragraph emptyPara = new Paragraph("No records found for this period.", metaFont);
                emptyPara.setSpacingBefore(20);
                doc.add(emptyPara);
                doc.close();
                return out.toByteArray();
            }

            // 3. Table Setup
            PdfPTable table = new PdfPTable(columns.size());
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.getDefaultCell().setBorderColor(BORDER_COLOR);

            // 4. Header Row
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD, new Color(100, 100, 100));
            for (ColumnDef col : columns) {
                PdfPCell cell = new PdfPCell(new Phrase(col.header, headerFont));
                cell.setBackgroundColor(HEADER_BG);
                cell.setBorderColor(BORDER_COLOR);
                cell.setBorderWidth(0);
                cell.setBorderWidthBottom(1f);
                cell.setPaddingTop(8);
                cell.setPaddingBottom(8);
                cell.setPaddingLeft(6);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }

            // 5. Data Rows
            Font rowFont = FontFactory.getFont(FontFactory.HELVETICA, 10, TEXT_COLOR);

            for (Object item : data) {
                for (ColumnDef col : columns) {
                    String val = col.extractor.apply(item);
                    if (val == null || val.equals("null")) val = "-";

                    PdfPCell cell = new PdfPCell(new Phrase(val, rowFont));

                    // Notion Style: Minimal borders
                    cell.setBorderWidth(0);
                    cell.setBorderWidthBottom(0.5f);
                    cell.setBorderColor(BORDER_COLOR);

                    cell.setPaddingTop(6);
                    cell.setPaddingBottom(6);
                    cell.setPaddingLeft(6);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    table.addCell(cell);
                }
            }

            doc.add(table);
            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }


    private String formatTime(LocalDateTime t) {
        return t == null ? "-" : t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private String str(String s) {
        return s == null || s.isEmpty() ? "-" : s;
    }

    @RequiredArgsConstructor
    private static class ColumnDef {
        final String header;
        final Function<Object, String> extractor;
    }
}
