package com.github.preferencer.processor.model;

/**
 * @author rauny.souza
 */
public class PostConstructMethod {

    private String name;
    private boolean injectContext;

    public PostConstructMethod(String name, boolean injectContext) {
        this.name = name;
        this.injectContext = injectContext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInjectContext() {
        return injectContext;
    }

    public void setInjectContext(boolean injectContext) {
        this.injectContext = injectContext;
    }
}
