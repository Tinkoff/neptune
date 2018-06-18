package com.github.toy.constructor.core.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Retention(RUNTIME) @Target({METHOD})
public @interface TestAnnotation {
    String value() default EMPTY;
}
