package com.github.raunysouza.preferencer;

/**
 * @author raunysouza
 */
public @interface Preference {
    String defaultValue() default "";
}
