package com.github.preferencer.processor.method;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rauny.souza
 */
public class SharedPreferenceMethodResolver {

    private Map<TypeAvailable, SharedPreferencesMethod> methodMap = new HashMap<>();

    public SharedPreferenceMethodResolver() {
        methodMap.put(TypeAvailable.BOOLEAN, new BooleanMethod());
        methodMap.put(TypeAvailable.FLOAT, new FloatMethod());
        methodMap.put(TypeAvailable.INT, new IntMethod());
        methodMap.put(TypeAvailable.LONG, new LongMethod());
        methodMap.put(TypeAvailable.STRING, new StringMethod());
        methodMap.put(TypeAvailable.STRING_SET, new StringSetMethod());
    }

    enum TypeAvailable {
        BOOLEAN, FLOAT, INT, LONG, STRING, STRING_SET
    }

    public SharedPreferencesMethod getMethod(String type) {
        TypeAvailable typeAvailable = null;
        switch (type) {
            case "boolean":
            case "java.lang.Boolean":
                typeAvailable = TypeAvailable.BOOLEAN;
                break;
            case "float":
            case "java.lang.Float":
                typeAvailable = TypeAvailable.FLOAT;
                break;
            case "int":
            case "java.lang.Integer":
                typeAvailable = TypeAvailable.INT;
                break;
            case "long":
            case "java.lang.Long":
                typeAvailable = TypeAvailable.LONG;
                break;
            case "java.lang.String":
                typeAvailable = TypeAvailable.STRING;
                break;
            case "java.util.Set<java.lang.String>":
                typeAvailable = TypeAvailable.STRING_SET;
                break;
        }

        return typeAvailable != null ? methodMap.get(typeAvailable) : null;
    }
}
