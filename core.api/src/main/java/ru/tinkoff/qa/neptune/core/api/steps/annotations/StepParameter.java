package ru.tinkoff.qa.neptune.core.api.steps.annotations;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.lang.annotation.Annotation;
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
@Metadata
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


    /**
     * Assigns a class that transforms value of a field to a readable parameter of a step.
     *
     * @return subclass of {@link ParameterValueGetter}
     */
    Class<? extends ParameterValueGetter<?>> makeReadableBy() default ParameterValueGetter.DefaultParameterValueGetter.class;

    final class StepParameterCreator {

        public static StepParameter createStepParameter(String value) {
            return new StepParameter() {
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
    }
}
