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
    protected Boolean internalGet(SharedPreferences preferences, String key, Boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Boolean value) {
        editor.putBoolean(key, value);
    }
}
