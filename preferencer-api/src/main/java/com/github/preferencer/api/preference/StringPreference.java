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
    protected String internalGet(SharedPreferences preferences, String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, String value) {
        editor.putString(key, value);
    }

}
