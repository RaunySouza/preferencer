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
    public Set<String> get() {
        return getSharedPreferences().getStringSet(getKey(), getDefaultValue());
    }

    @Override
    public void put(Set<String> value) {
        getSharedPreferences().edit().putStringSet(getKey(), value).apply();
    }
}
