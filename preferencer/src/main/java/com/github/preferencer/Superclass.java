package com.github.preferencer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that class is a superclass of SharedPreference class. It means that all methods
 * declared in this class will be implemented in generated class.
 * It allows hierarchical SharedPreferences.
 *
 * @author rauny.souza
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Superclass {
}
