package com.github.preferencer.api;

import android.content.SharedPreferences;

/**
 * @author raunysouza
 */
public abstract class BaseSharedPreferenceEditor {

    private SharedPreferences.Editor editor;

    public BaseSharedPreferenceEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public void apply() {
        editor.apply();
    }

    protected SharedPreferences.Editor getEditor() {
        return editor;
    }
}
