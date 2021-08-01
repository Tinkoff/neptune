package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.function.BiConsumer;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure.CaptureOnFailureReader.readCaptorsOnFailure;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess.CaptureOnSuccessReader.readCaptorsOnSuccess;

final class FieldValueCaptureMaker<T extends Annotation> {

    private final Object o;
    private final Class<T> annotation;
    private final BiConsumer<Object, T> whatToDoWithFieldValue;

    private FieldValueCaptureMaker(Object o, Class<T> annotation, BiConsumer<Object, T> whatToDoWithFieldValue) {
        this.o = o;
        this.annotation = annotation;
        this.whatToDoWithFieldValue = whatToDoWithFieldValue;
    }

    void makeCaptures() {
        if (o == null) {
            return;
        }

        var clz = o.getClass();
        while (!clz.equals(Object.class)) {
            stream(clz.getDeclaredFields())
                    .filter(f -> !isStatic(f.getModifiers()) && f.getAnnotation(annotation) != null)
                    .forEach(f -> {
                        f.setAccessible(true);
                        try {
                            var value = f.get(o);
                            whatToDoWithFieldValue.accept(value, f.getAnnotation(annotation));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
            clz = clz.getSuperclass();
        }
    }

    static FieldValueCaptureMaker<CaptureOnSuccess> onSuccess(Object o) {
        return new FieldValueCaptureMaker<>(o, CaptureOnSuccess.class, (obj, onSuccess) -> {
            var captors = new ArrayList<Captor<Object, Object>>();
            readCaptorsOnSuccess(onSuccess, captors);
            catchValue(obj, captors);
        });
    }

    static FieldValueCaptureMaker<CaptureOnFailure> onFailure(Object o) {
        return new FieldValueCaptureMaker<>(o, CaptureOnFailure.class, (obj, onFailure) -> {
            var captors = new ArrayList<Captor<Object, Object>>();
            readCaptorsOnFailure(onFailure, captors);
            catchValue(obj, captors);
        });
    }
}
