package ru.tinkoff.qa.neptune.core.api.event.firing.annotation;

import java.lang.annotation.Annotation;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;

public interface MakesCapturesOnFinishing<T extends MakesCapturesOnFinishing> {

    private static <T extends MakesCapturesOnFinishing, S extends Annotation> S[] annotations(Class<? extends T> clazz,
                                                                                              Class<S> annotationClass) {
        var classToCheck = (Class<?>) clazz;
        var annotations = classToCheck.getAnnotationsByType(annotationClass);

        while (!classToCheck.equals(Object.class) && annotations.length == 0) {
            classToCheck = classToCheck.getSuperclass();
            annotations = classToCheck.getAnnotationsByType(annotationClass);
        }
        return annotations;
    }

    /**
     * It is possible to annotate any subclass of {@link MakesCapturesOnFinishing} by {@link MakeImageCapturesOnFinishing},
     * {@link MakeFileCapturesOnFinishing}, {@link MakeStringCapturesOnFinishing} and {@link MakeCaptureOnFinishing} to
     * define captures to be made by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     * after invocation of {@link java.util.function.Consumer#accept(Object)} or
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Consumer}/
     * {@link java.util.function.Function}. This method makes the setting up according to annotations used by a class of
     * the object/superclasses.
     *
     * @param toBeSetUp an instance of {@link MakesCapturesOnFinishing} to be tuned.
     * @param <T>       is a type of {@link MakesCapturesOnFinishing}
     */
    @SuppressWarnings("unchecked")
    static <T extends MakesCapturesOnFinishing> void makeCaptureSettings(T toBeSetUp) {
        checkNotNull(toBeSetUp);
        var clazz = toBeSetUp.getClass();

        if (annotations(clazz, MakeImageCapturesOnFinishing.class).length > 0) {
            toBeSetUp.makeImageCaptureOnFinish();
        }

        if (annotations(clazz, MakeFileCapturesOnFinishing.class).length > 0) {
            toBeSetUp.makeFileCaptureOnFinish();
        }

        if (annotations(clazz, MakeStringCapturesOnFinishing.class).length > 0) {
            toBeSetUp.makeStringCaptureOnFinish();
        }

        MakeCaptureOnFinishing[] makesCaptureOnFinishing;
        if ((makesCaptureOnFinishing = annotations(clazz, MakeCaptureOnFinishing.class)).length > 0) {
            stream(makesCaptureOnFinishing).forEach(makeCaptureOnFinishing ->
                    toBeSetUp.onFinishMakeCaptureOfType(makeCaptureOnFinishing.typeOfCapture()));
        }
    }

    /**
     * Marks that it is needed to produce a {@link java.awt.image.BufferedImage} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} or {@link java.util.function.Function#apply(Object)} on built resulted
     * {@link java.util.function.Consumer}/{@link java.util.function.Function}. This image is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This image is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.awt.image.BufferedImage}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values of
     * {@link java.util.function.Consumer#accept(Object)} and/or resulted values of {@link java.util.function.Function#apply(Object)}.
     *
     * @return self-reference
     */
    T makeImageCaptureOnFinish();

    /**
     * Marks that it is needed to produce a {@link java.io.File} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} or {@link java.util.function.Function#apply(Object)} on built resulted
     * {@link java.util.function.Consumer}/{@link java.util.function.Function}. This file is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This file is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.io.File}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values of
     * {@link java.util.function.Consumer#accept(Object)} and/or resulted values of {@link java.util.function.Function#apply(Object)}.
     *
     * @return self-reference
     */
    T makeFileCaptureOnFinish();

    /**
     * Marks that it is needed to produce a {@link java.lang.StringBuilder} after invocation of
     * {@link java.util.function.Consumer#accept(Object)} or {@link java.util.function.Function#apply(Object)} on built resulted
     * {@link java.util.function.Consumer}/{@link java.util.function.Function}.This string builder is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This string builder is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor}
     * or {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} that may produce a {@link java.lang.StringBuilder}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor} or
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values of
     * {@link java.util.function.Consumer#accept(Object)} and/or resulted values of {@link java.util.function.Function#apply(Object)}.
     *
     * @return self-reference
     */
    T makeStringCaptureOnFinish();

    /**
     * Marks that it is needed to produce some value after invocation of
     * {@link java.util.function.Consumer#accept(Object)} or {@link java.util.function.Function#apply(Object)} on built resulted
     * {@link java.util.function.Consumer}/{@link java.util.function.Function}.This value is produced by
     * {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This value is produced if there is any subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor}
     * that may produce something of type defined by {@param typeOfCapture}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor} should be able to handle input values of
     * {@link java.util.function.Consumer#accept(Object)} and/or resulted values of {@link java.util.function.Function#apply(Object)}.
     *
     * @param typeOfCapture is a type of a value to produce by {@link ru.tinkoff.qa.neptune.core.api.event.firing.Captor#getData(java.lang.Object)}
     * @return self-reference
     */
    T onFinishMakeCaptureOfType(Class<?> typeOfCapture);
}
