package com.github.preferencer.processor.utils;

import com.squareup.javapoet.ClassName;

/**
 * @author raunysouza
 */
public final class ClassNameUtil {
    private ClassNameUtil() {}

    // Android
    public static final ClassName context                 = ClassName.get("android.content", "Context");
    public static final ClassName preferenceManager       = ClassName.get("android.preference", "PreferenceManager");
    public static final ClassName sharedPreferences       = ClassName.get("android.content", "SharedPreferences");
    public static final ClassName sharedPreferencesEditor = ClassName.get("android.content.SharedPreferences", "Editor");

    // API
    public static final ClassName baseSharedPreference = ClassName.get("com.github.preferencer.api", "BaseSharedPreference");
    public static final ClassName stringPreference     = ClassName.get("com.github.preferencer.api.preference", "StringPreference");
    public static final ClassName booleanPreference    = ClassName.get("com.github.preferencer.api.preference", "BooleanPreference");
    public static final ClassName intPreference        = ClassName.get("com.github.preferencer.api.preference", "IntPreference");
    public static final ClassName longPreference       = ClassName.get("com.github.preferencer.api.preference", "LongPreference");
    public static final ClassName floatPreference      = ClassName.get("com.github.preferencer.api.preference", "FloatPreference");
    public static final ClassName stringSetPreference  = ClassName.get("com.github.preferencer.api.preference", "StringSetPreference");

    public static final ClassName baseSharedPreferenceEditor = ClassName.get("com.github.preferencer.api", "BaseSharedPreferenceEditor");

}
