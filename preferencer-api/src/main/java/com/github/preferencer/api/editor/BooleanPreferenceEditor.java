package com.github.preferencer.api.editor;

import android.content.SharedPreferences;

import com.github.preferencer.api.BaseSharedPreferenceEditor;

/**
 * @author raunysouza
 */
public class BooleanPreferenceEditor<R extends BaseSharedPreferenceEditor> extends BasePreferenceEditor<Boolean, R> {

    public BooleanPreferenceEditor(SharedPreferences.Editor editor, R sharedPreferenceEditor, String key) {
        super(editor, sharedPreferenceEditor, key);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Boolean value) {
        editor.putBoolean(key, value);
    }
}
