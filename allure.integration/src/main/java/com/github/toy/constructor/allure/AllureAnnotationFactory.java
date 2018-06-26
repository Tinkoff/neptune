package com.github.toy.constructor.allure;

import com.github.toy.constructor.core.api.StepAnnotationFactory;
import io.qameta.allure.Step;

import java.lang.annotation.Annotation;

public class AllureAnnotationFactory implements StepAnnotationFactory {
    @Override
    public Annotation forPerform() {
        return new Step() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Step.class;
            }

            @Override
            public String value() {
                return "Perform: {0}";
            }
        };
    }

    @Override
    public Annotation forGet() {
        return new Step() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Step.class;
            }

            @Override
            public String value() {
                return "Get: {0}";
            }
        };
    }

    @Override
    public Annotation forReturn() {
        return new Step() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Step.class;
            }

            @Override
            public String value() {
                return "Returned value: {0}";
            }
        };
    }
}
