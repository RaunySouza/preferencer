package com.github.preferencer.processor.model;

import com.github.preferencer.processor.utils.NamingUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

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
    private boolean shouldGenerateSetter;
    private Optional<ExecutableElement> setterMethodElement = Optional.empty();

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

    public boolean isShouldGenerateSetter() {
        return shouldGenerateSetter;
    }

    public void setShouldGenerateSetter(boolean shouldGenerateSetter) {
        this.shouldGenerateSetter = shouldGenerateSetter;
    }

    public Optional<ExecutableElement> getSetterMethodElement() {
        return setterMethodElement;
    }

    public void setSetterMethodElement(ExecutableElement setterMethodElement) {
        this.setterMethodElement = Optional.ofNullable(setterMethodElement);
    }
}
