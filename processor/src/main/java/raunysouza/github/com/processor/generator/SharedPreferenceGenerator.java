package raunysouza.github.com.processor.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

import raunysouza.github.com.processor.CodeGeneration;
import raunysouza.github.com.processor.exception.ProcessingException;
import raunysouza.github.com.processor.method.SharedPreferenceMethodResolver;
import raunysouza.github.com.processor.method.SharedPreferencesMethod;
import raunysouza.github.com.processor.model.Preference;
import raunysouza.github.com.processor.model.SharedPreferenceClass;

/**
 * @author raunysouza
 */
public class SharedPreferenceGenerator implements Generator {

    private static final String FIELD_PREFERENCE_NAME = "preferences";

    private SharedPreferenceMethodResolver typeMethodResolver;
    private ClassName sharedPreferenceClassName;
    private ClassName contextClassName;

    public SharedPreferenceGenerator() {
        typeMethodResolver = new SharedPreferenceMethodResolver();
        contextClassName = ClassName.get("android.content", "Context");
        sharedPreferenceClassName = ClassName.get("android.content", "SharedPreferences");
    }

    @Override
    public void generate(SharedPreferenceClass clazz, ProcessingEnvironment env) throws ProcessingException {
        ClassName sourceTypeName = (ClassName) ParameterizedTypeName.get(clazz.getSourceElement().asType());
        String prefClassName = clazz.getName();

        List<PreferenceHolder> preferenceHolders = new ArrayList<>(clazz.getPreferences().size());
        for (Preference preference : clazz.getPreferences()) {
            SharedPreferencesMethod method = typeMethodResolver.getMethod(preference.getType().toString());
            if (method == null) {
                throw new ProcessingException(String.format("Type %s not supported by SharedPreference", preference.getType().toString()),
                        clazz.getSourceElement());
            }
            preferenceHolders.add(new PreferenceHolder(preference, method));
        }

        ClassName instanceClassName = ClassName.get(sourceTypeName.packageName(), prefClassName);
        TypeSpec.Builder builder = TypeSpec.classBuilder(prefClassName)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addFields(generatedDefaultFields(instanceClassName))
                .addFields(generateFields(clazz.getPreferences()))
                .addMethod(generateGetInstanceMethod(instanceClassName))
                .addMethods(generateFieldsMethod(preferenceHolders, instanceClassName))
                .addMethod(generateConstructor(clazz, preferenceHolders))
                .addMethod(generateClearMethod())
                .addMethod(generateSaveMethod(preferenceHolders));

        if (clazz.isInterface()) {
            builder.addSuperinterface(sourceTypeName);
        } else {
            builder.superclass(sourceTypeName);
        }

        CodeGeneration.addGeneratedAnnotation(env, builder);

        try {
            CodeGeneration.writeType(env, sourceTypeName.packageName(), builder.build());
        } catch (IOException e) {
            throw new ProcessingException("Error trying to write file", clazz.getSourceElement());
        }
    }

    private MethodSpec generateConstructor(SharedPreferenceClass clazz, List<PreferenceHolder> preferenceHolders) {
        MethodSpec.Builder builder =  MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(contextClassName, "context")
                        .addAnnotation(AnnotationSpec.builder(ClassName.get("android.support.annotation", "NonNull")).build())
                        .build()
                )
                .addStatement("$T.requireNonNull($L, $S)", ClassName.get(Objects.class), "context", "Context must not be null");

        if (clazz.isUseDefault()) {
            ClassName preferenceManagerClassName = ClassName.get("android.preference", "PreferenceManager");
            builder.addStatement("this.$L = $T.getDefaultSharedPreferences($L)",
                    FIELD_PREFERENCE_NAME, preferenceManagerClassName, "context");
        } else {
            builder.addStatement("this.$L = context.getSharedPreferences($S, $T.MODE_PRIVATE)",
                    FIELD_PREFERENCE_NAME, clazz.getPreferenceName(), contextClassName);
        }

