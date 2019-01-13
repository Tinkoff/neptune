package ru.tinkoff.qa.neptune.core.api.event.firing.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE})
public @interface MakeCapturesOnFinishingRepeatable {
    MakeCaptureOnFinishing[] value();
}
