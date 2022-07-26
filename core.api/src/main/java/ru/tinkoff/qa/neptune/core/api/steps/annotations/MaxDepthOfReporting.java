package ru.tinkoff.qa.neptune.core.api.steps.annotations;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

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
 * In runtime depth is calculated by depth of invocations of steps created by {@link SequentialGetStepSupplier} or
 * {@link SequentialActionSupplier} one inside another.
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
            for (StackTraceElement stackTraceElement : trace) {
                var className = stackTraceElement.getClassName();
                var methodName = stackTraceElement.getMethodName();
                if ((className.equals("ru.tinkoff.qa.neptune.core.api.steps.Get")
                        || className.equals("ru.tinkoff.qa.neptune.core.api.steps.ActionImpl"))
                        &&
                        (methodName.equals("apply") || methodName.equals("performAction"))) {
                    depth++;
                }
            }
            return depth;
        }
    }
}
