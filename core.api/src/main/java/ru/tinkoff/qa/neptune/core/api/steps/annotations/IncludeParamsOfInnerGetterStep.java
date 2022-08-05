package ru.tinkoff.qa.neptune.core.api.steps.annotations;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * It means that parameters (criteria, timeouts, custom parameters) of previous step created by {@link SequentialGetStepSupplier}
 * are included into definition of the next step in hierarchical step sequence.
 *
 * <p>ATTENTION!!!!</p>
 * Classes of objects of {@link SequentialGetStepSupplier} and {@link SequentialActionSupplier}
 * should have unique definitions of step parameters.
 *
 * @see SequentialGetStepSupplier.DefineCriteriaParameterName
 * @see SequentialGetStepSupplier.DefineTimeOutParameterName
 * @see SequentialGetStepSupplier.DefinePollingTimeParameterName
 * @see StepParameter
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface IncludeParamsOfInnerGetterStep {
}
