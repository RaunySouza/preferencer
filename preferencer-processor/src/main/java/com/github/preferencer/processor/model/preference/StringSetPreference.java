package com.github.preferencer.processor.model.preference;

import com.squareup.javapoet.ClassName;

import java.util.StringJoiner;

/**
 * @author raunysouza
 */
public class StringSetPreference extends LiteralPreference {

    public StringSetPreference(String name, Object defaultValue, ClassName type) {
        super(name, defaultValue, type);
        convertDefaultValue();
    }

    private void convertDefaultValue() {
        String[] defaultValue = (String[]) getDefaultValue();
        if (defaultValue.length > 0) {
            StringJoiner joiner = new StringJoiner(", ");
            for (String s : defaultValue) {
                joiner.add("\"" + s + "\"");
            }
            setDefaultValue(joiner.toString());
        } else {
            setDefaultValue("\"\"");
        }
    }

}
