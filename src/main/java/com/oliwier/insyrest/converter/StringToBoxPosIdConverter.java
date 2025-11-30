package com.oliwier.insyrest.converter;

import com.oliwier.insyrest.entity.id.BoxPosId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBoxPosIdConverter implements Converter<String, BoxPosId> {

    @Override
    public BoxPosId convert(String source) {
        String[] parts = source.split(",", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid BoxPosId format. Expected: bpos_id,b_id");
        }

        Integer bPosId = Integer.valueOf(parts[0].trim());
        String bId = parts[1].trim();


        return new BoxPosId(bPosId, bId);
    }
}