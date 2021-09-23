package ru.tinkoff.qa.neptune.core.api.localization;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition.getKnownPartitions;
import static ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator.getKey;
import static ru.tinkoff.qa.neptune.core.api.localization.TemplateParameter.buildTextByTemplate;

public class LocalizationByResourceBundle implements StepLocalization {

    private static final Map<Locale, List<Properties>> resourceBundles = new HashMap<>();

    private String getFromResourceBundles(Locale locale, String key) {

        List<Properties> bundles;
        synchronized (resourceBundles) {
            bundles = resourceBundles.computeIfAbsent(locale,
                    l -> getKnownPartitions().stream().map(p -> p.getResourceBundle(locale)).collect(toList()));
        }

        for (var rb : bundles) {
            if (rb.containsKey(key)) {
                return rb.getProperty(key);
            }
        }

        return null;
    }

    @Override
    public <T> String classTranslation(Class<T> clz, String description, List<TemplateParameter> descriptionTemplateParams, Locale locale) {
        var value = getFromResourceBundles(locale, getKey(clz));

        if (value == null) {
            return buildTextByTemplate(description, descriptionTemplateParams);
        }

        return buildTextByTemplate(value, descriptionTemplateParams);
    }

    @Override
    public String methodTranslation(Method method, String description, List<TemplateParameter> descriptionTemplateParams, Locale locale) {
        var value = getFromResourceBundles(locale, getKey(method));

        if (value == null) {
            return buildTextByTemplate(description, descriptionTemplateParams);
        }

        return buildTextByTemplate(value, descriptionTemplateParams);
    }

    @Override
    public <T extends AnnotatedElement & Member> String memberTranslation(T member, String description, Locale locale) {
        var value = getFromResourceBundles(locale, getKey(member));

        if (value == null) {
            return description;
        }
        return value;
    }

    @Override
    public String translation(String text, Locale locale) {
        return text;
    }
}
