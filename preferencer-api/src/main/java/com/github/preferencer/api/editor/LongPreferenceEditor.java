package com.github.preferencer.api.editor;

import android.content.SharedPreferences;

import com.github.preferencer.api.BaseSharedPreferenceEditor;

/**
 * @author raunysouza
 */
public class LongPreferenceEditor<R extends BaseSharedPreferenceEditor> extends BasePreferenceEditor<Long, R> {

    public LongPreferenceEditor(SharedPreferences.Editor editor, R sharedPreferenceEditor, String key) {
        super(editor, sharedPreferenceEditor, key);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Long value) {
        editor.putFloat(key, value);
    }
}
