package com.github.preferencer.processor.generator;

import com.github.preferencer.processor.CodeGeneration;
import com.github.preferencer.processor.exception.ProcessingException;
import com.github.preferencer.processor.method.SharedPreferenceMethodResolver;
import com.github.preferencer.processor.method.SharedPreferencesMethod;
import com.github.preferencer.processor.model.Preference;
import com.github.preferencer.processor.model.SharedPreferenceClass;
import com.github.preferencer.processor.utils.NamingUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;


/**
 * @author raunysouza
 */
public class SharedPreferenceGenerator implements Generator {

    private static final String FIELD_PREFERENCE_NAME = "preferences";
    private static final String FIELD_CURRENT_TRANSACTION = "currentTransaction";
    private static final String VAR_CONTEXT = "context";

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
                .addMethod(generateGetInstanceMethod(instanceClassName))
                .addMethods(generateFieldsMethod(preferenceHolders, instanceClassName))
                .addMethod(generateConstructor(clazz))
                .addMethod(generateClearMethod())
                .addTypes(generateInnerClasses());

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

    private MethodSpec generateConstructor(SharedPreferenceClass clazz) {
        MethodSpec.Builder builder =  MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(contextClassName, VAR_CONTEXT)
                        .addAnnotation(AnnotationSpec.builder(ClassName.get("android.support.annotation", "NonNull")).build())
                        .build()
                )
                .addStatement("$T.requireNonNull($L, $S)", ClassName.get(Objects.class), VAR_CONTEXT, "Context must not be null");

        if (clazz.isUseDefault()) {
            ClassName preferenceManagerClassName = ClassName.get("android.preference", "PreferenceManager");
            builder.addStatement("this.$L = $T.getDefaultSharedPreferences($L)",
                    FIELD_PREFERENCE_NAME, preferenceManagerClassName, VAR_CONTEXT);
        } else {
            builder.addStatement("this.$L = $L.getSharedPreferences($S, $T.MODE_PRIVATE)",
                    FIELD_PREFERENCE_NAME, VAR_CONTEXT, clazz.getPreferenceName(), contextClassName);
        }

        return builder.build();
    }

    private List<MethodSpec> generateFieldsMethod(List<PreferenceHolder> preferenceHolders, ClassName thisClass) {
        List<MethodSpec> specs = new ArrayList<>(preferenceHolders.size());

        for (PreferenceHolder preferenceHolder : preferenceHolders) {
            String defaultValue = StringUtils.isEmpty(preferenceHolder.preference.getDefaultValue())
                    ? preferenceHolder.method.defaultValue() : preferenceHolder.preference.getDefaultValue();
            Set<Modifier> modifiers = new HashSet<>(preferenceHolder.preference.getMethodElement().getModifiers());
            modifiers.remove(Modifier.ABSTRACT);

            MethodSpec getter = MethodSpec.methodBuilder(preferenceHolder.preference.getMethodElement().getSimpleName().toString())
                    .addModifiers(modifiers)
                    .addAnnotation(Override.class)
                    .returns(ParameterizedTypeName.get(preferenceHolder.preference.getType()))
                    .addStatement("return this.$L.$L($S, $L)", FIELD_PREFERENCE_NAME, preferenceHolder.method.get(),
                            NamingUtils.getVariableName(preferenceHolder.preference.getName()), defaultValue)
                    .build();

            specs.add(getter);

            MethodSpec setter = MethodSpec.methodBuilder("set" + NamingUtils.getMethodName(preferenceHolder.preference.getName()))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(thisClass)
                    .addParameter(ParameterizedTypeName.get(preferenceHolder.preference.getType()),
                            NamingUtils.getVariableName(preferenceHolder.preference.getName()))
                    .addStatement("$T.Editor editor = $L != null ? $L.editor : $L.edit()", sharedPreferenceClassName,
                            FIELD_CURRENT_TRANSACTION, FIELD_CURRENT_TRANSACTION, FIELD_PREFERENCE_NAME)
                    .addStatement("editor.$L($S, $L)", preferenceHolder.method.put(),
                            NamingUtils.getKeyName(preferenceHolder.preference.getName()),
                            NamingUtils.getVariableName(preferenceHolder.preference.getName()))
                    .beginControlFlow("if ($L == null)", FIELD_CURRENT_TRANSACTION)
                        .addStatement("editor.apply()")
                    .endControlFlow()
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

        //currentTransaction field
        FieldSpec currentTransaction = FieldSpec.builder(ClassName.get("", "Transaction"), FIELD_CURRENT_TRANSACTION, Modifier.PRIVATE)
                .build();
        specs.add(currentTransaction);

        return specs;
    }

    private MethodSpec generateClearMethod() {
        return MethodSpec.methodBuilder("clear")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("this.$L.edit().clear().apply()", FIELD_PREFERENCE_NAME)
                .build();
    }

    private MethodSpec generateGetInstanceMethod(ClassName thisInstance) {
        return MethodSpec.methodBuilder("getInstance")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(thisInstance)
                .addParameter(contextClassName, VAR_CONTEXT)
                .beginControlFlow("if (instance == null)")
                    .beginControlFlow("synchronized ($T.class)", thisInstance)
                        .addStatement("instance = new $T($L)", thisInstance, VAR_CONTEXT)
                    .endControlFlow()
                .endControlFlow()
                .addStatement("return instance")
                .build();
    }

    private List<TypeSpec> generateInnerClasses() {
        List<TypeSpec> specs = new ArrayList<>();

        TypeSpec transaction = TypeSpec.classBuilder("Transaction")
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(ClassName.get("android.content.SharedPreferences", "Editor"), "editor", Modifier.PRIVATE).build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addStatement("editor = $L.edit()", FIELD_PREFERENCE_NAME)
                        .build())
                .addMethod(MethodSpec.methodBuilder("commit")
                        .returns(void.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("editor.apply()")
                        .addStatement("$L = null", FIELD_CURRENT_TRANSACTION)
                        .build())
                .addMethod(MethodSpec.methodBuilder("rollback")
                        .returns(void.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$L = null", FIELD_CURRENT_TRANSACTION)
                        .build())
                .build();

        specs.add(transaction);

        return specs;
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
