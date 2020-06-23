package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.nio.file.Path;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method}. It is used to define {@code filename}
 * of a part of a multipart body. This annotation has an effect when {@link Parameter} is annotated
 * by {@link MultiPartBody}. When a parameter has a type that differs from {@link File} and {@link Path}
 * or {@link #useGivenFileName()} is {@code false} then file name is generated randomly by default.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface DefineFileName {
    /**
     * @return to use name of a file as a {@code filename} or not. It has sense when
     * type of a {@link Parameter} is {@link File}/{@link Path}
     */
    boolean useGivenFileName() default false;
}
