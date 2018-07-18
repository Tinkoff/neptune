package ru.tinkoff.qa.neptune.core.api;

import static java.util.Optional.ofNullable;

public final class AsIsCondition<T> implements Condition<T> {

    public static final AsIsCondition<Object> AS_IS = new AsIsCondition<>();

    private AsIsCondition() {
        super();
    }

    @Override
    public String toString() {
        return "as is";
    }

    @Override
    public boolean test(T t) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return ofNullable(obj).map(o -> AsIsCondition.class.isAssignableFrom(o.getClass()))
                .orElse(false);
    }
}
