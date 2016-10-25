package com.github.raunysouza.preferencer;

/**
 * @author raunysouza
 */
public @interface Preference {
    String name() default "";
    String defaultValue() default "";
}
