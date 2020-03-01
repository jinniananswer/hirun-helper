package com.microtomato.hirun.framework.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Steven
 * @date 2020-03-01
 */
@Component
public class JsonLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {

        String szLocalDateTime = parser.getText();

        if (StringUtils.isBlank(szLocalDateTime)) {
            System.err.println("invalid date supplied for conversion,ignored.");
            return null;
        }
        return LocalDateTime.parse(parser.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("GMT+8")));
    }
}