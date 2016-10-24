package raunysouza.github.com.processor.generator;

import javax.annotation.processing.ProcessingEnvironment;

import raunysouza.github.com.processor.exception.ProcessingException;
import raunysouza.github.com.processor.model.SharedPreferenceClass;

/**
 * @author raunysouza
 */
public interface Generator {

    void generate(SharedPreferenceClass clazz, ProcessingEnvironment env) throws ProcessingException;
}
