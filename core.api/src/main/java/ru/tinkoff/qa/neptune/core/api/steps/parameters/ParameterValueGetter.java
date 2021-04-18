package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.lang.reflect.Constructor;

import static java.lang.String.valueOf;

/**
 * Converts some value to a readable parameter of a step or to fragment of step description.
 * It is supposed to be used by the reading of {@link StepParameter#makeReadableBy()} and {@link DescriptionFragment#makeReadableBy()}
 *
 * @param <T> is a type of field value to covert
 */
public interface ParameterValueGetter<T> {
    String getParameterValue(T fieldValue);

    final class DefaultParameterValueGetter implements ParameterValueGetter<Object> {

        @Override
        public String getParameterValue(Object fieldValue) {
            return valueOf(fieldValue);
        }
    }

    final class ParameterValueReader {

        @SuppressWarnings("unchecked")
        public static <T> String getParameterForStep(T fieldValue, Class<? extends ParameterValueGetter<?>> cls) {

            try {
                var c = (Constructor<? extends ParameterValueGetter<T>>) cls.getConstructor();
                c.setAccessible(true);
                var getter = c.newInstance();
                return getter.getParameterValue(fieldValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
