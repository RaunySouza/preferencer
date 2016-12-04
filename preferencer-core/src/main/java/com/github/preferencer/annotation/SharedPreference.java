package com.github.preferencer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface SharedPreference {

    /**
     * Indicates that will be used default SharedPreferences.</br></br>
     *
     * The SharedPreferences will be got through this call:
     * <strong>PreferenceManager.getDefaultSharedPreferences(Context)</strong>
     */
    boolean useDefault() default false;
}
