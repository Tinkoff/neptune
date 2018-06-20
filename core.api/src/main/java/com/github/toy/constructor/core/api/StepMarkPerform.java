package com.github.toy.constructor.core.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * This is the service annotation is designed to mark step-methods that
 * perform some actions and don't return any result. It is supposed to be
 * used in byte-code manipulations.
 */
@Retention(RUNTIME) @Target({METHOD})
@interface StepMarkPerform {
}