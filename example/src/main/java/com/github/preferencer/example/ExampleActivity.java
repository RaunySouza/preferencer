package com.github.preferencer.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Set;

/**
 * @author raunysouza
 */
public class ExampleActivity extends AppCompatActivity {

    private SettingsPreference mSettingsPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsPreference = SettingsPreference.getInstance(this);

        mSettingsPreference.name()
                .putIfAbsent("My new name");

        Set<String> animals = mSettingsPreference.animals().get();
        for (String animal : animals) {
            System.out.println("animal = " + animal);
        }

        float value = mSettingsPreference.amount().orElse(2f);

        //Batch edition
        mSettingsPreference.edit()
                .count().put(1)
                .timeTwo().remove()
                .apply();

        try (SettingsPreference.SettingsPreferenceEditor edit = mSettingsPreference.edit()) {
            edit.beginTransaction();

            edit
                .loaded().put(false)
                .nickname().put("Doe");

            edit.setSuccessfully();
        }
    }
}
