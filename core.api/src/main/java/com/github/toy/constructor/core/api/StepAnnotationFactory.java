package com.github.toy.constructor.core.api;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Function;

public interface StepAnnotationFactory {

    /**
     * This method generates annotation to annotate step methods that
     * perform some actions and don't return any result. This is {@link PerformStep#perform(Consumer)}
     * generally.
     *
     * @return generated annotation
     */
    Annotation forPerform();

    /**
     * This method generates annotation to annotate step methods that
     * return some result. This is {@link GetStep#get(Function)}
     * generally.
     *
     * @return generated annotation
     */
    Annotation forGet();

    /**
     * This method generates annotation to annotate step methods that
     * fire some returned values. This is {@link GetStep#fireValue(Object)}
     * generally.
     *
     * @return generated annotation
     */
    Annotation forReturn();
}
