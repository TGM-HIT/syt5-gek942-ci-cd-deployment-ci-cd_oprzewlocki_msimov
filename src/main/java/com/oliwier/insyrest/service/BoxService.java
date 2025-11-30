package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.Box;
import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.Sample;
import com.oliwier.insyrest.entity.id.SampleId;
import com.oliwier.insyrest.repository.BoxPosRepository;
import com.oliwier.insyrest.repository.BoxRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoxService extends AbstractCrudService<Box, String>{
    protected BoxService(BoxRepository repository, BoxRepository boxRepository, BoxPosRepository boxPosRepository, SampleRepository sampleRepository) {
        super(repository, repository);
        this.boxRepository = boxRepository;
        this.boxPosRepository = boxPosRepository;
        this.sampleRepository = sampleRepository;
    }

    private final BoxRepository boxRepository;
    private final BoxPosRepository boxPosRepository;
    private final SampleRepository sampleRepository;

    @Transactional(readOnly = true)
    public boolean existsLinkedToSample(SampleId id) {
        return boxPosRepository.existsBySampleId(id);
    }

    @Transactional
    public void deleteEmptyBoxes() {
        var all = boxRepository.findAll();
        all.stream()
                .filter(box -> boxPosRepository.countByBox(box) == 0)
                .forEach(boxRepository::delete);
    }

    @Transactional
    public void deleteBoxesBySample(SampleId id) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sample not found: " + id));

        List<BoxPos> boxPositions = boxPosRepository.findBySample(sample);
        if (boxPositions.isEmpty()) return;

        List<Box> boxes = boxPositions.stream()
                .map(BoxPos::getBox)
                .distinct()
                .toList();

        boxPosRepository.deleteAll(boxPositions);

        for (Box box : boxes) {
            if (boxPosRepository.countByBox(box) == 0) {
                boxRepository.delete(box);
            }
        }
    }

    @Transactional(readOnly = true)
    public boolean hasBoxPos(String bId) {
        return !boxPosRepository.findAllByBoxId(bId).isEmpty();
    }

    @Transactional
    public void cascadeDeleteBox(String bId) {
        boxPosRepository.deleteAllByBoxId(bId);
        boxRepository.deleteById(bId);
    }
}
