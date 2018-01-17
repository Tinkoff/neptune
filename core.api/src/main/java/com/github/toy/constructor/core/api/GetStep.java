package com.github.toy.constructor.core.api;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

public interface GetStep<THIS extends GetStep<THIS>> {

    @ToBeReported(constantMessagePart = "Get:")
    default  <T> T get(Function<THIS, T> function) {
        checkArgument(function != null,
                "The function which returns the goal value is not defined");
        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "The function which returns the goal value should be described " +
                        "by the StoryWriter.toGet method.");
        return log(function.apply((THIS) this));
    }

    default  <T> T get(Supplier<Function<THIS, T>> functionSupplier) {
        checkNotNull(functionSupplier, "Supplier of the value to get was not defined");
        return get(functionSupplier.get());
    }

    @ToBeReported(constantMessagePart = "Returned value:")
    default  <T> T log(T value) {
        return value;
    }
}
