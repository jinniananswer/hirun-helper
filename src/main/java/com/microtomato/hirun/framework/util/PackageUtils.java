package com.microtomato.hirun.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Steven
 * @date 2019-12-20
 */
public final class PackageUtils {

    public static final String compactPackage(Class cls) {
        String className = cls.getName();
        return compactPackage(className);
    }

    public static final String compactPackage(String className) {
        StringBuilder sb = new StringBuilder();
        String[] split = StringUtils.split(className, '.');
        for (int i = 0; i < split.length - 1; i++) {
            sb.append(split[i].charAt(0)).append('.');
        }
        sb.append(split[split.length - 1]);
        return sb.toString();
    }

    public static void main(String[] args) {

    }
}
