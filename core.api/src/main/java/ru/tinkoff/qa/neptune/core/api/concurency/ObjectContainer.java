package ru.tinkoff.qa.neptune.core.api.concurency;

import ru.tinkoff.qa.neptune.core.api.GetStep;
import ru.tinkoff.qa.neptune.core.api.PerformActionStep;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Thread.currentThread;
import static java.util.Collections.synchronizedSet;
import static java.util.stream.Collectors.toList;

public class ObjectContainer<T> {

    private static final Set<ObjectContainer<?>> containers = synchronizedSet(new HashSet<>());

    private final T t;
    private Thread busyBy;
    private Object groupBy;

    public ObjectContainer(T t) {
        checkNotNull(t);
        checkArgument(PerformActionStep.class.isAssignableFrom(t.getClass()) ||
                GetStep.class.isAssignableFrom(t.getClass()), "Class of an object should be " +
                "assignable from ru.tinkoff.qa.neptune.core.api.GetStep and/or " +
                "ru.tinkoff.qa.neptune.core.api.PerformActionStep.");
        this.t = t;
        synchronized (containers) {
            containers.add(this);
        }
    }

    private static <T> List<ObjectContainer<?>> getAllObjects(Class<T> tClass,
                                                              Predicate<ObjectContainer<?>> predicate) {
        checkNotNull(tClass);
        checkNotNull(predicate);
        checkArgument(PerformActionStep.class.isAssignableFrom(tClass) ||
                GetStep.class.isAssignableFrom(tClass), "Class of an object should be " +
                "assignable from ru.tinkoff.qa.neptune.core.api.GetStep and/or " +
                "ru.tinkoff.qa.neptune.core.api.PerformActionStep.");
        synchronized (containers) {
            return containers.stream().filter(predicate).collect(toList());
        }
    }

    /**
     * Gets all objects of {@link ObjectContainer}.
     *
     * @param tClass is a class of wrapped objects. {@link ObjectContainer#getWrappedObject()}
     * @param <T> is a type of wrapped objects.
     * @return filled or empty list of objects of {@link ObjectContainer}.
     */
    public static synchronized <T> List<ObjectContainer<?>> getAllObjects(Class<T> tClass) {
        return getAllObjects(tClass, objectContainer ->
                tClass.isAssignableFrom(objectContainer.getWrappedObject().getClass()));
    }

    /**
     * Gets all objects of {@link ObjectContainer} grouped by some other object.
     *
     * @param tClass is a class of wrapped objects. {@link ObjectContainer#getWrappedObject()}
     * @param groupingBy is the grouping object.
     * @param <T> is a type of wrapped objects.
     * @return filled or empty list of objects of {@link ObjectContainer} grouped by some other object.
     */
    public static synchronized <T> List<ObjectContainer<?>> getAllObjects(Class<T> tClass, Object groupingBy) {
        checkNotNull(groupingBy);
        return getAllObjects(tClass, objectContainer ->
                tClass.isAssignableFrom(objectContainer.getWrappedObject().getClass())
                        && groupingBy.equals(objectContainer.getGroupBy()));
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
        List<ObjectContainer<?>> freeObjects = getAllObjects(tClass, objectContainer ->
                !objectContainer.isBusy() && tClass.isAssignableFrom(objectContainer.getWrappedObject().getClass()));
        if (freeObjects.size() == 0) {
            return null;
        }
        ObjectContainer<?> result = freeObjects.get(0);
        result.setBusy(currentThread());
        return (ObjectContainer<T>) result;
    }

    /**
     * Sets any free object of {@link ObjectContainer} by current thread and returns it when any one is found.
     *
     * @param tClass is a class of wrapped objects. {@link ObjectContainer#getWrappedObject()}
     * @param groupingBy is the grouping object.
     * @param <T> is a type of wrapped objects.
     * @return an object of {@link ObjectContainer} that has become busy if there is some objects free of threads.
     */
    @SuppressWarnings("unchecked")
    public static synchronized  <T> ObjectContainer<T> setObjectBusy(Class<T> tClass, Object groupingBy) {
        checkNotNull(groupingBy);
        List<ObjectContainer<?>> freeObjects =  getAllObjects(tClass, objectContainer ->
                !objectContainer.isBusy() && tClass.isAssignableFrom(objectContainer.getWrappedObject().getClass())
                        && groupingBy.equals(objectContainer.getGroupBy()));
        if (freeObjects.size() == 0) {
            return null;
        }
        ObjectContainer<?> result = freeObjects.get(0);
        result.setBusy(currentThread());
        return (ObjectContainer<T>) result;
    }

    public static synchronized void remove(List<ObjectContainer<?>> objectContainers) {
        checkNotNull(objectContainers);
        synchronized (containers) {
            containers.removeAll(objectContainers);
        }
    }

    private synchronized boolean isBusy() {
        return busyBy != null;
    }

    public synchronized void setBusy(Thread thread) {
        this.busyBy = thread;
        new ThreadStateLoop(currentThread(), this).start();
    }

    synchronized void setFree() {
        this.busyBy = null;
    }

    public T getWrappedObject() {
        return t;
    }

    public void groupBy(Object groupBy) {
        this.groupBy = groupBy;
    }

    private Object getGroupBy() {
        return groupBy;
    }
}
