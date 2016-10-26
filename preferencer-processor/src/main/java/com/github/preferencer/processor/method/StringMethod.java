package com.github.preferencer.processor.method;

/**
 * @author raunysouza
 */
public class StringMethod implements SharedPreferencesMethod {

    @Override
    public String get() {
        return "getString";
    }

    @Override
    public String put() {
        return "putString";
    }

    @Override
    public String defaultValue() {
        return "\"\"";
    }
}
