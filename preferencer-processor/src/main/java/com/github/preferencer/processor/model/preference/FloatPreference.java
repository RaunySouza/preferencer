package com.github.preferencer.processor.model.preference;

import com.github.preferencer.processor.model.Preference;
import com.squareup.javapoet.ClassName;

/**
 * @author raunysouza
 */
public class FloatPreference extends Preference {

    public FloatPreference(String name, Object defaultValue, ClassName type) {
        super(name, defaultValue, type);
    }

    @Override
    protected String getValueFormat() {
        return "$LF";
    }


}
