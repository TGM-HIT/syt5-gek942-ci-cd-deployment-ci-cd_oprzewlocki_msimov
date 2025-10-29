package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.dto.BoxPosDto;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.BoxPosId;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.repository.BoxPosRepository;
import com.oliwier.insyrest.repository.BoxRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import com.oliwier.insyrest.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/boxpos")
public class BoxPosController extends AbstractCrudController<BoxPos, BoxPosId> {

    private final BoxPosRepository boxPosRepository;
    private final BoxRepository boxRepository;
    private final SampleRepository sampleRepository;

    public BoxPosController(
            CrudService<BoxPos, BoxPosId> service,
            ObjectMapper mapper,
            BoxPosRepository boxPosRepository,
            BoxRepository boxRepository,
            SampleRepository sampleRepository
    ) {
        super(service, mapper); // <- behält GET (paging/filter) & DELETE
        this.boxPosRepository = boxPosRepository;
        this.boxRepository = boxRepository;
        this.sampleRepository = sampleRepository;
    }

    // --------- FLAT CREATE (kein override, eigener Pfad) ----------
    @PostMapping("/flat")
    @Transactional
    public BoxPos createFlat(@RequestBody BoxPosDto dto) {
        var id  = new BoxPosId(dto.bposId(), dto.bId());
        var box = boxRepository.findById(dto.bId())
                .orElseThrow(() -> new RuntimeException("Box not found: " + dto.bId()));
        var sampleId = new SampleId(dto.sId(), java.time.LocalDateTime.parse(dto.sStamp()));
        var sample = sampleRepository.findById(sampleId)
                .orElseThrow(() -> new RuntimeException("Sample not found: " + dto.sId() + "," + dto.sStamp()));
        var date = dto.dateExported() != null ? java.time.LocalDateTime.parse(dto.dateExported()) : null;

        var pos = new BoxPos(id, box, sample, date);
        return boxPosRepository.save(pos);
    }

    // --------- FLAT UPDATE ----------
    @PutMapping("/flat/{bposId},{bId}")
    @Transactional
    public BoxPos updateFlat(@PathVariable Integer bposId,
                             @PathVariable String bId,
                             @RequestBody BoxPosDto dto) {
        var id = new BoxPosId(bposId, bId);
        var pos = boxPosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BoxPos not found: " + bposId + "," + bId));

        var box = boxRepository.findById(bId)
                .orElseThrow(() -> new RuntimeException("Box not found: " + bId));
        var sampleId = new SampleId(dto.sId(), java.time.LocalDateTime.parse(dto.sStamp()));
        var sample = sampleRepository.findById(sampleId)
                .orElseThrow(() -> new RuntimeException("Sample not found: " + dto.sId() + "," + dto.sStamp()));
        var date = dto.dateExported() != null ? java.time.LocalDateTime.parse(dto.dateExported()) : null;

        pos.setBox(box);
        pos.setSample(sample);
        pos.setDateExported(date);
        return boxPosRepository.save(pos);
    }

    // DELETE bleibt vom AbstractCrudController (@DeleteMapping("/{id}"))
    // GET (paging/filter/sort) bleibt auch vom AbstractCrudController
}
