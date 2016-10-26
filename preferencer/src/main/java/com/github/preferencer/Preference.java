package com.github.preferencer;

/**
 * @author raunysouza
 */
public @interface Preference {
    String name() default "";
    String defaultValue() default "";
}
