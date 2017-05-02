package com.github.toy.constructor.api;

import com.sun.istack.internal.NotNull;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

public interface GetStep<THIS extends GetStep<THIS>> {
    default  <T> T get(@NotNull String description, @NotNull Function<THIS, T> function) {
        checkNotNull(description);
        checkNotNull(function);
        return function.apply((THIS) this);
    }
}
