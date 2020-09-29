package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart;

import ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding.NOT_DEFINED;

/**
 * Marks a parameter of a {@link java.lang.reflect.Method} those variable value
 * is used as a part of a multipart request body.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface MultiPartBody {

    /**
     * @return name of a part
     */
    String name() default EMPTY;

    /**
     * @return is parameter required or not. Default value is {@code true}.
     * It allows or doesn't allow {@code null} values of a parameter on a method
     * invocation.
     */
    boolean isRequired() default true;

    /**
     * @return value of {@code Content-Transfer-Encoding}
     */
    ContentTransferEncoding contentTransferEncoding() default NOT_DEFINED;
}
