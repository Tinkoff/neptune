package com.github.toy.constructor.core.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This is the service annotation which is designed to mark step-methods.
 * Also it is supposed to be used in byte-code manipulations.
 */
@Retention(RUNTIME) @Target({METHOD})
public @interface ToBeReported {
}
