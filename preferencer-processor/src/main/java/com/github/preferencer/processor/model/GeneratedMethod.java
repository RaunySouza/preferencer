package com.github.preferencer.processor.model;

import java.util.Optional;

import javax.lang.model.element.ExecutableElement;

/**
 * @author rauny.souza
 */
public class GeneratedMethod {

    private boolean shouldGenerate = true;
    private ExecutableElement methodElement;

    public boolean shouldGenerate() {
        return shouldGenerate;
    }

    public void setShouldGenerate(boolean shouldGenerate) {
        this.shouldGenerate = shouldGenerate;
    }

    public Optional<ExecutableElement> getMethodElement() {
        return Optional.ofNullable(methodElement);
    }

    public void setMethodElement(ExecutableElement methodElement) {
        this.methodElement = methodElement;
    }
}
