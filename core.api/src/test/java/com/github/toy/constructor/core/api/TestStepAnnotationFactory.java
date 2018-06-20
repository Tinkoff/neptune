package com.github.toy.constructor.core.api;

import java.lang.annotation.Annotation;

public class TestStepAnnotationFactory implements StepAnnotationFactory {

    private final TestAnnotation2 testAnnotation = new TestAnnotation2() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation2.class;
        }

        @Override
        public String value() {
            return "Test value";
        }
    };

    @Override
    public Annotation forPerform() {
        return testAnnotation;
    }

    @Override
    public Annotation forGet() {
        return testAnnotation;
    }

    @Override
    public Annotation forReturn() {
        return testAnnotation;
    }
}