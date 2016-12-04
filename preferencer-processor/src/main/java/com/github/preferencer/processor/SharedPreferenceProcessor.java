package com.github.preferencer.processor;

import com.github.preferencer.annotation.DefaultBoolean;
import com.github.preferencer.annotation.DefaultFloat;
import com.github.preferencer.annotation.DefaultInt;
import com.github.preferencer.annotation.DefaultLong;
import com.github.preferencer.annotation.DefaultString;
import com.github.preferencer.annotation.DefaultStringSet;
import com.github.preferencer.annotation.SharedPreference;
import com.github.preferencer.processor.exception.ProcessingException;
import com.github.preferencer.processor.generator.SharedPreferenceGenerator;
import com.github.preferencer.processor.model.Preference;
import com.github.preferencer.processor.model.SharedPreferenceType;
import com.github.preferencer.processor.model.preference.FloatPreference;
import com.github.preferencer.processor.model.preference.LiteralPreference;
import com.github.preferencer.processor.model.preference.LongPreference;
import com.github.preferencer.processor.model.preference.StringPreference;
import com.github.preferencer.processor.model.preference.StringSetPreference;
import com.github.preferencer.processor.utils.ClassNameUtil;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author raunysouza
 */
@AutoService(Processor.class)
public class SharedPreferenceProcessor extends AbstractProcessor {

    private Types typesUtil;
    private Messager messager;
    private SharedPreferenceGenerator generator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        generator = new SharedPreferenceGenerator();
        typesUtil = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(SharedPreference.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (Element element : roundEnv.getElementsAnnotatedWith(SharedPreference.class)) {
                TypeElement typeElement = (TypeElement) element;
                if (!isValidClass(typeElement)) {
                    return true;
                }

                SharedPreference sharedPreferenceAnnotation = typeElement.getAnnotation(SharedPreference.class);
                SharedPreferenceType type = new SharedPreferenceType(typeElement, sharedPreferenceAnnotation.useDefault());
                getPreferences(type);

                if (type.getPreferences().isEmpty()) {
                    warn(element, "Class %s is annotated with @SharedPreference but has no field", typeElement.getSimpleName());
                }

                // No errors, generate
                generator.generate(type, processingEnv);
            }
        } catch (ProcessingException e) {
            error(e.getElement(), e.getMessage());
        } catch (Exception e) {
            error(null, e.getMessage());
        }

        return true;
    }

    private void getPreferences(SharedPreferenceType type) throws ProcessingException {
        for (Element element : type.getSourceElement().getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement executableElement = (ExecutableElement) element;
                TypeElement returnedType;
                if (executableElement.getReturnType().getKind().isPrimitive()) {
                    returnedType = typesUtil.boxedClass(
                            typesUtil.getPrimitiveType(executableElement.getReturnType().getKind()));
                } else {
                    returnedType = (TypeElement) typesUtil.asElement(executableElement.getReturnType());
                }

                String name = executableElement.getSimpleName().toString();
                Object defaultValue;
                Preference preference;
                switch (returnedType.getQualifiedName().toString()) {
                    case "java.lang.String":
                        defaultValue = getDefaultValueAnnotation(executableElement, DefaultString.class)
                                .map(DefaultString::value).orElse("");
                        preference = new StringPreference(name, defaultValue, ClassNameUtil.stringPreference);
                        break;
                    case "java.lang.Boolean":
                        defaultValue = getDefaultValueAnnotation(executableElement, DefaultBoolean.class)
                                .map(DefaultBoolean::value).orElse(false);
                        preference = new LiteralPreference(name, defaultValue, ClassNameUtil.booleanPreference);
                        break;
                    case "java.lang.Integer":
                        defaultValue = getDefaultValueAnnotation(executableElement, DefaultInt.class)
                                .map(DefaultInt::value).orElse(0);
                        preference = new LiteralPreference(name, defaultValue, ClassNameUtil.intPreference);
                        break;
                    case "java.lang.Long":
                        defaultValue = getDefaultValueAnnotation(executableElement, DefaultLong.class)
                                .map(DefaultLong::value).orElse(0L);
                        preference = new LongPreference(name, defaultValue, ClassNameUtil.longPreference);
                        break;
                    case "java.lang.Float":
                        defaultValue = getDefaultValueAnnotation(executableElement, DefaultFloat.class)
                                .map(DefaultFloat::value).orElse(0.0F);
                        preference = new FloatPreference(name, defaultValue, ClassNameUtil.floatPreference);
                        break;
                    case "java.util.Set":
                        defaultValue = getDefaultValueAnnotation(executableElement, DefaultStringSet.class)
                                .map(DefaultStringSet::value).orElse(new String[0]);
                        preference = new StringSetPreference(name, defaultValue, ClassNameUtil.stringSetPreference);
                        break;
                    default:
                        throw new ProcessingException(String.format("Element %s has a invalid type", executableElement), executableElement);
                }

                type.addPreference(preference);
            }
        }
    }

    private <T extends Annotation> Optional<T> getDefaultValueAnnotation(ExecutableElement executableElement, Class<T> clazz) {
        return Optional.ofNullable(executableElement.getAnnotation(clazz));
    }

    private boolean isValidClass(TypeElement element) {
        if (element.getKind() != ElementKind.INTERFACE) {
            error(element, "Only interfaces allowed to generate SharedPreferences");
            return false;
        }
        return true;
    }

    private void error(Element element, String message, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(message, args),
                element
        );
    }

    private void warn(Element element, String message, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.WARNING,
                String.format(message, args),
                element
        );
    }
}
