package com.github.preferencer.processor.model;

import com.github.preferencer.processor.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

/**
 * @author raunysouza
 */
public abstract class Preference {

    private String name;
    private Object defaultValue;
    private ClassName type;

    public Preference(String name, Object defaultValue, ClassName type) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ClassName getType() {
        return type;
    }

    public void setType(ClassName type) {
        this.type = type;
    }

    protected abstract String getValueFormat();

    public void createStatement(MethodSpec.Builder builder) {
        builder.addStatement(String.format("return create$L($S, %s)", getValueFormat()),
                NamingUtils.getMethodName(getType().simpleName()),
                NamingUtils.getKeyName(getName()), getDefaultValue());
    }
}
