package com.github.preferencer.processor.model.preference;

import com.github.preferencer.processor.model.Preference;
import com.squareup.javapoet.ClassName;

/**
 * @author raunysouza
 */
public class StringPreference extends Preference {

    public StringPreference(String name, Object defaultValue, ClassName type) {
        super(name, defaultValue, type);
    }

    @Override
    protected String getValueFormat() {
        return "$S";
    }
}