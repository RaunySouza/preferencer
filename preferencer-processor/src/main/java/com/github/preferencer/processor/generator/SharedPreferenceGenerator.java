package com.github.preferencer.processor.generator;

import com.github.preferencer.processor.CodeGeneration;
import com.github.preferencer.processor.exception.ProcessingException;
import com.github.preferencer.processor.model.Preference;
import com.github.preferencer.processor.model.SharedPreferenceType;
import com.github.preferencer.processor.utils.ClassNameUtil;
import com.github.preferencer.processor.utils.NamingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;


/**
 * @author raunysouza
 */
public class SharedPreferenceGenerator {

    private static final String FIELD_PREFERENCE_NAME = "preferences";
    private static final String FIELD_CURRENT_TRANSACTION = "currentTransaction";
    private static final String VAR_CONTEXT = "context";

    public void generate(SharedPreferenceType type, ProcessingEnvironment env) throws ProcessingException {
        ClassName sourceTypeName = (ClassName) ParameterizedTypeName.get(type.getSourceElement().asType());

        TypeSpec.Builder builder = TypeSpec.classBuilder(generateTargetClassName(type.getName()))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(generateConstructor(type))
                .addMethods(generatePreferenceMethods(type.getPreferences()))
                .superclass(ClassNameUtil.baseSharedPreference);

        CodeGeneration.addGeneratedAnnotation(env, builder);

        try {
            CodeGeneration.writeType(env, sourceTypeName.packageName(), builder.build());
        } catch (IOException e) {
            throw new ProcessingException("Error trying to write file", type.getSourceElement());
        }
    }

    private MethodSpec generateConstructor(SharedPreferenceType type) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(ClassNameUtil.context, VAR_CONTEXT, Modifier.FINAL).build());

        if (type.isUseDefault()) {
            builder.addStatement("super($T.getDefaultSharedPreferences($L))",
                    ClassNameUtil.preferenceManager, VAR_CONTEXT);
        } else {
            builder.addStatement("super($L.getSharedPreferences($S, 0))",
                    VAR_CONTEXT, NamingUtils.getKeyName(type.getName()));
        }

        return builder.build();
    }

    private List<MethodSpec> generatePreferenceMethods(List<Preference> preferences) {
        List<MethodSpec> specs = new ArrayList<>(preferences.size());
        preferences.forEach(preference -> {
            MethodSpec.Builder builder = MethodSpec.methodBuilder(preference.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(preference.getType());
            preference.createStatement(builder);
            specs.add(builder.build());
        });
        return specs;
    }

    private String generateTargetClassName(String name) {
        if (name.startsWith("I")) {
            return name.substring(1);
        }

        return name + "Preference";
    }
}
