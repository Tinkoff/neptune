package ru.tinkoff.qa.neptune.core.api.event.firing.annotations;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepAction;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Optional.ofNullable;

/**
 * Annotates {@link SequentialGetStepSupplier} and
 * {@link SequentialActionSupplier} and defines max
 * depth of reporting available.
 * <p></p>
 * In runtime depth is calculated by depth of invocations of {@link StepFunction#apply(Object)} or
 * {@link StepAction#performAction(Object)} one from another.
 * <p></p>
 * Minimal value is 0.
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface MaxDepthOfReporting {
    int value();

    class MaxDepthOfReportingReader {

        static int getMaxDepth(Class<?> cls) {
            return ofNullable(cls.getAnnotation(MaxDepthOfReporting.class))
                    .map(maxDepthOfReporting -> {
                        var depth = maxDepthOfReporting.value();
                        if (maxDepthOfReporting.value() < 0) {
                            throw new IllegalArgumentException("Min value of the reporting depth is 0. Please check and improve "
                                    + "the class"
                                    + cls.getName());
                        }

                        return depth;
                    }).orElse(Integer.MAX_VALUE);
        }
    }
}
