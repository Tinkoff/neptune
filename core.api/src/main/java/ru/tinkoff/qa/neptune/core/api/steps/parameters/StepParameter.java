package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static java.lang.String.valueOf;
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


    /**
     * Assigns a class that transforms value of a field to a readable parameter of a step.
     *
     * @return subclass of {@link ParameterValueGetter}
     */
    Class<? extends ParameterValueGetter<?>> makeReadableBy() default DefaultParameterValueGetter.class;

    /**
     * Converts value of a {@link Field} to a readable parameter of a step.
     * A field is supposed to be annotated by {@link StepParameter}.
     * Subclasses of ParameterValueGetter should not have any declared constructor except
     * public default constructor.
     *
     * @param <T> is a type of field value to covert
     */
    interface ParameterValueGetter<T> {

        String getParameterValue(T fieldValue);
    }

    final class DefaultParameterValueGetter implements ParameterValueGetter<Object> {

        @Override
        public String getParameterValue(Object fieldValue) {
            return valueOf(fieldValue);
        }
    }

    @SuppressWarnings("unchecked")
    final class ParameterValueReader {

        public static <T> String getParameterForStep(T fieldValue, StepParameter stepParameter) {

            try {
                var c = (Constructor<? extends ParameterValueGetter<T>>) stepParameter.makeReadableBy().getConstructor();
                c.setAccessible(true);
                var getter = c.newInstance();
                return getter.getParameterValue(fieldValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
