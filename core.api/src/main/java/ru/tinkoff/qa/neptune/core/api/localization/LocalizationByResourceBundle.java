package ru.tinkoff.qa.neptune.core.api.localization;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationBundlePartition.getKnownPartitions;
import static ru.tinkoff.qa.neptune.core.api.localization.ResourceBundleGenerator.getKey;
import static ru.tinkoff.qa.neptune.core.api.localization.TemplateParameter.buildTextByTemplate;

public final class LocalizationByResourceBundle implements StepLocalization {

    private static final Map<Locale, List<Map<String, String>>> RESOURCE_BUNDLES = new HashMap<>();

    public static String getFromResourceBundles(Locale locale, AnnotatedElement e) {
        List<Map<String, String>> bundles;
        synchronized (RESOURCE_BUNDLES) {
            bundles = RESOURCE_BUNDLES.computeIfAbsent(locale,
                    l -> getKnownPartitions().stream().map(p -> p.getResourceBundle(locale))
                            .filter(Objects::nonNull)
                            .collect(toList()));
        }

        var key = getKey(e);
        for (var rb : bundles) {
            if (rb.containsKey(key)) {
                return rb.get(key);
            }
        }

        return null;
    }

    @Override
    public <T> String classTranslation(Class<T> clz, String template, List<TemplateParameter> descriptionTemplateParams, Locale locale) {
        var value = getFromResourceBundles(locale, clz);

        if (value == null) {
            return buildTextByTemplate(template, descriptionTemplateParams);
        }

        return buildTextByTemplate(value, descriptionTemplateParams);
    }

    @Override
    public String methodTranslation(Method method, String template, List<TemplateParameter> descriptionTemplateParams, Locale locale) {
        var value = getFromResourceBundles(locale, method);

        if (value == null) {
            return buildTextByTemplate(template, descriptionTemplateParams);
        }

        return buildTextByTemplate(value, descriptionTemplateParams);
    }

    @Override
    public <T extends AnnotatedElement & Member> String memberTranslation(T member, String toBeTranslated, Locale locale) {
        var value = getFromResourceBundles(locale, member);

        if (value == null) {
            return toBeTranslated;
        }
        return value;
    }

    @Override
    public String translation(String text, Locale locale) {
        return text;
    }
}
