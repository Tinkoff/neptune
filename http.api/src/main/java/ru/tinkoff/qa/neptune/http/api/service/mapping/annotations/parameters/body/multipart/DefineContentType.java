package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method}. It is used to define {@code Content-Type}
 * of a part of a multipart body. This annotation has an effect when {@link Parameter} is annotated
 * by {@link MultiPartBody}. When {@link #contentType()} is not defined then the value is set up/calculated
 * automatically.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface DefineContentType {

    /**
     * @return {@code Content-Type} of a part of a multipart body.
     */
    String contentType() default EMPTY;
}
