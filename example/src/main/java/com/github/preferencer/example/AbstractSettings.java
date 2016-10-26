package com.github.preferencer.example;

import android.text.TextUtils;

import com.github.preferencer.Preference;
import com.github.preferencer.SharedPreference;

/**
 * @author raunysouza
 */
@SharedPreference
public abstract class AbstractSettings {

    @Preference(name = "")
    protected abstract String getInternalName();

    public String getName() {
        String name = getInternalName();
        if (TextUtils.isEmpty(name)) {
            //Do something useful here
        }
        return name;
    }

}
