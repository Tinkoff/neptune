package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form;

import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.query.QueryParameter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.FORM;

/**
 * Defines a form parameter. This annotation is applied to a {@link java.lang.reflect.Parameter}
 * that represents a parameter of a query of http request/parameter of {@code x-www-form-urlencoded}
 * request body.
 *
 * @see QueryParameter
 * @see URLEncodedParameter
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface FormParam {

    /**
     * @return to explode parameter value or not. Default value is {@code true}.
     * This has an effect when parameter value has type {@link Map} or type of some POJO.
     */
    boolean explode() default true;


    /**
     * @return to percent-encode reserved characters or not. These characters are kept as they are
     * when it is {@code true}
     */
    boolean allowReserved() default false;


    /**
     * @return style of a form parameter. Default is {@link FormStyles#FORM}
     */
    FormStyles style() default FORM;

}
