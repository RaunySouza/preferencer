package com.github.preferencer.processor.model.preference;

import com.github.preferencer.processor.model.Preference;
import com.squareup.javapoet.ClassName;

/**
 * @author raunysouza
 */
public class LongPreference extends Preference {

    public LongPreference(String name, Object defaultValue, ClassName type) {
        super(name, defaultValue, type);
    }

    @Override
    protected String getValueFormat() {
        return "$LL";
    }


}
