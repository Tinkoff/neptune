package ru.tinkoff.qa.neptune.core.api.steps.parameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import java.lang.reflect.Constructor;
import java.time.Duration;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * Converts some value to a readable parameter of a step or to fragment of step description.
 * It is supposed to be used by the reading of {@link StepParameter#makeReadableBy()} and {@link DescriptionFragment#makeReadableBy()}
 *
 * @param <T> is a type of field value to covert
 */
public interface ParameterValueGetter<T> {
    String getParameterValue(T fieldValue) throws JsonProcessingException;

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

    final class DurationParameterValueGetter implements ParameterValueGetter<Duration> {

        @Override
        public String getParameterValue(Duration fieldValue) {
            return formatDurationHMS(fieldValue.toMillis());
        }
    }

    final class TranslatedDescriptionParameterValueGetter implements ParameterValueGetter<Object> {

        @Override
        public String getParameterValue(Object fieldValue) {
            var cls = fieldValue.getClass();

            if (SequentialGetStepSupplier.class.isAssignableFrom(cls)
                    || SequentialActionSupplier.class.isAssignableFrom(cls)) {
                return fieldValue.toString();
            }

            Description d = null;
            while (d == null && !cls.equals(Object.class)) {
                d = cls.getAnnotation(Description.class);
                cls = cls.getSuperclass();
            }

            return ofNullable(d)
                    .map(description -> translate(fieldValue))
                    .orElseGet(() -> translate(fieldValue.toString()));
        }
    }
}
