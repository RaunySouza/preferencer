package com.github.preferencer.api.editor;

import android.content.SharedPreferences;

import com.github.preferencer.api.BaseSharedPreferenceEditor;

import java.util.Set;

/**
 * @author raunysouza
 */
public class StringSetPreferenceEditor<R extends BaseSharedPreferenceEditor> extends BasePreferenceEditor<Set<String>, R> {

    public StringSetPreferenceEditor(SharedPreferences.Editor editor, R sharedPreferenceEditor, String key) {
        super(editor, sharedPreferenceEditor, key);
    }

    @Override
    protected void internalPut(SharedPreferences.Editor editor, String key, Set<String> value) {
        editor.putStringSet(key, value);
    }
}
