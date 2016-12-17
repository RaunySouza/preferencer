package com.github.preferencer.api;

import android.content.SharedPreferences;

import com.github.preferencer.api.editor.StringPreferenceEditor;
import com.github.preferencer.api.preference.StringPreference;
import com.github.preferencer.api.preference.StringSetPreference;

public class PreferenceMock extends BaseSharedPreference {

    public PreferenceMock(SharedPreferences sharedPreferences) {
        super(sharedPreferences);
    }

    public StringPreference name() {
        return createStringPreference("name", "");
    }

    public StringPreference nameWithDefault() {
        return createStringPreference("name_with_default", "default");
    }

    public StringSetPreference people() {
        return createStringSetPreference("people", new String[0]);
    }

    public StringSetPreference animals() {
        return createStringSetPreference("animals", "cow", "horse", "dog", "cat");
    }

    public PreferenceMockEditor edit() {
        return new PreferenceMockEditor(getSharedPreferences().edit());
    }

    public class PreferenceMockEditor extends BaseSharedPreferenceEditor {

        public PreferenceMockEditor(SharedPreferences.Editor editor) {
            super(editor);
        }

        public StringPreferenceEditor<PreferenceMockEditor> name() {
            return new StringPreferenceEditor<>(getEditor(), this, "name");
        }

        public StringPreferenceEditor<PreferenceMockEditor> nameWithDefault() {
            return new StringPreferenceEditor<>(getEditor(), this, "name_with_default");
        }
    }
}