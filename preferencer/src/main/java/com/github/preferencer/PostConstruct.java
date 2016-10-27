package com.github.preferencer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author rauny.souza
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface PostConstruct {
}
