package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.localization.StepLocalization;
import ru.tinkoff.qa.neptune.core.api.localization.TemplateParameter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class NewEngine implements StepLocalization {

    @Override
    public <T> String classTranslation(Class<T> clz, String description, List<TemplateParameter> descriptionTemplateParams, Locale locale) {
        return "I'm class";
    }

    @Override
    public String methodTranslation(Method method, String description, List<TemplateParameter> descriptionTemplateParams, Locale locale) {
        return "I'm method";
    }

    @Override
    public <T extends AnnotatedElement & Member> String memberTranslation(T member, String description, Locale locale) {
        return "I'm member";
    }

    @Override
    public String translation(String text, Locale locale) {
        return "kek";
    }
}
