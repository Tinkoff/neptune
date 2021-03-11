package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

public class PseudoField implements AnnotatedElement, Member {
    private final Class<?> clazz;
    private final String name;
    private final String value;

    public PseudoField(Class<?> clazz, String name, String value) {
        this.clazz = clazz;
        this.name = name;
        this.value = value;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return clazz;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        if (annotationClass.isAssignableFrom(StepParameter.class)) {
            return (T) new StepParameter() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return StepParameter.class;
                }

                @Override
                public String value() {
                    return value;
                }

                @Override
                public boolean doNotReportNullValues() {
                    return false;
                }

                @Override
                public Class<? extends ParameterValueGetter<?>> makeReadableBy() {
                    return null;
                }
            };
        }
        return null;
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public boolean isSynthetic() {
        return false;
    }
}
