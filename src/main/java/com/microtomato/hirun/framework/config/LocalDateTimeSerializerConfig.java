package com.microtomato.hirun.framework.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 对类型为 LocalDateTime 的字段进行格式转换
 *
 * @author Steven
 */
@Configuration
public class LocalDateTimeSerializerConfig {

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;

    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }

    /**
     * Json 对象里的 String 自动转换成 LocalDateTime
     *
     * @return 转换器
     */
    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConvert() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }

                switch (source.length()) {
                    case 8:
                        // 格式: yyyyMMDD
                        return LocalDateTime.parse(source + " 00:00:00", DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
                    case 10:
                        // 格式: yyyy-MM-DD
                        return LocalDateTime.parse(source + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    default:
                        // 格式: yyyy-MM-dd HH:mm:ss
                        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            }
        };
    }

    /**
     * Json 对象里的 String 自动转换成 LocalDateTime
     *
     * @return 转换器
     */
    @Bean
    public Converter<String, LocalDate> LocalDateConvert() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }

                switch (source.length()) {
                    case 8:
                        // 格式: yyyyMMDD
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyyMMdd"));
                    case 10:
                        // 格式: yyyy-MM-DD
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    default:
                        // 格式: yyyy-MM-dd HH:mm:ss
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
            }
        };
    }
}
