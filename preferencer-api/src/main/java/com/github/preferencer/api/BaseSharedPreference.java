package com.github.preferencer.api;

import android.content.SharedPreferences;

import com.github.preferencer.api.preference.BooleanPreference;
import com.github.preferencer.api.preference.FloatPreference;
import com.github.preferencer.api.preference.IntPreference;
import com.github.preferencer.api.preference.LongPreference;
import com.github.preferencer.api.preference.StringPreference;
import com.github.preferencer.api.preference.StringSetPreference;

/**
 * @author raunysouza
 */
public abstract class BaseSharedPreference {

    private SharedPreferences sharedPreferences;

    public BaseSharedPreference(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    protected StringPreference createStringPreference(String key, String defaultValue) {
        return new StringPreference(sharedPreferences, key, defaultValue);
    }

    protected BooleanPreference createBooleanPreference(String key, Boolean defaultValue) {
        return new BooleanPreference(sharedPreferences, key, defaultValue);
    }

    protected IntPreference createIntPreference(String key, Integer defaultValue) {
        return new IntPreference(sharedPreferences, key, defaultValue);
    }

    protected LongPreference createLongPreference(String key, Long defaultValue) {
        return new LongPreference(sharedPreferences, key, defaultValue);
    }

    protected FloatPreference createFloatPreference(String key, Float defaultValue) {
        return new FloatPreference(sharedPreferences, key, defaultValue);
    }

    protected StringSetPreference createStringSetPreference(String key, String... defaultValue) {
        return new StringSetPreference(sharedPreferences, key, defaultValue);
    }

    protected SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
