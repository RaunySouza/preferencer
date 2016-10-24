package raunysouza.github.com.processor.method;

import com.squareup.javapoet.MethodSpec;

/**
 * @author raunysouza
 */
public interface SharedPreferencesMethod {

    void createGetStatement(MethodSpec.Builder builder, String key, String value);

    void createPutStatement(MethodSpec.Builder builder, String key, String value);

}
