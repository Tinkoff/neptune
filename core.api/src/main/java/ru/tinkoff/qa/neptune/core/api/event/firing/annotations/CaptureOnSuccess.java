package ru.tinkoff.qa.neptune.core.api.event.firing.annotations;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;

/**
 * Annotates subclasses of {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier} and
 * {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier} to define which capture should be made
 * then step is succeed. All parameters defined by this annotation are inherited by subclasses. Also it may be
 * overridden in subclasses by declaration of another CaptureOnSuccess.
 *
 * Also it annotates fields subclasses of {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier} and
 * {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier}. Values of annotated fields form captures then
 * step is succeed.
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface CaptureOnSuccess {
    /**
     * @return subclasses of {@link Captor} that make captures on success. ATTENTION!!! Defined classes should
     * have no declared constructors or they should have declared constructors without parameters. Also, it is possible
     * to define abstract classes there, but if they have no non-abstract subclasses in classpath then they are ignored
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Captor>[] by();

    class CaptureOnSuccessReader {

        public static void readCaptorsOnSuccess(Class<?> toRead, Collection<Captor<Object, Object>> toFill) {
            var cls = toRead;
            while (!cls.equals(Object.class)) {
                var onSuccess = cls.getAnnotation(CaptureOnSuccess.class);
                readCaptorsOnSuccess(onSuccess, toFill);
                cls = cls.getSuperclass();
            }
        }

        public static void readCaptorsOnSuccess(CaptureOnSuccess onSuccess, Collection<Captor<Object, Object>> toFill) {
            if (onSuccess != null) {
                toFill.addAll(createCaptors(onSuccess.by()));
            }
        }
    }
}
