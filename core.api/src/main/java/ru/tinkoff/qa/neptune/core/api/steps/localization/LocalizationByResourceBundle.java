package ru.tinkoff.qa.neptune.core.api.steps.localization;

import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import static java.util.Optional.ofNullable;
import static java.util.ResourceBundle.getBundle;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator.RESOURCE_BUNDLE;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator.getKey;

public class LocalizationByResourceBundle implements StepLocalization {
    static ResourceBundle resourceBundle =
            ofNullable(DEFAULT_LOCALE_PROPERTY.get())
                    .map(locale -> getBundle(RESOURCE_BUNDLE, locale))
                    .orElse(getBundle(RESOURCE_BUNDLE));

    @Override
    public String classTranslation(Class<?> clz) {
        if (resourceBundle.containsKey(getKey(clz))) {
            return resourceBundle.getString(getKey(clz));
        }
        return clz.getAnnotation(Description.class).value();
    }

    @Override
    public String methodTranslation(Method method, Object... args) {
        if (resourceBundle.containsKey(getKey(method))) {
            return StepLocalization.translationByTemplate(method, resourceBundle.getString(getKey(method)), args);
        }
        return StepLocalization.translationByTemplate(method,
                method.getAnnotation(Description.class).value(),
                args);
    }

    @Override
    public <T extends AnnotatedElement & Member> String memberTranslation(T member) {
        if (resourceBundle.containsKey(getKey(member))) {
            return resourceBundle.getString(getKey(member));
        } else
            return member.getAnnotation(StepParameter.class).value();
    }
}
