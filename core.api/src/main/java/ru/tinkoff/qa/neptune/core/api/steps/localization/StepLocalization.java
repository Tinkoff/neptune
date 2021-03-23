package ru.tinkoff.qa.neptune.core.api.steps.localization;

import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.PseudoField;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Locale;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;
import static ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter.ParameterValueReader.getParameterForStep;

/**
 * Interface to translate step parameter and description.
 */
public interface StepLocalization {

    static String translate(String textToTranslate) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();

        if (engine == null) {
            return textToTranslate;
        }
        return engine.translation(textToTranslate, locale);
    }

    static String translate(AnnotatedElement annotatedElement, Object... args) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();

        if (engine == null) {
            if (annotatedElement instanceof Method) {
                if (annotatedElement.getAnnotation(Description.class) != null) {
                    String description = annotatedElement.getAnnotation(Description.class).value();
                    return translationByTemplate((Method) annotatedElement, description, args);
                } else {
                    Class<?> clazz = ((Method) annotatedElement).getDeclaringClass();
                    while (clazz.getAnnotation(Description.class) == null && !clazz.equals(Object.class)) {
                        clazz = clazz.getSuperclass();
                    }
                    return ofNullable(clazz.getAnnotation(Description.class)).map(Description::value).orElse(null);
                }
            } else {
                if (annotatedElement.getAnnotation(StepParameter.class) != null) {
                    return annotatedElement.getAnnotation(StepParameter.class).value();
                } else {
                    return null;
                }
            }
        }

        if (annotatedElement instanceof Method) {
            if (annotatedElement.getAnnotation(Description.class) != null) {
                return engine.methodTranslation((Method) annotatedElement, locale, args);
            } else {
                Class<?> clazz = ((Method) annotatedElement).getDeclaringClass();
                while (clazz.getAnnotation(Description.class) == null && !clazz.equals(Object.class)) {
                    clazz = clazz.getSuperclass();
                }
                return engine.classTranslation(clazz, locale);
            }
        } else {
            if (annotatedElement.getAnnotation(StepParameter.class) != null) {
                if (annotatedElement instanceof Field) {
                    return engine.memberTranslation((Field) annotatedElement, locale);
                }
                return engine.memberTranslation((PseudoField) annotatedElement, locale);
            } else {
                return null;
            }
        }
    }

    static String translationByTemplate(Method method, String template, Object... args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            Annotation[] annotations = parameterAnnotations[argIndex];
            for (Annotation annotation : annotations) {
                if (!(annotation instanceof DescriptionFragment)) {
                    continue;
                }
                DescriptionFragment stepDescriptionFragment = (DescriptionFragment) annotation;
                template = template.replace("{" + stepDescriptionFragment.value() + "}",
                        getParameterForStep(args[argIndex], stepDescriptionFragment.makeReadableBy()));
            }
        }

        return template;
    }

    String classTranslation(Class<?> clz, Locale locale);

    String methodTranslation(Method method, Locale locale, Object... parameters);

    <T extends AnnotatedElement & Member> String memberTranslation(T member, Locale locale);

    String translation(String text, Locale locale);
}
