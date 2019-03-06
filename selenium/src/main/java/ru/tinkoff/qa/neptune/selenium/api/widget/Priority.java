package ru.tinkoff.qa.neptune.selenium.api.widget;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.String.format;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Optional.ofNullable;

/**
 * This annotation is used to define a priority of widgets to find.
 * It is useful when some subclass of the {@link Widget}
 * has few child classes and it is necessary to define which classes are supposed to be instantiated
 * first.
 */
@Retention(RUNTIME) @Target({TYPE})
public @interface Priority {
    int HIGHEST = 1;
    int LOWEST = MAX_VALUE;

    /**
     * Defines the priority of similar widgets for the searching. The lower number means the
     * higher priority. The value to be set should be between 1 and {@link Integer#MAX_VALUE}
     *
     * @return the number of priority of the searching for widget.
     */
    int value();

    final class Reader {
        /**
         * Reads priority of the searching for some widget.
         * @param toBeRead is a subclass of {@link Widget} to be read.
         * @return the number of priority of the searching for widget.
         */
        public static int getPriority(Class<? extends Widget> toBeRead) {
            Class<?> clazz = toBeRead;
            Priority priority = clazz.getAnnotation(Priority.class);
            if (priority == null) {
                while (!clazz.equals(Object.class)) {
                    clazz = clazz.getSuperclass();
                    priority = clazz.getAnnotation(Priority.class);
                }
            }

            Class<?> classWithPriority = clazz;

            return ofNullable(priority).map(priorityAnnotation -> {
                int priorityValue = priorityAnnotation.value();
                if (priorityValue < HIGHEST) {
                    throw new IllegalArgumentException(format("%s is annotated by @%s with illegal value %s. " +
                            "This value should be not lower than %s", classWithPriority.getName(),
                            Priority.class.getSimpleName(),
                            priorityValue,
                            HIGHEST));
                }
                return priorityValue;
            }).orElse(LOWEST);
        }
    }
}
