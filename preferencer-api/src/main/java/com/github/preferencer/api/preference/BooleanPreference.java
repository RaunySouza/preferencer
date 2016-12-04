package com.github.preferencer.api.preference;

import android.content.SharedPreferences;

/**
 * @author raunysouza
 */
public class BooleanPreference extends BasePreference<Boolean> {

    public BooleanPreference(SharedPreferences sharedPreferences, String key, Boolean defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public Boolean get() {
        return getSharedPreferences().getBoolean(getKey(), getDefaultValue());
    }

    @Override
    public void put(Boolean value) {
        getSharedPreferences().edit().putBoolean(getKey(), value).apply();
    }
}
