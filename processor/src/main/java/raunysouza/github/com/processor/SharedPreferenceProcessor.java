package raunysouza.github.com.processor;

import com.github.raunysouza.preferencer.SharedPreference;
import com.google.auto.service.AutoService;

import java.util.Arrays;
import java.util.LinkedHashSet;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import raunysouza.github.com.processor.exception.ProcessorException;
import raunysouza.github.com.processor.generator.Generator;
import raunysouza.github.com.processor.generator.SharedPreferenceGenerator;
import raunysouza.github.com.processor.model.Preference;
import raunysouza.github.com.processor.model.SharedPreferenceClass;

/**
 * @author raunysouza
 */
@AutoService(Processor.class)
public class SharedPreferenceProcessor extends AbstractProcessor {

    private Messager messager;
    private Generator generator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        generator = new SharedPreferenceGenerator();
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

                SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass();
                sharedPreferenceClass.setSourceElement(typeElement);
                sharedPreferenceClass.setInterface(typeElement.getKind() == ElementKind.INTERFACE);
                SharedPreference annotation = typeElement.getAnnotation(SharedPreference.class);
                sharedPreferenceClass.setUseDefault(annotation.useDefault());
                getAllPreferences(typeElement, sharedPreferenceClass);
                // No errors, generate
                generator.generate(sharedPreferenceClass, processingEnv);
            }
        } catch (ProcessorException e) {
            error(e.getElement(), e.getMessage());
        }

        return true;
    }

    private boolean isValidClass(TypeElement element) {
        // Check whether its a public class
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error(element, "The class %s is not public", element.getQualifiedName().toString());
            return false;
        }

        // Check whether its a abstract class
        if (!element.getModifiers().contains(Modifier.ABSTRACT)) {
            error(element, "Only Abstract class supported and class %s isn't",
                    element.getQualifiedName().toString());
            return false;
        }

        return true;
    }

    private void getAllPreferences(TypeElement typeElement, SharedPreferenceClass sharedPreferenceClass) throws ProcessorException {
        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD &&
                    element.getModifiers().containsAll(Arrays.asList(Modifier.ABSTRACT, Modifier.PUBLIC))) {

                ExecutableElement executableElement = (ExecutableElement) element;
                String name = executableElement.getSimpleName().toString();
                if (name.startsWith("get") || name.startsWith("is")) {
                    Preference preference = new Preference();
                    preference.setMethodElement(executableElement);

                    name = name.replaceAll("(get|is)", "");
                    preference.setName(name);

                    com.github.raunysouza.preferencer.Preference annotation = executableElement.getAnnotation(com.github.raunysouza.preferencer.Preference.class);
                    if (annotation != null) {
                        preference.setDefaultValue(annotation.defaultValue());
                    }

                    preference.setType(executableElement.getReturnType());

                    sharedPreferenceClass.addPreference(preference);
                }
            }
        }
    }

    private void error(Element element, String message, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(message, args),
                element
        );
    }
}
