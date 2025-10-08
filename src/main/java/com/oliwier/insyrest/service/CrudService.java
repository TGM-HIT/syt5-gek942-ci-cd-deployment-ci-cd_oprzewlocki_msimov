package com.oliwier.insyrest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CrudService<T, ID> {
    Page<T> findAll(Pageable pageable);

    Optional<T> findById(ID id);

    T save(T entity);

    void deleteById(ID id);
}
