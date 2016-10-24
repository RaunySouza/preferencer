package com.github.raunysouza.preferencer;

import java.util.Set;

/**
 * @author raunysouza
 */
@SharedPreference
public abstract class AbstractSettings {

    public void test() {
        String name = getName();
    }

    @Preference
    public abstract String getName();

    public abstract int getAge();

    public abstract Float getA();

    @Preference
    public abstract boolean getBoo();

    public abstract long getLon();

    public abstract Set<String> getSet();

}
