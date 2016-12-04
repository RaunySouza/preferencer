package com.github.preferencer.example;

import com.github.preferencer.annotation.DefaultStringSet;
import com.github.preferencer.annotation.SharedPreference;

import java.util.Set;

/**
 * @author raunysouza
 */
@SharedPreference
public interface Settings {

    String name();

    boolean loaded();

    Boolean loadedTwo();

    int count();

    Integer countTwo();

    long time();

    Long timeTwo();

    float amount();

    Float amountTwo();

    Set<String> people();

    @DefaultStringSet({ "dog", "cat", "cow" })
    Set<String> animals();
}
