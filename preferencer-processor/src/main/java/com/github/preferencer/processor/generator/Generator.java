package com.github.preferencer.processor.generator;

import com.github.preferencer.processor.exception.ProcessingException;
import com.github.preferencer.processor.model.SharedPreferenceClass;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author raunysouza
 */
public interface Generator {

    void generate(SharedPreferenceClass clazz, ProcessingEnvironment env) throws ProcessingException;
}
