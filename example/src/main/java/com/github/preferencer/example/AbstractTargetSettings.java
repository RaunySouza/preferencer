package com.github.preferencer.example;

import com.github.preferencer.PostConstruct;
import com.github.preferencer.SharedPreference;

/**
 * @author rauny.souza
 */
@SharedPreference
public abstract class AbstractTargetSettings extends AbstractSettings {

    public abstract String getLastName();

    @PostConstruct
    public void init() {}
}
