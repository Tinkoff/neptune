package ru.tinkoff.qa.neptune.core.api.localization;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.AdditionalMetadata;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
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
     * Makes a translation
     *
     * @param object is an object which may be used for auxiliary purposes
     * @param method is a method which may be used for auxiliary purposes
     * @param args   objects which may be used for auxiliary purposes
     * @param <T>    is a type of a given objec
     * @return translated text
     */
    static <T> String translate(T object, Method method, Object... args) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();

        var description = method.getAnnotation(Description.class);
        if (description != null) {
            var templateParameters = templateParameters(object, method, args);
            var fullDescription = buildTextByTemplate(description.value(), templateParameters);
            if (engine == null || locale == null) {
                return fullDescription;
            }

            return engine.methodTranslation(method, fullDescription, templateParameters, locale);
        } else {
            return translateByClass(object, engine, locale);
        }
    }

    static <T> String translate(T object) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();
        return translateByClass(object, engine, locale);
    }

    static String translate(Field f) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();
        return translateMember(f, engine, locale);
    }

    static String translate(AdditionalMetadata<?> f) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();
        return translateMember(f, engine, locale);
    }

    private static <T> String translateByClass(T toBeTranslated,
                                               StepLocalization localization,
                                               Locale locale) {
        var clazz = toBeTranslated.getClass();
        var cls = clazz;
        while (clazz.getAnnotation(Description.class) == null && !clazz.equals(Object.class)) {
            clazz = clazz.getSuperclass();
        }

        var cls2 = clazz;
        return ofNullable(cls2.getAnnotation(Description.class))
                .map(description -> {
                    var templateParameters = templateParameters(toBeTranslated, cls);
                    var fullDescription = buildTextByTemplate(description.value(), templateParameters);
                    if (localization == null || locale == null) {
                        return fullDescription;
                    }

                    return localization.classTranslation(cls2, fullDescription, templateParameters, locale);
                })
                .orElse(null);
    }

    private static <T extends AnnotatedElement & Member> String translateMember(T translateFrom,
                                                                                StepLocalization localization,
                                                                                Locale locale) {
        var memberAnnotation = stream(translateFrom.getAnnotations())
                .filter(annotation -> annotation.annotationType().getAnnotation(Metadata.class) != null)
                .findFirst()
                .orElse(null);

        if (memberAnnotation == null) {
            return null;
        }

        try {
            var m = memberAnnotation.annotationType().getDeclaredMethod("value");
            m.setAccessible(true);
            var value = (String) m.invoke(memberAnnotation);

            if (localization == null || locale == null) {
                return value;
            }

            return localization.memberTranslation(translateFrom, value, locale);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Map<String, String> templateParameters(T object, AnnotatedElement annotatedElement, Object... args) {
        var result = new HashMap<String, String>();

        if (annotatedElement instanceof Method) {
            var method = (Method) annotatedElement;
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int argIndex = 0; argIndex < args.length; argIndex++) {
                Annotation[] annotations = parameterAnnotations[argIndex];

                for (Annotation annotation : annotations) {
                    if (!(annotation instanceof DescriptionFragment)) {
                        continue;
                    }
                    var stepDescriptionFragment = (DescriptionFragment) annotation;
                    var paramValue = ofNullable(args[argIndex])
                            .map(o -> getParameterForStep(o, stepDescriptionFragment.makeReadableBy()))
                            .orElseGet(() -> valueOf((Object) null));

                    result.put(stepDescriptionFragment.value(), paramValue);
                }
            }
            return result;
        }

        var clz = (Class<?>) annotatedElement;
        while (!clz.equals(Object.class)) {
            stream(clz.getDeclaredFields())
                    .filter(field -> !isStatic(field.getModifiers()) && field.getAnnotation(DescriptionFragment.class) != null)
                    .forEach(field -> {
                        field.setAccessible(true);
                        var stepDescriptionFragment = field.getAnnotation(DescriptionFragment.class);
                        try {
                            var paramValue = ofNullable(field.get(object))
                                    .map(o -> getParameterForStep(o, stepDescriptionFragment.makeReadableBy()))
                                    .orElseGet(() -> valueOf((Object) null));

                            result.put(stepDescriptionFragment.value(), paramValue);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });

            clz = clz.getSuperclass();
        }
        return result;
    }

    static String buildTextByTemplate(String template, Map<String, String> templateParameters) {
        var strContainer = new Object() {
            String contained;

            public String getContained() {
                return contained;
            }

            public void setContained(String s) {
                this.contained = s;
            }
        };

        strContainer.setContained(template);
        templateParameters.forEach((key, value) -> {
            var s = strContainer.getContained();
            s = s.replace("{" + key + "}", value);
            strContainer.setContained(s);
        });

        return strContainer.getContained();
    }

    /**
     * Makes translation by usage of a class.
     *
     * @param clz                       is a class whose metadata may be used for auxiliary purposes
     * @param description               is a description of a step. It is fully completed by value taken
     *                                  from {@link Description} of a class and by values which are taken from
     *                                  fields annotated by {@link DescriptionFragment}
     * @param descriptionTemplateParams is a map of parameters and their values. These parameters are taken from
     *                                  fields annotated by {@link DescriptionFragment}. This parameter is included
     *                                  because the map is possible to be re-used for some reasons.
     * @param locale                    is a used locale
     * @param <T>                       is a type of a class
     * @return translated text
     */
    <T> String classTranslation(Class<T> clz,
                                String description,
                                Map<String, String> descriptionTemplateParams,
                                Locale locale);

    /**
     * Makes translation by usage of a method.
     *
     * @param method                    is a method whose metadata may be used for auxiliary purposes
     * @param description               is a description of a step. It is fully completed by value taken
     *                                  from {@link Description} of a method and by values which are taken from
     *                                  method parameters annotated by {@link DescriptionFragment}
     * @param descriptionTemplateParams is a map of parameters and their values. These parameters are taken from
     *                                  method parameters annotated by {@link DescriptionFragment}.This parameter is included
     *                                  because the map is possible to be re-used for some reasons.
     * @param locale                    is a used locale
     * @return translated text
     */
    String methodTranslation(Method method,
                             String description,
                             Map<String, String> descriptionTemplateParams,
                             Locale locale);

    /**
     * Makes translation by usage of other annotated element that differs from {@link Class} and {@link Method}
     *
     * @param member      is an annotated element whose metadata may be used for auxiliary purposes
     * @param description is a description taken from the member
     * @param locale      is a used locale
     * @param <T>         is a type of a member
     * @return translated text
     */
    <T extends AnnotatedElement & Member> String memberTranslation(T member,
                                                                   String description,
                                                                   Locale locale);

    /**
     * Makes translation of a text.
     *
     * @param text   to be translated
     * @param locale is a used locale
     * @return translated text
     */
    String translation(String text, Locale locale);
}
