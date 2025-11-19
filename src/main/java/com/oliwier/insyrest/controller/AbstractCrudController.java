package com.oliwier.insyrest.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliwier.insyrest.service.CrudService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCrudController<T, ID> {

    private final CrudService<T, ID> service;

    private final ObjectMapper mapper;


    protected AbstractCrudController(CrudService<T, ID> service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }


    @GetMapping
    public Page<T> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam MultiValueMap<String, String> params
    ) {
        Map<String, String> filter = extractSubMap(params, "filter");
        Map<String, String> sort   = extractSubMap(params, "sort");

        System.out.println("FILTER = " + filter);
        System.out.println("SORT = " + sort);

        return service.findAllWithFilters(PageRequest.of(page, size), filter, sort);
    }

    Map<String, String> extractSubMap(MultiValueMap<String, String> params, String prefix) {
        Map<String, String> result = new HashMap<>();
        String start = prefix + "[";
        int startLen = start.length();

        for (String key : params.keySet()) {
            if (key.startsWith(start) && key.endsWith("]")) {
                String subKey = key.substring(startLen, key.length() - 1); // e.g. filter[name] → "name"
                result.put(subKey, params.getFirst(key));
            }
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        return entity.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public T create(@Valid @RequestBody T entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody Map<String, Object> updated) throws JsonMappingException {
        Optional<T> existingOpt = service.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        T existing = existingOpt.get();
        mapper.updateValue(existing, updated);
        return ResponseEntity.ok(service.save(existing));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<T> patch(@PathVariable ID id, @RequestBody Map<String, Object> updates) throws JsonMappingException {
        Optional<T> existingOpt = service.findById(id);
        if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

        T existing = existingOpt.get();
        mapper.updateValue(existing, updates);
        return ResponseEntity.ok(service.save(existing));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        return service.findById(id)
                .<ResponseEntity<?>>map(e -> {
                    try {
                        service.deleteById(id);
                        return ResponseEntity.noContent().build();
                    } catch (org.springframework.dao.DataIntegrityViolationException ex) {
                        ex.getMostSpecificCause();
                        String msg = ex.getMostSpecificCause().getMessage();
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of(
                                        "code", "FK_CONSTRAINT",
                                        "message", "Entity is still referenced",
                                        "detail", msg
                                ));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

}