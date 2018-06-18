package com.github.toy.constructor.core.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * This is the service annotation which is designed to mark step-methods.
 * Also it is supposed to be used in byte-code manipulations.
 */
@Retention(RUNTIME) @Target({METHOD})
public @interface StepMark {
    /**
     * @return some constant part of the message to be logged.
     */
    String constantMessagePart() default EMPTY;
}
