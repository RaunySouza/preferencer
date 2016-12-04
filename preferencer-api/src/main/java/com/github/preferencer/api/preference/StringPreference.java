package com.github.preferencer.api.preference;

import android.content.SharedPreferences;

/**
 * @author raunysouza
 */
public class StringPreference extends BasePreference<String> {

    public StringPreference(SharedPreferences sharedPreferences, String key, String defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public String get() {
        return getSharedPreferences().getString(getKey(), getDefaultValue());
    }

    @Override
    public void put(String value) {
        getSharedPreferences().edit().putString(getKey(), value).apply();
    }
}
