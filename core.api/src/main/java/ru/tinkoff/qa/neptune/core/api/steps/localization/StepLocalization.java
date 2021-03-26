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

    /**
     * Translation of a text.
     *
     * @param textToTranslate string to be translated.
     * @return translated text
     */
    static String translate(String textToTranslate) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();

        if (engine == null) {
            return textToTranslate;
        }
        return engine.translation(textToTranslate, locale);
    }

    /**
     * Crates translated text by annotations which may annotate passed {@link AnnotatedElement}.
     * <p></p>
     * Following annotations are used:
     * <p></p>
     * {@link Description}, {@link StepParameter}, {@link DescriptionFragment}
     *
     * @param annotatedElement used to get some translated text
     * @param args             additional arguments which are used to build a text. These arguments replace values in {@code `{}`}
     * @return translated text
     */
    static String translate(AnnotatedElement annotatedElement, Object... args) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();

        if (engine == null) {
            if (annotatedElement instanceof Method) {
                if (annotatedElement.getAnnotation(Description.class) != null) {
                    String description = annotatedElement.getAnnotation(Description.class).value();
                    return buildTextByTemplate((Method) annotatedElement, description, args);
                } else {
                    return translateClass(((Method) annotatedElement).getDeclaringClass(), null, null);
                }
            } else if (annotatedElement instanceof Class) {
                return translateClass((Class<?>) annotatedElement, null, null);
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
                return translateClass(((Method) annotatedElement).getDeclaringClass(), engine, locale);
            }
        } else if (annotatedElement instanceof Class) {
            return translateClass((Class<?>) annotatedElement, engine, locale);
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

    private static String translateClass(Class<?> cls, StepLocalization localization, Locale locale) {
        var clazz = cls;
        while (clazz.getAnnotation(Description.class) == null && !clazz.equals(Object.class)) {
            clazz = clazz.getSuperclass();
        }

        var cls2 = clazz;
        return ofNullable(cls2.getAnnotation(Description.class))
                .map(description -> {
                    if (localization == null || locale == null) {
                        return description.value();
                    }

                    return localization.classTranslation(cls2, locale);
                })
                .orElse(null);
    }

    /**
     * Builds some text from templated string where variables are placed in {@code `{}`}. Variables are supposed
     * to be defined by {@link DescriptionFragment} that marks parameters of signature of the passed method.
     * <p></p>
     * This method is allowed to be reused by any implementation of {@link StepLocalization}
     *
     * @param method   method whose parameters are supposed be annotated by {@link DescriptionFragment}
     * @param template is a templated string
     * @param args     values of parameters of current invocation of the method
     * @return built text
     */
    static String buildTextByTemplate(Method method, String template, Object... args) {
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
