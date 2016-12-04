package com.github.preferencer.api.preference;

import android.content.SharedPreferences;

/**
 * @author raunysouza
 */
public class FloatPreference extends BasePreference<Float> {

    public FloatPreference(SharedPreferences sharedPreferences, String key, Float defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public Float get() {
        return getSharedPreferences().getFloat(getKey(), getDefaultValue());
    }

    @Override
    public void put(Float value) {
        getSharedPreferences().edit().putFloat(getKey(), value).apply();
    }
}
