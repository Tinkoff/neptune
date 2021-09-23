package ru.tinkoff.qa.neptune.core.api.localization;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.AdditionalMetadata;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Metadata;

import java.lang.reflect.*;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.localization.TemplateParameter.buildTextByTemplate;
import static ru.tinkoff.qa.neptune.core.api.localization.TemplateParameter.getTemplateParameters;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

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
     * @param <T>    is a type of given objec
     * @return translated text
     */
    static <T> String translate(T object, Method method, Object... args) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();

        var description = method.getAnnotation(Description.class);
        if (description != null) {
            return translate(description.value(), method, args);
        } else {
            return translateByClass(object, engine, locale);
        }
    }

    /**
     * Makes a translation
     *
     * @param originalTemplate is original string template which may be used for auxiliary purposes
     * @param method           is a method which may be used for auxiliary purposes
     * @param args             objects which may be used for auxiliary purposes
     * @return translated text
     */
    static String translate(String originalTemplate, Method method, Object... args) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();
        var locale = DEFAULT_LOCALE_PROPERTY.get();

        var templateParameters = getTemplateParameters(originalTemplate, method, args);
        var fullDescription = buildTextByTemplate(originalTemplate, templateParameters);
        if (engine == null || locale == null) {
            return fullDescription;
        }

        return engine.methodTranslation(method, fullDescription, templateParameters, locale);
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
        while (clazz.getAnnotation(Description.class) == null && !clazz.equals(Object.class)) {
            clazz = clazz.getSuperclass();
        }

        var cls2 = clazz;
        return ofNullable(cls2.getAnnotation(Description.class))
                .map(description -> {
                    var templateParameters = getTemplateParameters(description.value(), toBeTranslated);
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

    /**
     * Makes translation by usage of a class.
     *
     * @param clz                       is a class whose metadata may be used for auxiliary purposes
     * @param description               is a description of a step. It is commonly completed by value taken
     *                                  from {@link Description} of a class and by values which are taken from
     *                                  fields annotated by {@link DescriptionFragment}
     * @param descriptionTemplateParams is a list of parameters and their values. These parameters are taken from
     *                                  fields annotated by {@link DescriptionFragment}. This parameter is included
     *                                  because the list is possible to be re-used for some reasons.
     * @param locale                    is a used locale
     * @param <T>                       is a type of class
     * @return translated text
     */
    <T> String classTranslation(Class<T> clz,
                                String description,
                                List<TemplateParameter> descriptionTemplateParams,
                                Locale locale);

    /**
     * Makes translation by usage of a method.
     *
     * @param method                    is a method whose metadata may be used for auxiliary purposes
     * @param description               is a description of a step. It is commonly completed by value taken
     *                                  from {@link Description} of a method and by values which are taken from
     *                                  method parameters annotated by {@link DescriptionFragment}
     * @param descriptionTemplateParams is a list of parameters and their values. These parameters are taken from
     *                                  method parameters annotated by {@link DescriptionFragment}.This parameter is included
     *                                  because the list is possible to be re-used for some reasons.
     * @param locale                    is a used locale
     * @return translated text
     */
    String methodTranslation(Method method,
                             String description,
                             List<TemplateParameter> descriptionTemplateParams,
                             Locale locale);

    /**
     * Makes translation by usage of other annotated element that differs from {@link Class} and {@link Method}
     *
     * @param member      is an annotated element whose metadata may be used for auxiliary purposes
     * @param description is a description taken from the member
     * @param locale      is a used locale
     * @param <T>         is a type of member
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
