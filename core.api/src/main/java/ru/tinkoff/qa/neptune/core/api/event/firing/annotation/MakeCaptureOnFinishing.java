package ru.tinkoff.qa.neptune.core.api.event.firing.annotation;


import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is designed to mark subclasses of {@link MakesCapturesOnFinishing}. When a class is marked by this
 * annotation then it means that it is needed to produce some value after invocation of
 * {@link java.util.function.Consumer#accept(Object)} or {@link java.util.function.Function#apply(Object)} on built resulted
 * {@link java.util.function.Consumer}/{@link java.util.function.Function}. This value is produced by
 * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
 *
 * <p>NOTE 1</p>
 * This value is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor}
 * that may produce a value of type defined by {@link #typeOfCapture()}.
 *
 * <p>NOTE 2</p>
 * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values of
 * {@link java.util.function.Consumer#accept(Object)} and/or resulted values of {@link java.util.function.Function#apply(Object)}.
 */
@Retention(RUNTIME)
@Target({TYPE})
@Repeatable(MakeCapturesOnFinishingRepeatable.class)
public @interface MakeCaptureOnFinishing {

    /**
     * Defines a type of a value, that should be returned by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(Object)}
     *
     * @return a class of a value to be returned by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(Object)}
     */
    Class<?> typeOfCapture();
}
