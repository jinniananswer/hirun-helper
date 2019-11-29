package com.microtomato.hirun.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microtomato.hirun.modules.system.entity.dto.UnReadedDTO;

import java.time.LocalDateTime;

/**
 * Json 工具类，基于 Jackson 的实现
 *
 * @author Steven
 * @date 2019-04-29
 */
public final class JsonUtils {

    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());

        // 配置信息，当前使用正常的情况下，先采用默认配置。
//        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
//        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    /**
     * Java 对象序列化成 Json 字符串
     *
     * @param o
     * @return
     */
    public static String encode(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json 串反序列化成 Java 对象
     *
     * @param json
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T decode(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        UnReadedDTO dto = new UnReadedDTO();
        dto.setCreateTime(LocalDateTime.now());
        dto.setName("金念");

        System.out.println(JsonUtils.encode(dto));
    }

}
