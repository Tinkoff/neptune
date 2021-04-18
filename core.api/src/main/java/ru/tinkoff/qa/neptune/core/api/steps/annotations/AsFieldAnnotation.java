package ru.tinkoff.qa.neptune.core.api.steps.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotates annotations which may be used for creation {@link PseudoField}.
 * <P>ATTENTION!!!!!</P>
 * Annotated annotation should have declared method {@code String value()}
 */
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE})
public @interface AsFieldAnnotation {
}
