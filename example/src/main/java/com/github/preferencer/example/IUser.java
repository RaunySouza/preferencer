package com.github.preferencer.example;

import com.github.preferencer.SharedPreference;

/**
 * @author raunysouza
 */
@SharedPreference(
        useDefault = true
)
public interface IUser {

    int getAge();
}
