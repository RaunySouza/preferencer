package com.github.preferencer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author raunysouza
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Preference {
    /**
     * Override the SharedPreference's key
     */
    String name() default "";

    /**
     * Override SharedPreference's default value.
     * This value is a literal.
     *
     * Example:
     *
     * @Preference(defaultValue = "0L")
     * public abstract long foo();
     *
     * It will generate:
     * @Override
     * public long foo() {
     *     return preference.getLong("foo", 0L);
     * }
     *
     * Obs.: Strings should be escape.
     */
    String defaultValue() default "";
}
