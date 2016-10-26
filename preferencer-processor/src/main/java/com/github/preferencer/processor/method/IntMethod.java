package com.github.preferencer.processor.method;

/**
 * @author raunysouza
 */
public class IntMethod implements SharedPreferencesMethod {

    @Override
    public String get() {
        return "getInt";
    }

    @Override
    public String put() {
        return "putInt";
    }

    @Override
    public String defaultValue() {
        return "0";
    }

}
