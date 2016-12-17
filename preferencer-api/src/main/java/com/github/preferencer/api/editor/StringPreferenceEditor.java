package com.github.preferencer.api.editor;

import android.content.SharedPreferences;

import com.github.preferencer.api.BaseSharedPreferenceEditor;

/**
 * @author raunysouza
 */
public class StringPreferenceEditor<R extends BaseSharedPreferenceEditor> extends BasePreferenceEditor<String, R> {

    public StringPreferenceEditor(SharedPreferences.Editor editor, R sharedPreferenceEditor, String key) {
        super(editor, sharedPreferenceEditor, key);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, String value) {
        editor.putString(key, value);
    }
}
