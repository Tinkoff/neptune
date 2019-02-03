package ru.tinkoff.qa.neptune.core.api.event.firing.annotation;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is designed to mark subclasses of {@link MakesCapturesOnFinishing}. When a class is marked by this
 * annotation then it means that it is needed to produce a {@link java.awt.image.BufferedImage} after invocation of
 * {@link java.util.function.Consumer#accept(Object)} or {@link java.util.function.Function#apply(Object)} on built resulted
 * {@link java.util.function.Consumer}/{@link java.util.function.Function}. This image is produced by
 * {@link Captor#getData(java.lang.Object)}
 *
 * <p>NOTE 1</p>
 * This image is produced if there is any subclass of {@link ImageCaptor}
 * or  {@link Captor} that may produce a {@link java.awt.image.BufferedImage}.
 *
 * <p>NOTE 2</p>
 * A subclass of {@link ImageCaptor} or
 * {@link Captor} should be able to handle input values of
 * {@link java.util.function.Consumer#accept(Object)} and/or resulted values of {@link java.util.function.Function#apply(Object)}.
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface MakeImageCapturesOnFinishing {
}
