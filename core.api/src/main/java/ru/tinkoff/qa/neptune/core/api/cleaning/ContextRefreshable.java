package ru.tinkoff.qa.neptune.core.api.cleaning;

import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public interface ContextRefreshable {

    /**
     * Performs the refreshing of a {@link Context} instance related to current thread.
     * It is considered that given subclass of {@link Context} has a static field of
     * same type as the class. This field should not be null. Also it is required
     * to fill the field with a value created by invocation of {@code Context.getInstance(Class)}
     *
     * @param toBeRefreshed
     */
    static void refreshContext(Class<? extends Context> toBeRefreshed) {
        if (!ContextRefreshable.class.isAssignableFrom(toBeRefreshed)) {
            return;
        }

        var field = stream(toBeRefreshed.getDeclaredFields())
                .filter(f -> {
                    if (!isStatic(f.getModifiers())) {
                        return false;
                    }

                    return f.getType().equals(toBeRefreshed);
                })
                .findFirst()
                .orElse(null);

        if (field == null) {
            return;
        }

        field.setAccessible(true);
        try {
            var value = field.get(toBeRefreshed);
            ofNullable(value).ifPresent(o -> ((ContextRefreshable) o).refreshContext());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    void refreshContext();
}
