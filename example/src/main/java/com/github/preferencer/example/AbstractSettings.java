package com.github.preferencer.example;

import com.github.preferencer.PostConstruct;
import com.github.preferencer.SharedPreference;

/**
 * @author raunysouza
 */
@SharedPreference
public abstract class AbstractSettings {

    protected abstract String getName();

    @PostConstruct
    protected void init() {

    }

}
