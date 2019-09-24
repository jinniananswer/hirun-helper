package com.microtomato.hirun.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Map的工具类，可以用来判断是否为空或者取值上的简便操作
 *
 * @author jinnian
 * @date 2018/2/20 23:55
 */
public class MapUtils {

    public static boolean isNotEmpty(Map map) {
        if (map == null) {
            return false;
        }

        if (map.isEmpty()) {
            return false;
        }

        return true;
    }

    public static boolean isEmpty(Map map) {
        return !isNotEmpty(map);
    }

    public static String getValue(Map map, String key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }

        Object valueObj = map.get(key);
        if (valueObj == null) {
            return defaultValue;
        }

        String value = valueObj.toString();
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        return value;
    }
}
