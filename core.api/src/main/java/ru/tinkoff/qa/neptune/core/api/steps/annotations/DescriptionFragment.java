package ru.tinkoff.qa.neptune.core.api.steps.annotations;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
public @interface DescriptionFragment {
    String value();

    Class<? extends ParameterValueGetter<?>> makeReadableBy() default ParameterValueGetter.DefaultParameterValueGetter.class;

}
