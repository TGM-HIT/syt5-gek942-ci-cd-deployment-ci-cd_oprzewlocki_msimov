package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.dto.request.LogRequest;
import com.oliwier.insyrest.dto.response.LogResponse;
import com.oliwier.insyrest.entity.Log;
import com.oliwier.insyrest.mapper.LogMapper;
import com.oliwier.insyrest.service.LogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogController extends AbstractCrudController<Log, Long, LogRequest, LogResponse> {

    public LogController(LogService logService, LogMapper logMapper) {
        super(logService, logMapper);
    }
}
