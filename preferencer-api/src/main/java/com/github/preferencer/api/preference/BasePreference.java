package com.github.preferencer.api.preference;

import android.content.SharedPreferences;

import java8.util.Objects;
import java8.util.function.Consumer;
import java8.util.function.Supplier;

/**
 * @author raunysouza
 */
abstract class BasePreference<T> {

    private SharedPreferences sharedPreferences;
    private String key;
    private T defaultValue;

    BasePreference(SharedPreferences sharedPreferences, String key, T defaultValue) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public abstract T get();

    public abstract void put(T value);

    public void remove() {
        sharedPreferences.edit().remove(getKey()).apply();
    }

    public boolean exists() {
        return false;
    }

    public T orElseGet(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return exists() ? get() : supplier.get();
    }

    public T orElse(T other) {
        return exists() ? get() : other;
    }

    public void ifExists(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        if (exists()) {
            consumer.accept(get());
        }
    }

    public void putIfAbsent(T value) {
        if (!exists()) {
            put(value);
        }
    }

    protected String getKey() {
        return key;
    }

    protected T getDefaultValue() {
        return defaultValue;
    }

    protected SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
