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
    protected Long internalGet(SharedPreferences preferences, String key, Long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Long value) {
        editor.putLong(key, value);
    }

}
