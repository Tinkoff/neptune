package ru.tinkoff.qa.neptune.core.api.cleaning;

import io.github.classgraph.ClassGraph;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContext;

@SuppressWarnings("rawtypes")
public interface ContextRefreshable {

    List<Class<? extends Context>> REFRESHABLE_CONTEXTS = new ClassGraph()
            .enableClassInfo()
            .ignoreClassVisibility()
            .scan()
            .getSubclasses(Context.class.getName())
            .loadClasses(Context.class)
            .stream()
            .filter(ContextRefreshable.class::isAssignableFrom)
            .collect(toList());

    /**
     * Performs the refreshing of an instance of {@link Context} which belongs to current thread.
     *
     * @param toBeRefreshed is a class of an instance to be refreshed
     */
    static void refreshContext(Class<? extends Context> toBeRefreshed) {
        if (!ContextRefreshable.class.isAssignableFrom(toBeRefreshed)) {
            return;
        }

        var context = getCreatedContext(toBeRefreshed);
        if (nonNull(context)) {
            ((ContextRefreshable) context).refreshContext();
        }
    }

    void refreshContext();
}
