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
    /**
     * @return defines class of a {@link Throwable} may be thrown when get-step returns no valuable data.
     * <P>ATTENTION!!!!!</P>
     * Defined class should have declared public constructor that takes message string with no other parameters.
     */
    Class<? extends RuntimeException> toThrow() default NotPresentException.class;

    String startDescription() default "Not present:";

    class ThrowWhenNoDataReader {

        public static ThrowWhenNoData getThrowableClass(Class<?> toRead) {
            var cls = toRead;
            while (!cls.equals(Object.class)) {
                var throwNoData = cls.getAnnotation(ThrowWhenNoData.class);
                if (throwNoData != null) {
                    return throwNoData;
                }
                cls = cls.getSuperclass();
            }
            return null;
        }
    }
}
