package com.github.preferencer.processor.utils;

import com.google.common.base.CaseFormat;

/**
 * @author rauny.souza
 */
public final class NamingUtils {
    private NamingUtils() {}

    public static String getKeyName(String name) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
    }

    public static String getVariableName(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
    }

    public static String getMethodName(String name) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
    }
}
