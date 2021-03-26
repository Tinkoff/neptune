package ru.tinkoff.qa.neptune.core.api.steps.localization;

import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.util.Optional.ofNullable;
import static java.util.ResourceBundle.getBundle;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator.RESOURCE_BUNDLE;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator.getKey;

public class LocalizationByResourceBundle implements StepLocalization {
    private static ResourceBundle resourceBundle;

    private static ResourceBundle getResourceBundle(Locale locale) {
        resourceBundle = ofNullable(resourceBundle)
                .orElse(getBundle(RESOURCE_BUNDLE, locale));
        return resourceBundle;
    }

    @Override
    public String classTranslation(Class<?> clz, Locale locale) {
        var bundle = getResourceBundle(locale);
        if (bundle.containsKey(getKey(clz))) {
            return bundle.getString(getKey(clz));
        }
        return clz.getAnnotation(Description.class).value();
    }

    @Override
    public String methodTranslation(Method method, Locale locale, Object... args) {
        var bundle = getResourceBundle(locale);

        if (bundle.containsKey(getKey(method))) {
            return StepLocalization.buildTextByTemplate(method, bundle.getString(getKey(method)), args);
        }
        return StepLocalization.buildTextByTemplate(method,
                method.getAnnotation(Description.class).value(),
                args);
    }

    @Override
    public <T extends AnnotatedElement & Member> String memberTranslation(T member, Locale locale) {
        var bundle = getResourceBundle(locale);

        if (bundle.containsKey(getKey(member))) {
            return bundle.getString(getKey(member));
        } else
            return member.getAnnotation(StepParameter.class).value();
    }

    @Override
    public String translation(String text, Locale locale) {
        return text;
    }
}
