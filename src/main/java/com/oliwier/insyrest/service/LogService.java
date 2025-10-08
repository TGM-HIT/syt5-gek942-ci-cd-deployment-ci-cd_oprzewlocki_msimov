package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Log;
import com.oliwier.insyrest.repository.LogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogService {

    private final LogRepository repository;

    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public Page<Log> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<Log> findById(Long id) {
        return repository.findById(id);
    }

    public Log save(Log log) {
        return repository.save(log);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
