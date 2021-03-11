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

import static java.lang.String.valueOf;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

/**
 * Interface to translate step parameter and description.
 */
public interface StepLocalization {

    static String translate(AnnotatedElement annotatedElement, Object... args) {
        var engine = DEFAULT_LOCALIZATION_ENGINE.get();

        if (engine == null) {
            if (annotatedElement instanceof Method) {
                if (annotatedElement.getAnnotation(Description.class) != null) {
                    String description = annotatedElement.getAnnotation(Description.class).value();

                    return translationByTemplate((Method) annotatedElement, description, args);
                } else {
                    Class<?> clazz = ((Method) annotatedElement).getDeclaringClass();
                    while (clazz.getAnnotation(Description.class) == null && !clazz.equals(Object.class)) {
                        clazz = clazz.getSuperclass();
                    }
                    return clazz.getAnnotation(Description.class).value();
                }
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
                return engine.methodTranslation((Method) annotatedElement, args);
            } else {
                Class<?> clazz = ((Method) annotatedElement).getDeclaringClass();
                while (clazz.getAnnotation(Description.class) == null && !clazz.equals(Object.class)) {
                    clazz = clazz.getSuperclass();
                }
                return engine.classTranslation(clazz);
            }
        } else {
            if (annotatedElement.getAnnotation(StepParameter.class) != null) {
                if (annotatedElement instanceof Field) {
                    return engine.memberTranslation((Field) annotatedElement);
                }
                return engine.memberTranslation((PseudoField) annotatedElement);
            } else {
                return null;
            }
        }
    }

    static String translationByTemplate(Method method, String template, Object... args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            Annotation[] annotations = parameterAnnotations[argIndex];
            for (Annotation annotation : annotations) {
                if (!(annotation instanceof DescriptionFragment)) {
                    continue;
                }
                DescriptionFragment stepDescriptionFragment = (DescriptionFragment) annotation;
                template = template.replace("{" + stepDescriptionFragment.value() + "}", valueOf(args[argIndex]));
            }
        }

        return template;
    }

    String classTranslation(Class<?> clz);

    String methodTranslation(Method method, Object... parameters);

    <T extends AnnotatedElement & Member> String memberTranslation(T member);
}
