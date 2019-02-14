package ru.tinkoff.qa.neptune.core.api.concurency;

import ru.tinkoff.qa.neptune.core.api.steps.context.ActionStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Thread.currentThread;
import static java.util.Collections.synchronizedSet;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class ObjectContainer<T> {

    private static final Set<ObjectContainer<?>> containers = synchronizedSet(new HashSet<>());

    private final T t;
    private Thread busyBy;

    public ObjectContainer(T t) {
        checkNotNull(t);
        checkArgument(ActionStepContext.class.isAssignableFrom(t.getClass()) ||
                GetStepContext.class.isAssignableFrom(t.getClass()), "Class of an object should be " +
                "assignable from GetStepContext and/or " +
                "ActionStepContext.");
        this.t = t;
        synchronized (containers) {
            this.setBusy(currentThread());
            containers.add(this);
        }
    }

    public static <T> List<ObjectContainer<?>> getAllObjects(Class<T> tClass,
                                                             Predicate<ObjectContainer<?>> predicate) {
        checkNotNull(tClass);
        checkNotNull(predicate);
        checkArgument(ActionStepContext.class.isAssignableFrom(tClass) ||
                GetStepContext.class.isAssignableFrom(tClass), "Class of an object should be " +
                "assignable from GetStepContext and/or " +
                "ActionStepContext.");
        synchronized (containers) {
            return containers.stream().filter(predicate
                    .and(objectContainer -> tClass.isAssignableFrom(objectContainer.getWrappedObject().getClass()))).collect(toList());
        }
    }

    /**
     * Sets any free object of {@link ObjectContainer} by current thread and returns it when any one is found.
     *
     * @param tClass is a class of wrapped objects. {@link ObjectContainer#getWrappedObject()}
     * @param <T> is a type of wrapped objects.
     * @return an object of {@link ObjectContainer} that has become busy if there is some objects free of threads.
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> ObjectContainer<T> setObjectBusy(Class<T> tClass) {
        var freeObjects = getAllObjects(tClass, objectContainer ->
                !objectContainer.isBusy());
        if (freeObjects.size() == 0) {
            return null;
        }
        var result = freeObjects.get(0);
        result.setBusy(currentThread());
        return (ObjectContainer<T>) result;
    }

    private synchronized boolean isBusy() {
        return nonNull(busyBy);
    }

    private synchronized void setBusy(Thread thread) {
        this.busyBy = thread;
        new ThreadStateLoop(currentThread(), this).start();
    }

    synchronized void setFree() {
        this.busyBy = null;
    }

    public T getWrappedObject() {
        return t;
    }
}
