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
    protected Float internalGet(SharedPreferences preferences, String key, Float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Float value) {
        editor.putFloat(key, value);
    }

}
