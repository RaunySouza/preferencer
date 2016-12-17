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
    protected Integer internalGet(SharedPreferences preferences, String key, Integer defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Integer value) {
        editor.putInt(key, value);
    }

}
