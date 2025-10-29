package com.oliwier.insyrest.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public abstract class AbstractCrudService<T, ID> implements CrudService<T, ID> {

    private final JpaRepository<T, ID> repository;
    protected final JpaSpecificationExecutor<T> specRepository;

    protected AbstractCrudService(JpaRepository<T, ID> repository, JpaSpecificationExecutor<T> specRepository) {
        this.repository = repository;
        this.specRepository = specRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        repository.deleteById(id);
    }


    @Override
    @Transactional
    public Page<T> findAllWithFilters(PageRequest pageable,
                                      Map<String, String> filter,
                                      Map<String, String> sort) {

        filter = (filter == null) ? Collections.emptyMap() : filter;
        sort = (sort == null) ? Collections.emptyMap() : sort;

        Specification<T> spec = Specification.where(null);

        for (Map.Entry<String, String> entry : filter.entrySet()) {
            final String field = mapFieldName(entry.getKey());
            final String value = entry.getValue();

            if (field == null || field.isBlank() || value == null || value.isBlank()) continue;

            Specification<T> fieldSpec = (root, query, cb) -> {
                try {
                    Path<?> p = root;
                    for (String segment : field.split("\\.")) p = p.get(segment);
                    if (p == null) return null;

                    Class<?> type = p.getJavaType();
                    String raw = value.trim();

                    // Handle OR and AND filters
                    if (raw.contains("|")) {
                        Path<?> finalP = p;
                        List<Predicate> ors = Arrays.stream(raw.split("\\|"))
                                .map(v -> buildPredicate(cb, finalP, type, v.trim()))
                                .filter(Objects::nonNull)
                                .toList();
                        return cb.or(ors.toArray(new Predicate[0]));
                    }
                    if (raw.contains("&")) {
                        Path<?> finalP1 = p;
                        List<Predicate> ands = Arrays.stream(raw.split("&"))
                                .map(v -> buildPredicate(cb, finalP1, type, v.trim()))
                                .filter(Objects::nonNull)
                                .toList();
                        return cb.and(ands.toArray(new Predicate[0]));
                    }

                    return buildPredicate(cb, p, type, raw);

                } catch (Exception e) {
                    System.err.println("Filter skipped for field '" + field + "': " + e.getMessage());
                    return null;
                }
            };

            spec = (spec == null) ? fieldSpec : spec.and(fieldSpec);
        }

        // ===== SORT =====
        Pageable sortedPageable = pageable;
        if (!sort.isEmpty()) {
            try {
                List<Sort.Order> orders = sort.entrySet().stream()
                        .filter(e -> e.getKey() != null && !e.getKey().isBlank())
                        .map(e -> new Sort.Order(
                                "desc".equalsIgnoreCase(e.getValue()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                mapFieldName(e.getKey())
                        ))
                        .toList();

                if (!orders.isEmpty()) {
                    sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
                }
            } catch (Exception e) {
                System.err.println("Sorting skipped: " + e.getMessage());
            }
        }

        Specification<T> finalSpec = (spec != null) ? spec : (root, query, cb) -> cb.conjunction();
        return specRepository.findAll(finalSpec, sortedPageable);
    }

    @SuppressWarnings("unchecked")
    private static Predicate buildPredicate(CriteriaBuilder cb, Path<?> p, Class<?> type, String raw) {
        try {
            // Detect operator
            String[] ops = {">=", "<=", "!=", ">", "<", "~", "="};
            String op = "=";
            String value = raw;
            for (String o : ops) {
                if (raw.contains(o)) {
                    op = o;
                    value = raw.substring(raw.indexOf(o) + o.length()).trim();
                    break;
                }
            }

            // ===== NULL checks =====
            if ("null".equalsIgnoreCase(value))  return cb.isNull(p);
            if ("!null".equalsIgnoreCase(value)) return cb.isNotNull(p);

            // ===== STRING =====
            if (String.class.equals(type)) {
                Expression<String> expr = p.as(String.class);
                String valLower = value.toLowerCase();
                return switch (op) {
                    case "~"  -> cb.like(cb.lower(expr), "%" + valLower + "%");
                    case "!=" -> cb.notLike(cb.lower(expr), "%" + valLower + "%");
                    default   -> cb.like(cb.lower(expr), "%" + valLower + "%");
                };
            }

            // ===== NUMBER =====
            if (Number.class.isAssignableFrom(type)) {
                Expression<BigDecimal> expr = p.as(BigDecimal.class);

                // Range syntax: 10..20
                if (value.contains("..")) {
                    String[] parts = value.split("\\.\\.");
                    var start = new BigDecimal(parts[0]);
                    var end = new BigDecimal(parts[1]);
                    return cb.and(
                            cb.greaterThanOrEqualTo(expr, start),
                            cb.lessThanOrEqualTo(expr, end)
                    );
                }

                BigDecimal num = new BigDecimal(value);
                return switch (op) {
                    case ">"  -> cb.greaterThan(expr, num);
                    case "<"  -> cb.lessThan(expr, num);
                    case ">=" -> cb.greaterThanOrEqualTo(expr, num);
                    case "<=" -> cb.lessThanOrEqualTo(expr, num);
                    case "!=" -> cb.notEqual(expr, num);
                    default   -> cb.equal(expr, num);
                };
            }

            // ===== DATES =====
            if (java.time.temporal.Temporal.class.isAssignableFrom(type)) {
                Expression<?> expr = p.as(type);

                // Range with ..
                if (value.contains("..")) {
                    String[] parts = value.split("\\.\\.");
                    if (type.equals(LocalDateTime.class)) {
                        var start = LocalDateTime.parse(parts[0]);
                        var end = LocalDateTime.parse(parts[1]);
                        return cb.between((Expression<LocalDateTime>) expr, start, end);
                    } else {
                        var start = LocalDate.parse(parts[0]);
                        var end = LocalDate.parse(parts[1]);
                        return cb.between((Expression<LocalDate>) expr, start, end);
                    }
                }

                // Try full parse
                try {
                    if (type.equals(LocalDateTime.class)) {
                        var dt = LocalDateTime.parse(value);
                        return switch (op) {
                            case ">"  -> cb.greaterThan((Expression<LocalDateTime>) expr, dt);
                            case "<"  -> cb.lessThan((Expression<LocalDateTime>) expr, dt);
                            case ">=" -> cb.greaterThanOrEqualTo((Expression<LocalDateTime>) expr, dt);
                            case "<=" -> cb.lessThanOrEqualTo((Expression<LocalDateTime>) expr, dt);
                            case "!=" -> cb.notEqual(expr, dt);
                            default   -> cb.equal(expr, dt);
                        };
                    } else {
                        var d = LocalDate.parse(value);
                        return switch (op) {
                            case ">"  -> cb.greaterThan((Expression<LocalDate>) expr, d);
                            case "<"  -> cb.lessThan((Expression<LocalDate>) expr, d);
                            case ">=" -> cb.greaterThanOrEqualTo((Expression<LocalDate>) expr, d);
                            case "<=" -> cb.lessThanOrEqualTo((Expression<LocalDate>) expr, d);
                            case "!=" -> cb.notEqual(expr, d);
                            default   -> cb.equal(expr, d);
                        };
                    }
                } catch (DateTimeParseException ignored) {
                    // Partial date (year/month)
                    if (type.equals(LocalDateTime.class)) {
                        Expression<String> dateStr = cb.function("TO_CHAR", String.class, p,
                                cb.literal("YYYY-MM-DD\"T\"HH24:MI:SS"));
                        return cb.like(dateStr, value + "%");
                    } else {
                        Expression<String> dateStr = cb.function("TO_CHAR", String.class, p,
                                cb.literal("YYYY-MM-DD"));
                        return cb.like(dateStr, value + "%");
                    }
                }
            }

            // ===== BOOLEAN =====
            if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                Boolean b = Boolean.parseBoolean(value);
                return switch (op) {
                    case "!=" -> cb.notEqual(p, b);
                    default   -> cb.equal(p, b);
                };
            }

            // ===== FALLBACK =====
            return cb.equal(p, value);

        } catch (Exception e) {
            System.err.println("Predicate failed: " + e.getMessage());
            return null;
        }
    }



    private String mapFieldName(String field) {
        return switch (field) {
            case "s_stamp" -> "id.sStamp";
            case "s_id" -> "id.sId";
            default -> field;
        };
    }
}

