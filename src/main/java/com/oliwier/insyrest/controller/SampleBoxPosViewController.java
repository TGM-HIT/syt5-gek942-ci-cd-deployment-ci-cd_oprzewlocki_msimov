package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.dto.SampleBoxPosViewDTO;
import com.oliwier.insyrest.entity.SampleBoxPosView;
import com.oliwier.insyrest.repository.SampleBoxPosViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sample-boxpos")
@RequiredArgsConstructor
public class SampleBoxPosViewController {

    private final SampleBoxPosViewRepository repository;

    @GetMapping
    public ResponseEntity<Page<SampleBoxPosViewDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        Sort sortObj = Sort.by(Sort.Direction.DESC, "sStamp");

        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<SampleBoxPosView> result = repository.findAll(pageable);

        Page<SampleBoxPosViewDTO> dtoPage = result.map(entity ->
                SampleBoxPosViewDTO.builder()
                        .sId(entity.getSId())
                        .sStamp(entity.getSStamp())
                        .boxpos(entity.getBoxpos())
                        .build()
        );

        return ResponseEntity.ok(dtoPage);
    }
}
