package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.dto.AnalysisDTO;
import com.oliwier.insyrest.entity.Analysis;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.service.AnalysisService;
import com.oliwier.insyrest.service.SampleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController extends AbstractCrudController<Analysis, Long> {
    private final SampleService sampleService;
    private final AnalysisService analysisService;


    protected AnalysisController(AnalysisService service, ObjectMapper objectMapper, SampleService sampleService, AnalysisService analysisService) {
        super(service, objectMapper);
        this.sampleService = sampleService;
        this.analysisService = analysisService;
    }

    private AnalysisDTO toDto(Analysis a) {
        AnalysisDTO dto = new AnalysisDTO();

        dto.aId = a.getAId();

        dto.sId = a.getSample().getId().getsId();
        dto.sStamp = a.getSample().getId().getsStamp();

        var bp = a.getSample().getBoxPositions();
        if (bp == null || bp.isEmpty()) {
            dto.boxposString = "-";
        } else {
            var first = bp.iterator().next();
            var bid = first.getId().getBId();
            var pos = first.getId().getBposId();
            var sep = bp.size() == 1 ? "/" : "!";
            dto.boxposString = bid + sep + pos;
        }

        dto.pol = a.getPol();
        dto.nat = a.getNat();
        dto.kal = a.getKal();
        dto.an = a.getAn();
        dto.glu = a.getGlu();
        dto.dry = a.getDry();

        dto.lane = a.getLane();
        dto.comment = a.getComment();
        dto.dateIn = a.getDateIn();
        dto.dateOut = a.getDateOut();

        return dto;
    }


    @GetMapping("/validate-sample")
    public ResponseEntity<?> validateSample(
            @RequestParam String s_id,
            @RequestParam String s_stamp
    ) {
        try {
            LocalDateTime stamp = LocalDateTime.parse(s_stamp); // ← convert string to LocalDateTime
            SampleId id = new SampleId(s_id, stamp);
            boolean exists = sampleService.findById(id).isPresent();
            return ResponseEntity.ok(Map.of("valid", exists));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid timestamp format"));
        }
    }

    @GetMapping("/dto")
    public ResponseEntity<?> getAllDto(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam MultiValueMap<String, String> params
    ) {
        var filter = extractSubMap(params, "filter");
        var sort   = extractSubMap(params, "sort");

        var result = analysisService.findAllWithFilters(
                PageRequest.of(page, size),
                filter,
                sort
        );

        return ResponseEntity.ok(result.map(this::toDto));
    }


    @GetMapping("/dto/{id}")
    public ResponseEntity<?> getOneDto(@PathVariable Long id) {
        return analysisService.findById(id)
                .map(a -> ResponseEntity.ok(toDto(a)))
                .orElse(ResponseEntity.notFound().build());
    }

}
