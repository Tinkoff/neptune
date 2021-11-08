package ru.tinkoff.qa.neptune.core.api.steps.conditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import static com.google.common.collect.Lists.newArrayList;

final class ImmutableArrayList<T> extends ArrayList<T> {

    private final String toStringValue;

    ImmutableArrayList(Iterable<T> iterable) {
        super(newArrayList(iterable));
        toStringValue = iterable.toString();
    }

    private static UnsupportedOperationException uoe() {
        return new UnsupportedOperationException("Removal and editing operations are not supported. Sorry");
    }

    @Override
    public boolean add(T t) {
        throw uoe();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw uoe();
    }

    @Override
    public void clear() {
        throw uoe();
    }

    @Override
    public boolean remove(Object o) {
        throw uoe();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw uoe();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        throw uoe();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw uoe();
    }
}