        for (PreferenceHolder preferenceHolder : preferenceHolders) {
            builder.addStatement("set$L(this.$L.$L($S, $L))",
                    preferenceHolder.preference.getName(), FIELD_PREFERENCE_NAME,
                    preferenceHolder.method.get(), preferenceHolder.preference.getKeyName(),
                    preferenceHolder.method.defaultValue());
        }

        return builder.build();
    }

    private List<FieldSpec> generateFields(List<Preference> preferences) {
        List<FieldSpec> specs = new ArrayList<>(preferences.size());

        for (Preference preference : preferences) {
            FieldSpec field = FieldSpec.builder(
                        ParameterizedTypeName.get(preference.getType()), preference.getFieldName(),
                        Modifier.PRIVATE)
                    .build();

            specs.add(field);
        }

        return specs;
    }

    private List<MethodSpec> generateFieldsMethod(List<PreferenceHolder> preferenceHolders, ClassName thisClass) {
        List<MethodSpec> specs = new ArrayList<>(preferenceHolders.size());

        for (PreferenceHolder preferenceHolder : preferenceHolders) {
            MethodSpec getter = MethodSpec.methodBuilder(preferenceHolder.preference.getMethodElement().getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(ParameterizedTypeName.get(preferenceHolder.preference.getType()))
                    .addStatement("return " + preferenceHolder.preference.getFieldName())
                    .build();

            specs.add(getter);

            MethodSpec setter = MethodSpec.methodBuilder("set" + preferenceHolder.preference.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(thisClass)
                    .addParameter(ParameterizedTypeName.get(preferenceHolder.preference.getType()), preferenceHolder.preference.getFieldName())
                    .addStatement("this.$L = $L", preferenceHolder.preference.getFieldName(), preferenceHolder.preference.getFieldName())
                    .addStatement("return this")
                    .build();

            specs.add(setter);
        }

        return specs;
    }

    private List<FieldSpec> generatedDefaultFields(ClassName thisInstance) {
        List<FieldSpec> specs = new ArrayList<>();

        //instance field
        FieldSpec instance = FieldSpec.builder(thisInstance, "instance")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .build();

        specs.add(instance);

        //preference field
        FieldSpec preference = FieldSpec.builder(sharedPreferenceClassName, FIELD_PREFERENCE_NAME, Modifier.PRIVATE)
                .build();
        specs.add(preference);

        return specs;
    }

    private MethodSpec generateClearMethod() {
        return MethodSpec.methodBuilder("clear")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("this.$L.edit().clear().apply()", FIELD_PREFERENCE_NAME)
                .build();
    }

    private MethodSpec generateSaveMethod(List<PreferenceHolder> preferenceHolders) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("SharedPreferences.Editor editor = $L.edit()", FIELD_PREFERENCE_NAME);

        for (PreferenceHolder preferenceHolder : preferenceHolders) {
            builder.addStatement("editor.$L($S, $L)",
                    preferenceHolder.method.put(),
                    preferenceHolder.preference.getKeyName(), preferenceHolder.preference.getFieldName());
        }

        builder.addStatement("editor.apply()");

        return builder.build();
    }

    private MethodSpec generateGetInstanceMethod(ClassName thisInstance) {
        return MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(thisInstance)
                .addParameter(contextClassName, "context")
                .beginControlFlow("if (instance == null)")
                .beginControlFlow("synchronized ($T.class)", thisInstance)
                .addStatement("instance = new $T(context)", thisInstance)
                .endControlFlow()
                .endControlFlow()
                .addStatement("return instance")
                .build();
    }

    private class PreferenceHolder {
        Preference preference;
        SharedPreferencesMethod method;

        public PreferenceHolder(Preference preference, SharedPreferencesMethod method) {
            this.preference = preference;
            this.method = method;
        }
    }
}
