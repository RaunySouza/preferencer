package com.github.preferencer.processor.method;

/**
 * @author raunysouza
 */
public class BooleanMethod implements SharedPreferencesMethod {

    @Override
    public String get() {
        return "getBoolean";
    }

    @Override
    public String put() {
        return "putBoolean";
    }

    @Override
    public String defaultValue() {
        return "false";
    }
}
