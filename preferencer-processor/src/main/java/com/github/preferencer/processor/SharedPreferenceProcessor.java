package com.github.preferencer.processor;

import com.github.preferencer.PostConstruct;
import com.github.preferencer.SharedPreference;
import com.github.preferencer.Superclass;
import com.github.preferencer.processor.exception.ProcessingException;
import com.github.preferencer.processor.generator.Generator;
import com.github.preferencer.processor.generator.SharedPreferenceGenerator;
import com.github.preferencer.processor.model.PostConstructMethod;
import com.github.preferencer.processor.model.Preference;
import com.github.preferencer.processor.model.SharedPreferenceClass;
import com.google.auto.service.AutoService;

import org.apache.commons.lang3.StringUtils;

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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author raunysouza
 */
@AutoService(Processor.class)
public class SharedPreferenceProcessor extends AbstractProcessor {

    private Types typesUtil;
    private Messager messager;
    private Generator generator;

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

                SharedPreferenceClass sharedPreferenceClass = new SharedPreferenceClass();
                sharedPreferenceClass.setSourceElement(typeElement);
                sharedPreferenceClass.setInterface(typeElement.getKind() == ElementKind.INTERFACE);
                SharedPreference annotation = typeElement.getAnnotation(SharedPreference.class);
                sharedPreferenceClass.setUseDefault(annotation.useDefault());
                sharedPreferenceClass.setAllowTransaction(annotation.allowTransaction());
                sharedPreferenceClass.setPostConstructMethod(getPostConstruct(typeElement));
                getAllPreferences(typeElement, sharedPreferenceClass);

                if (sharedPreferenceClass.getPreferences().isEmpty()) {
                    warn(element, "Class %s is annotated with @SharedPreference but has no field", typeElement.getSimpleName());
                }

                // No errors, generate
                generator.generate(sharedPreferenceClass, processingEnv);
            }
        } catch (ProcessingException e) {
            error(e.getElement(), e.getMessage());
        } catch (Exception e) {
            error(null, e.getMessage());
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

    private void getAllPreferences(TypeElement typeElement, SharedPreferenceClass sharedPreferenceClass) throws ProcessingException {
        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD && element.getModifiers().contains(Modifier.ABSTRACT)) {

                ExecutableElement executableElement = (ExecutableElement) element;
                String methodName = executableElement.getSimpleName().toString();
                if (methodName.startsWith("get") || methodName.startsWith("is")) {
                    Preference preference = new Preference();
                    preference.setMethodElement(executableElement);

                    int offset = methodName.startsWith("get") ? 3 : 2;
                    preference.setName(methodName.substring(offset));

                    Optional<? extends Element> setter = typeElement.getEnclosedElements().stream()
                            .filter(e -> e.getSimpleName().toString().equals("set" + preference.getName()) && e.getKind() == ElementKind.METHOD)
                            .findFirst();

                    boolean shouldGenerateSetter = true;
                    if (setter.isPresent()) {
                        shouldGenerateSetter = setter.get().getModifiers().contains(Modifier.ABSTRACT);
                        preference.setSetterMethodElement((ExecutableElement) setter.get());
                    }

                    preference.setShouldGenerateSetter(shouldGenerateSetter);

                    com.github.preferencer.Preference annotation = executableElement.getAnnotation(com.github.preferencer.Preference.class);
                    if (annotation != null) {
                        preference.setDefaultValue(annotation.defaultValue());

                        if (!StringUtils.isEmpty(annotation.name())) {
                            preference.setKeyName(annotation.name());
                        }
                    }

                    preference.setType(executableElement.getReturnType());

                    sharedPreferenceClass.addPreference(preference);
                }
            }
        }

        TypeElement superclass = (TypeElement) typesUtil.asElement(typeElement.getSuperclass());
        if (superclass != null && superclass.getAnnotation(Superclass.class) != null) {
            getAllPreferences(superclass, sharedPreferenceClass);
        }

        for (TypeMirror typeMirror : typeElement.getInterfaces()) {
            TypeElement interfacee = (TypeElement) typesUtil.asElement(typeMirror);
            if (interfacee.getAnnotation(Superclass.class) != null) {
                getAllPreferences(interfacee, sharedPreferenceClass);
            }
        }
    }

    private PostConstructMethod getPostConstruct(TypeElement typeElement) throws ProcessingException {
        Optional<? extends Element> postConstructMethodOptional =  typeElement.getEnclosedElements().stream()
                .filter(element -> element.getAnnotation(PostConstruct.class) != null)
                .findFirst();

        if (postConstructMethodOptional.isPresent()) {
            ExecutableElement element = (ExecutableElement) postConstructMethodOptional.get();
            boolean injectContext = false;
            if (!element.getParameters().isEmpty()) {
                if (element.getParameters().size() > 1) {
                    throw new ProcessingException("@PostConstruct method cannot have more than one parameter", element);
                }

                VariableElement variableElement = element.getParameters().get(0);
                TypeElement parameterTypeElement = (TypeElement) typesUtil.asElement(variableElement.asType());
                if (!parameterTypeElement.getQualifiedName().toString().equals("android.content.Context")) {
                    throw new ProcessingException("Only android.content.Context could be injected in @PostConstruct method", element);
                }
                injectContext = true;
            }

            if (element.getModifiers().contains(Modifier.ABSTRACT)) {
                throw new ProcessingException("@PostConstruct method shouldn't be abstract", element);
            }

            if (element.getModifiers().contains(Modifier.PRIVATE)) {
                throw new ProcessingException("@PostConstruct method shouldn't be private", element);
            }

            if (element.getReturnType().getKind() != TypeKind.VOID) {
                warn(element, "PostConstruct doesn't return void, return ignored");
            }

            return new PostConstructMethod(element.getSimpleName().toString(), injectContext);
        }

        return null;
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
