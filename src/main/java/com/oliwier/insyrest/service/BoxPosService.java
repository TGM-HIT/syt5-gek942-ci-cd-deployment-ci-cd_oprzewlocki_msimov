package com.oliwier.insyrest.service;

import com.oliwier.insyrest.entity.BoxPos;
import com.oliwier.insyrest.entity.BoxPosId;
import com.oliwier.insyrest.entity.SampleId;
import com.oliwier.insyrest.repository.BoxPosRepository;
import com.oliwier.insyrest.repository.BoxRepository;
import com.oliwier.insyrest.repository.SampleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BoxPosService extends AbstractCrudService<BoxPos, BoxPosId>{
    private final BoxPosRepository boxPosRepository;
    private final SampleRepository sampleRepository;
    private final BoxRepository boxRepository;




    protected BoxPosService(BoxPosRepository repository, BoxPosRepository boxPosRepository, SampleRepository sampleRepository, BoxRepository boxRepository) {
        super(repository, repository);
        this.boxPosRepository = boxPosRepository;
        this.sampleRepository = sampleRepository;
        this.boxRepository = boxRepository;
    }

    @Transactional
    public void deleteAllBySample(SampleId id) {
        sampleRepository.findById(id).ifPresent(boxPosRepository::deleteAllBySample);
    }

    @Transactional(readOnly = true)
    public boolean existsForSample(SampleId id) {
        return sampleRepository.findById(id)
                .map(s -> !boxPosRepository.findBySample(s).isEmpty())
                .orElse(false);
    }


    @Override
    @Transactional
    public BoxPos save(BoxPos pos) {

        // ===== PATCH ID FROM FLAT FIELDS =====
        if (pos.getId() == null) {
            Integer bposId = null;
            String bId = null;

            // extract from boxpos fields (JSON flat)
            try {
                bposId = (Integer) pos.getClass().getMethod("getBposId").invoke(pos);
            } catch (Exception ignored) {}

            try {
                bId = (String) pos.getClass().getMethod("getBId").invoke(pos);
            } catch (Exception ignored) {}

            if (bposId == null || bId == null)
                throw new IllegalArgumentException("bposId or bId missing (frontend must send them)");

            pos.setId(new BoxPosId(bposId, bId)); // ✅ now id is real
        }

        // ===== RESOLVE BOX =====
        var box = boxRepository.findById(pos.getId().getBId())
                .orElseThrow(() -> new RuntimeException("Box not found: " + pos.getId().getBId()));
        pos.setBox(box);

        // ===== RESOLVE SAMPLE =====
        if (pos.getSample() == null || pos.getSample().getId() == null) {
            // We must build SampleId manually from flat fields
            String sId = null;
            String sStamp = null;

            try { sId = (String) pos.getClass().getMethod("getSId").invoke(pos); } catch (Exception ignored) {}
            try { sStamp = (String) pos.getClass().getMethod("getSStamp").invoke(pos); } catch (Exception ignored) {}

            if (sId != null && sStamp != null) {
                var sampleId = new SampleId(sId, LocalDateTime.parse(sStamp)
                );
                String finalSId = sId;
                var sample = sampleRepository.findById(sampleId)
                        .orElseThrow(() -> new RuntimeException("Sample not found: " + finalSId));
                pos.setSample(sample);
            }
        }

        return boxPosRepository.save(pos);
    }

}
