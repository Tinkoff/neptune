package ru.tinkoff.qa.neptune.core.api.steps.annotations;

import ru.tinkoff.qa.neptune.core.api.steps.NotPresentException;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotates {@link ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier} and defines class of a
 * {@link Throwable} may be thrown when get-step returns no valuable data. All that is defined by this annotation
 * is inherited by subclasses. Also it may be overridden in subclasses by declaration of another 'ThrowWhenNoData'
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface ThrowWhenNoData {

    String DEFAULT_MESSAGE_STARTING = "Not present:";

    /**
     * @return defines class of a {@link Throwable} may be thrown when get-step returns no valuable data.
     * <P>ATTENTION!!!!!</P>
     * <ul>
     *     <li>Defined class SHOULD have declared public constructor that takes only message string</li>
     *     <li>Defined class can have declared public constructor that takes message string and {@link Throwable}
     *     root-cause</li>
     * </ul>
     */
    Class<? extends RuntimeException> toThrow() default NotPresentException.class;

    String startDescription() default DEFAULT_MESSAGE_STARTING;

    class ThrowWhenNoDataReader {

        public static Class<?> getDeclaredBy(Class<?> toRead, boolean withInheritance) {
            var cls = toRead;
            var throwNoData = cls.getAnnotation(ThrowWhenNoData.class);

            if (withInheritance) {
                while (!cls.equals(Object.class) && throwNoData == null) {
                    throwNoData = cls.getAnnotation(ThrowWhenNoData.class);
                    if (throwNoData != null) {
                        return cls;
                    }
                    cls = cls.getSuperclass();
                }
            }

            if (throwNoData != null) {
                return cls;
            }

            return null;
        }
    }
}
