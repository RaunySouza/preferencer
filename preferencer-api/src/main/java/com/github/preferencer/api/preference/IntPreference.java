package com.github.preferencer.api.preference;

import android.content.SharedPreferences;

/**
 * @author raunysouza
 */
public class IntPreference extends BasePreference<Integer> {

    public IntPreference(SharedPreferences sharedPreferences, String key, Integer defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public Integer get() {
        return getSharedPreferences().getInt(getKey(), getDefaultValue());
    }

    @Override
    public void put(Integer value) {
        getSharedPreferences().edit().putInt(getKey(), value).apply();
    }
}
