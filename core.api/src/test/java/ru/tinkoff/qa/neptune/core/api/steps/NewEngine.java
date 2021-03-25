package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Locale;

public class NewEngine implements StepLocalization {
    @Override
    public String classTranslation(Class<?> clz, Locale locale) {
        return "I'm class";
    }

    @Override
    public String methodTranslation(Method method, Locale locale, Object... parameters) {
        return "I'm method";
    }

    @Override
    public <T extends AnnotatedElement & Member> String memberTranslation(T member, Locale locale) {
        return "I'm member";
    }

    @Override
    public String translation(String text, Locale locale) {
        return "kek";
    }
}
