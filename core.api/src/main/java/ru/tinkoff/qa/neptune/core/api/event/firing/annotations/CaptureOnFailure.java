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
 * then step is failed. All parameters defined by this annotation are inherited by subclasses. Also it may be
 * overridden in subclasses by declaration of another CaptureOnFailure.
 * 
 * Also it annotates fields subclasses of {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier} and
 * {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier}. Values of annotated fields form captures then
 * step is failed.
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface CaptureOnFailure {
    /**
     * @return subclasses of {@link Captor} that make captures on failure. ATTENTION!!! Defined classes should
     * have no declared constructors or they should have declared constructors without parameters. Also, it is possible
     * to define abstract classes there, but if they have no non-abstract subclasses in classpath then they are ignored
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Captor>[] by();

    class CaptureOnFailureReader {

        public static void readCaptorsOnFailure(Class<?> toRead, Collection<Captor<Object, Object>> toFill) {
            var cls = toRead;
            while (!cls.equals(Object.class)) {
                var onFailure = cls.getAnnotation(CaptureOnFailure.class);
                if (onFailure != null) {
                    toFill.addAll(createCaptors(onFailure.by()));
                    return;
                }
                cls = cls.getSuperclass();
            }
        }

        public static void readCaptorsOnFailure(CaptureOnFailure onFailure, Collection<Captor<Object, Object>> toFill) {
            if (onFailure != null) {
                toFill.addAll(createCaptors(onFailure.by()));
            }
        }
    }
}
