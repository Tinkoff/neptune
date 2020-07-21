package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Defines a name of a field of a parameter of http method.
 * It is applied to fields of classes marked by {@link MethodParameter}
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ParameterFieldName {

    /**
     * @return name of a field of a parameter of http method.
     */
    String value();
}
