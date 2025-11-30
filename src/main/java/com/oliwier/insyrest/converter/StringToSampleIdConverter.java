package com.oliwier.insyrest.converter;

import com.oliwier.insyrest.entity.id.SampleId;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToSampleIdConverter implements Converter<String, SampleId> {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter SPACE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public SampleId convert(String source) {
        String[] parts = source.split(",", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid SampleId format. Expected: s_id,yyyy-MM-dd'T'HH:mm:ss");
        }

        String sId = parts[0].trim();
        String rawStamp = parts[1].trim();

        LocalDateTime parsedStamp;
        try {
            parsedStamp = LocalDateTime.parse(rawStamp, ISO);
        } catch (Exception e) {
            parsedStamp = LocalDateTime.parse(rawStamp, SPACE);
        }

        return new SampleId(sId, parsedStamp);
    }
}
