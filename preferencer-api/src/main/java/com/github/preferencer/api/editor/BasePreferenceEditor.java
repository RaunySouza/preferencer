package com.github.preferencer.api.editor;

import android.content.SharedPreferences;

import com.github.preferencer.api.BaseSharedPreferenceEditor;

/**
 * @author raunysouza
 */
public abstract class BasePreferenceEditor<T, R extends BaseSharedPreferenceEditor> {

    private R sharedPreferenceEditor;
    private SharedPreferences.Editor editor;
    private String key;

    BasePreferenceEditor(SharedPreferences.Editor editor, R sharedPreferenceEditor, String key) {
        this.editor = editor;
        this.sharedPreferenceEditor = sharedPreferenceEditor;
        this.key = key;
    }

    protected abstract void internalPut(SharedPreferences.Editor editor, String key, T value);

    public R put(T value) {
        internalPut(editor, key, value);
        return sharedPreferenceEditor;
    }

    public R remove() {
        editor.remove(key);
        return sharedPreferenceEditor;
    }
}
