package com.github.toy.constructor.core.api;

import static java.util.Optional.ofNullable;

public final class AsIsPredicate<T> implements DescribedPredicate<T> {

    public static final AsIsPredicate<Object> AS_IS = new AsIsPredicate<>();

    private AsIsPredicate() {
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
        return ofNullable(obj).map(o -> AsIsPredicate.class.isAssignableFrom(o.getClass()))
                .orElse(false);
    }
}
