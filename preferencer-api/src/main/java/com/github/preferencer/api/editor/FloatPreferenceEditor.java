package com.github.preferencer.api.editor;

import android.content.SharedPreferences;

import com.github.preferencer.api.BaseSharedPreferenceEditor;

/**
 * @author raunysouza
 */
public class FloatPreferenceEditor<R extends BaseSharedPreferenceEditor> extends BasePreferenceEditor<Float, R> {

    public FloatPreferenceEditor(SharedPreferences.Editor editor, R sharedPreferenceEditor, String key) {
        super(editor, sharedPreferenceEditor, key);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Float value) {
        editor.putFloat(key, value);
    }
}
