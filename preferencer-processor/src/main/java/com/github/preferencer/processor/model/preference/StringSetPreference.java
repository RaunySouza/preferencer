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
        StringBuilder builder = new StringBuilder("new java.util.HashSet<String>(");
        String[] defaultValue = (String[]) getDefaultValue();
        if (defaultValue.length > 0) {
            builder.append("java.util.Arrays.asList(");
            StringJoiner joiner = new StringJoiner(",");
            for (String s : defaultValue) {
                joiner.add("\"" + s + "\"");
            }
            builder.append(joiner);
            builder.append(")");
        }

        builder.append(")");

        setDefaultValue(builder.toString());
    }

}
