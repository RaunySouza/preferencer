package com.github.preferencer.example;

import com.github.preferencer.Preference;
import com.github.preferencer.Superclass;

/**
 * @author raunysouza
 */
@Superclass
public abstract class AbstractSettings implements IUser {

    protected abstract String getName();

    @Override
    @Preference(defaultValue = "25")
    public abstract int getAge();
}
