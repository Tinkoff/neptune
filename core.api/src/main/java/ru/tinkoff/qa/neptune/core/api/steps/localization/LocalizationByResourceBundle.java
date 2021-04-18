package ru.tinkoff.qa.neptune.core.api.steps.localization;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static java.util.Optional.ofNullable;
import static java.util.ResourceBundle.getBundle;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator.RESOURCE_BUNDLE;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.ResourceBundleGenerator.getKey;

public class LocalizationByResourceBundle implements StepLocalization {

    private static ResourceBundle resourceBundle;
    private static boolean isRead;

    private static ResourceBundle getResourceBundle(Locale locale) {
        if (!isRead) {
            resourceBundle = ofNullable(resourceBundle)
                    .orElseGet(() -> {
                        try {
                            return getBundle(RESOURCE_BUNDLE, locale);
                        } catch (MissingResourceException e) {
                            return null;
                        }
                    });
            isRead = true;
        }
        return resourceBundle;
    }

    @Override
    public <T> String classTranslation(Class<T> clz, String description, Map<String, String> descriptionTemplateParams, Locale locale) {
        var bundle = getResourceBundle(locale);

        if (bundle == null) {
            return StepLocalization.buildTextByTemplate(description, descriptionTemplateParams);
        }

        if (bundle.containsKey(getKey(clz))) {
            return bundle.getString(getKey(clz));
        }

        return StepLocalization.buildTextByTemplate(description, descriptionTemplateParams);
    }

    @Override
    public String methodTranslation(Method method, String description, Map<String, String> descriptionTemplateParams, Locale locale) {
        var bundle = getResourceBundle(locale);
        if (bundle == null) {
            return StepLocalization.buildTextByTemplate(description, descriptionTemplateParams);
        }

        if (bundle.containsKey(getKey(method))) {
            return StepLocalization.buildTextByTemplate(bundle.getString(getKey(method)), descriptionTemplateParams);
        }

        return StepLocalization.buildTextByTemplate(description, descriptionTemplateParams);
    }

    @Override
    public <T extends AnnotatedElement & Member> String memberTranslation(T member, String description, Locale locale) {
        var bundle = getResourceBundle(locale);

        if (bundle == null) {
            return description;
        }

        if (bundle.containsKey(getKey(member))) {
            return bundle.getString(getKey(member));
        }
        return description;
    }

    @Override
    public String translation(String text, Locale locale) {
        return text;
    }
}
