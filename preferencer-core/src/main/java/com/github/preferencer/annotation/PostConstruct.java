package com.github.preferencer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates to processor to call annotated method after get sharedPreference from context.
 * Additionally, the method can declare the android.content.Context as parameter.
 *
 * @author rauny.souza
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface PostConstruct {
}
