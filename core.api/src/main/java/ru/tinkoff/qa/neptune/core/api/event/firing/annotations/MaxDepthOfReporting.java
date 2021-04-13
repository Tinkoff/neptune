package ru.tinkoff.qa.neptune.core.api.event.firing.annotations;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepAction;
import ru.tinkoff.qa.neptune.core.api.steps.StepFunction;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.Thread.currentThread;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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

        public static int getMaxDepth(Class<?> cls) {
            var clazz = cls;
            while (!clazz.equals(Object.class)) {
                var maxDepthOfReporting = clazz.getAnnotation(MaxDepthOfReporting.class);
                if (maxDepthOfReporting != null) {
                    var depth = maxDepthOfReporting.value();
                    if (depth < 0) {
                        throw new IllegalArgumentException("Min value of the reporting depth is 0. Please check and improve "
                                + "the class"
                                + clazz.getName());
                    }
                    return depth;
                }
                clazz = clazz.getSuperclass();
            }

            return Integer.MAX_VALUE;
        }

        public static int getCurrentDepth() {
            var trace = currentThread().getStackTrace();
            var depth = -1;
            for (int i = trace.length - 1; i >= 0; i--) {
                var className = trace[i].getClassName();
                var methodName = trace[i].getMethodName();
                if ((className.equals(StepFunction.class.getName()) || className.equals(StepAction.class.getName()))
                        &&
                        (methodName.equals("apply") || methodName.equals("performAction"))) {
                    depth++;
                }
            }
            return depth;
        }
    }
}
