package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body;

import ru.tinkoff.qa.neptune.http.api.request.body.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.NONE;

/**
 * Defines format of http request body parameter. This annotation is applied to {@link java.lang.reflect.Parameter}
 * annotated by {@link Body}, {@link URLEncodedParameter} or {@link MultiPartBody}
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface BodyParamFormat {

    /**
     * @return format of http request body parameter. Default value is {@link BodyDataFormat#NONE}
     */
    BodyDataFormat format() default NONE;

    /**
     * Is used for cases when body parameter is json or xml
     *
     * @return array of mixin classes
     * @see com.fasterxml.jackson.databind.ObjectMapper#addMixIn(Class, Class)
     */
    Class[] mixIns() default {};
}
