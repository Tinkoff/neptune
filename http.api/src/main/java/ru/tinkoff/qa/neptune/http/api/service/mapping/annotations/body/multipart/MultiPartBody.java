package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.multipart;

import ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.BodyDataFormat;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding.NOT_DEFINED;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.body.BodyDataFormat.NONE;

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
     * @return format of a part of http request body.
     */
    BodyDataFormat format() default NONE;

    /**
     * Is used for cases when body object is json or xml
     *
     * @return array of mixin classes
     * @see com.fasterxml.jackson.databind.ObjectMapper#addMixIn(Class, Class)
     */
    Class[] mixIns() default {};

    /**
     * @return value of {@code Content-Transfer-Encoding}
     */
    ContentTransferEncoding contentTransferEncoding() default NOT_DEFINED;
}
