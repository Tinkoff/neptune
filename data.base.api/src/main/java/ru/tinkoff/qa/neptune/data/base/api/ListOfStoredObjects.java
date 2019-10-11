package ru.tinkoff.qa.neptune.data.base.api;

import ru.tinkoff.qa.neptune.core.api.steps.LoggableObject;

import java.util.*;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isPersistent;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Deprecated
public class ListOfStoredObjects<T> implements LoggableObject, List<T> {

    public static final Function<List<? extends PersistableObject>, String> INFO_PERSISTABLE_INFO = persistableObjects -> {
        var result = persistableObjects
                .stream()
                .map(persistableObject -> {
                    if (isNull(persistableObject)) {
                        return null;
                    }
                    if (isPersistent(persistableObject)) {
                        return format("from table %s", persistableObject.fromTable());
                    }
                    return format("not stored mapped by %s", persistableObject.fromTable());
                })
                .filter(Objects::nonNull)
                .distinct().collect(toList());

        if (result.size() > 0) {
            return String.join(",", result);
        } else {
            return EMPTY;
        }
    };


    private final String description;
    private final List<T> innerList;
    private final Function<List<T>, String> functionToCreateDetailsToDescription;

    protected ListOfStoredObjects(String description) {
        this(description, new ArrayList<>(), null);
    }

    protected ListOfStoredObjects(String description, Collection<T> toAdd, Function<List<T>, String> functionToCreateDescription) {
        this.description = description;
        innerList = new ArrayList<>(toAdd);
        this.functionToCreateDetailsToDescription = functionToCreateDescription;
    }

    protected ListOfStoredObjects(String description, Collection<T> toAdd) {
        this(description, toAdd, null);
    }

    private static UnsupportedOperationException unsupportedOperationException() {
        return new UnsupportedOperationException(format("Operation is not supported by %s and subclasses",
                ListOfStoredObjects.class.getName()));
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return innerList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return innerList.iterator();
    }

    @Override
    public Object[] toArray() {
        return innerList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return innerList.toArray(a);
    }

    public final boolean add(T toAdd) {
        throw unsupportedOperationException();
    }

    @Override
    public final boolean remove(Object o) {
        return innerList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return innerList.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends T> c) {
        throw unsupportedOperationException();
    }

    @Override
    public final boolean addAll(int index, Collection<? extends T> c) {
        throw unsupportedOperationException();
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return innerList.removeAll(c);
    }

    @Override
    public final boolean retainAll(Collection<?> c) {
        throw unsupportedOperationException();
    }

    @Override
    public final void clear() {
        throw unsupportedOperationException();
    }

    @Override
    public T get(int index) {
        return innerList.get(index);
    }

    @Override
    public final T set(int index, T element) {
        throw unsupportedOperationException();
    }

    public final void add(int index, T element) {
        throw unsupportedOperationException();
    }

    @Override
    public final T remove(int index) {
        return innerList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return innerList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return innerList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return innerList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return innerList.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new ListOfStoredObjects<>(description, innerList.subList(fromIndex, toIndex),
                this.functionToCreateDetailsToDescription);
    }

    public String toString() {
        String additionalDetails =
                ofNullable(functionToCreateDetailsToDescription)
                        .map(listStringFunction -> listStringFunction.apply(innerList))
                        .orElse(null);
        if (!isBlank(additionalDetails)) {
            return format("%s, size %s, %s", description, size(), additionalDetails);
        }
        return format("%s, size %s", description, size());
    }
}
