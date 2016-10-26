package com.github.preferencer.example;

import com.github.preferencer.Preference;
import com.github.preferencer.SharedPreference;

/**
 * @author raunysouza
 */
@SharedPreference
public abstract class AbstractSettings {

    @Preference
    protected abstract String getName();


}
