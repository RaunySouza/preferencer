package com.github.preferencer.processor.model;

import com.github.preferencer.processor.utils.NamingUtils;

import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author raunysouza
 */
public class Preference {

    private ExecutableElement methodElement;
    private TypeMirror type;
    private String name;
    private String keyName;
    private String defaultValue;
    private GeneratedMethod setter;
    private GeneratedMethod remover;

    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    public void setMethodElement(ExecutableElement methodElement) {
        this.methodElement = methodElement;
    }

    public TypeMirror getType() {
        return type;
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyName() {
        return !StringUtils.isEmpty(keyName) ? keyName : NamingUtils.getKeyName(name);
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public GeneratedMethod getSetter() {
        return setter;
    }

    public void setSetter(GeneratedMethod setter) {
        this.setter = setter;
    }

    public GeneratedMethod getRemover() {
        return remover;
    }

    public void setRemover(GeneratedMethod remover) {
        this.remover = remover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preference that = (Preference) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
