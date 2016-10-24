package raunysouza.github.com.processor.method;

import com.squareup.javapoet.MethodSpec;

/**
 * @author raunysouza
 */
public class BooleanMethod implements SharedPreferencesMethod {

    @Override
    public void createGetStatement(MethodSpec.Builder builder, String key, String value) {
        builder.addStatement("getBoolean($S, $L)", key, value);
    }

    @Override
    public void createPutStatement(MethodSpec.Builder builder, String key, String value) {
        return "putBoolean";
    }

}
