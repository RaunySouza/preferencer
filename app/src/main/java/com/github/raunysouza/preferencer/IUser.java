package com.github.raunysouza.preferencer;

/**
 * @author raunysouza
 */
@SharedPreference(
        useDefault = true
)
public interface IUser {

    int getAge();
}
