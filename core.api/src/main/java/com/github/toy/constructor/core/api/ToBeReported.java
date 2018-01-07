package com.github.toy.constructor.core.api;

/**
 * This is the service annotation which is designed to mark step-methods.
 * Also it is supposed to be used in byte-code manipulations.
 */
@interface ToBeReported {
    static final String EXPLICIT_PATTERN = "{0}";

    String message() default "";
}
