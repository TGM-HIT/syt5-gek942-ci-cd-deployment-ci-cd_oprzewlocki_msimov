package com.oliwier.insyrest.controller;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.service.CrudService;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/samples")
public class SampleController extends AbstractCrudController<Sample, SampleId> {
    protected SampleController(CrudService<Sample, SampleId> service) {
        super(service);
    }
}
