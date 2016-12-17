package com.github.preferencer.api.preference;

import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author raunysouza
 */
public class StringSetPreference extends BasePreference<Set<String>> {

    public StringSetPreference(SharedPreferences sharedPreferences, String key, String... defaultValue) {
        super(sharedPreferences, key, new HashSet<>(Arrays.asList(defaultValue)));
    }

    @Override
    protected Set<String> internalGet(SharedPreferences preferences, String key, Set<String> defaultValue) {
        return preferences.getStringSet(key, defaultValue);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Set<String> value) {
        editor.putStringSet(key, value);
    }

}
