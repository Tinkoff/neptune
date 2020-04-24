package ru.tinkoff.qa.neptune.core.api.steps;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks fields of {@link SequentialActionSupplier} and {@link SequentialGetStepSupplier} those
 * values should be reported as parameters of steps
 */
@Retention(RUNTIME)
@Target({FIELD})
public @interface StepParameter {

    /**
     * Defines a name of a step parameter
     *
     * @return step parameter name
     */
    String value();

    /**
     * Defines whenever to skip or not parameter whose value is {@code null}
     *
     * @return whenever to skip or not parameter whose value is {@code null}
     */
    boolean doNotReportNullValues() default false;
}