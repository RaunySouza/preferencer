package com.github.preferencer.api.editor;

import android.content.SharedPreferences;

import com.github.preferencer.api.BaseSharedPreferenceEditor;

/**
 * @author raunysouza
 */
public class IntPreferenceEditor<R extends BaseSharedPreferenceEditor> extends BasePreferenceEditor<Integer, R> {

    public IntPreferenceEditor(SharedPreferences.Editor editor, R sharedPreferenceEditor, String key) {
        super(editor, sharedPreferenceEditor, key);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Integer value) {
        editor.putInt(key, value);
    }
}
