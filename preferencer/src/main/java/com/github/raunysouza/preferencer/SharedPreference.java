package com.github.raunysouza.preferencer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author raunysouza
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface SharedPreference {

    boolean useDefault() default false;

    boolean useCache() default false;
}
