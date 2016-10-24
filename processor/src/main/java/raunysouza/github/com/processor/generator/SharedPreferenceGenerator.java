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
import raunysouza.github.com.processor.exception.ProcessorException;
import raunysouza.github.com.processor.model.Preference;
import raunysouza.github.com.processor.model.SharedPreferenceClass;

/**
 * @author raunysouza
 */
public class SharedPreferenceGenerator implements Generator {

    private static final String FIELD_PREFERENCE_NAME = "preferences";

    private ClassName sharedPreferenceClassName;
    private ClassName contextClassName;

    public SharedPreferenceGenerator() {
        contextClassName = ClassName.get("android.content", "Context");
        sharedPreferenceClassName = ClassName.get("android.content", "SharedPreferences");
    }

    @Override
    public void generate(SharedPreferenceClass clazz, ProcessingEnvironment env) throws ProcessorException {
        ClassName sourceTypeName = (ClassName) ParameterizedTypeName.get(clazz.getSourceElement().asType());
        String prefClassName = clazz.getName();

        ClassName instanceClassName = ClassName.get(sourceTypeName.packageName(), prefClassName);
        TypeSpec.Builder builder = TypeSpec.classBuilder(prefClassName)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addFields(generatedDefaultFields(instanceClassName))
                .addFields(generateFields(clazz.getPreferences()))
                .addMethod(generateGetInstanceMethod(instanceClassName))
                .addMethods(generateFieldsMethod(clazz.getPreferences(), instanceClassName))
                .addMethod(generateConstructor(clazz))
                .addMethod(generateClearMethod())
                .addMethod(generateSaveMethod(clazz.getPreferences()));

        if (clazz.isInterface()) {
            builder.addSuperinterface(sourceTypeName);
        } else {
            builder.superclass(sourceTypeName);
        }

        CodeGeneration.addGeneratedAnnotation(env, builder);

        try {
            CodeGeneration.writeType(env, sourceTypeName.packageName(), builder.build());
        } catch (IOException e) {
            throw new ProcessorException("Error trying to write file", clazz.getSourceElement());
        }
    }

    private MethodSpec generateConstructor(SharedPreferenceClass clazz) {
        MethodSpec.Builder builder =  MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(
                        ParameterSpec.builder(contextClassName, "context")
                                .addAnnotation(
                                        AnnotationSpec.builder(ClassName.get("android.support.annotation", "NonNull"))
                                                .build())
                                .build()
                )
                .addStatement("$T.requireNonNull($L, $S)", ClassName.get(Objects.class), "context", "Context must not be null")
                .addStatement("this.$L = context.getSharedPreferences($S, $T.MODE_PRIVATE)",
                        FIELD_PREFERENCE_NAME, clazz.getPreferenceName(), contextClassName);

        for (Preference preference : clazz.getPreferences()) {
            builder.addStatement("set$L(this.$L.$L($S, $L))",
                    preference.getName(), FIELD_PREFERENCE_NAME,
                    "", preference.getKeyName(),
                    preference.getDefaultValue());
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

    private List<MethodSpec> generateFieldsMethod(List<Preference> preferences, ClassName thisClass) {
        List<MethodSpec> specs = new ArrayList<>(preferences.size());

        for (Preference preference : preferences) {
            MethodSpec getter = MethodSpec.methodBuilder(preference.getMethodElement().getSimpleName().toString())
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(ParameterizedTypeName.get(preference.getType()))
                    .addStatement("return " + preference.getFieldName())
                    .build();

            specs.add(getter);

            MethodSpec setter = MethodSpec.methodBuilder("set" + preference.getName())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(thisClass)
                    .addParameter(ParameterizedTypeName.get(preference.getType()), preference.getFieldName())
                    .addStatement("this.$L = $L", preference.getFieldName(), preference.getFieldName())
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

    private MethodSpec generateSaveMethod(List<Preference> preferences) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("SharedPreferences.Editor editor = $L.edit()", FIELD_PREFERENCE_NAME);

//        for (Preference preference : preferences) {
//            builder.addStatement("editor.$L($S, $L)",
//                    preference.getSharedPreferencesMethod().getPutMethodName(),
//                    preference.getName(), preference.getName());
//        }

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
}
