package com.github.raunysouza.preferencer;

import android.text.TextUtils;

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
