package com.github.preferencer.api.preference;

import android.content.SharedPreferences;

/**
 * @author raunysouza
 */
public class LongPreference extends BasePreference<Long> {

    public LongPreference(SharedPreferences sharedPreferences, String key, Long defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public Long get() {
        return getSharedPreferences().getLong(getKey(), getDefaultValue());
    }

    @Override
    public void put(Long value) {
        getSharedPreferences().edit().putLong(getKey(), value).apply();
    }
}
