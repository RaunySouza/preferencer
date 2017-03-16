package com.github.preferencer.api;

import android.content.SharedPreferences;

/**
 * @author raunysouza
 */
public abstract class BaseSharedPreferenceEditor implements AutoCloseable {

    private SharedPreferences.Editor editor;
    private boolean successfully = true;

    public BaseSharedPreferenceEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public void apply() {
        if (successfully) {
            editor.apply();
        }
    }

    protected SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void beginTransaction() {
        successfully = false;
    }

    public void setSuccessfully() {
        successfully = true;
    }

    @Override
    public void close() {
        apply();
    }
}
