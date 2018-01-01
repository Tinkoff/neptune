package com.github.toy.constructor.core.api;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface GetStep<THIS extends GetStep<THIS>> {

    private <T> T get(String description, Function<THIS, T> function) {
        checkArgument(DescribedFunction.class.isAssignableFrom(function.getClass()),
                "Action should be described by the StoryWriter.toGet method.");
        checkArgument(isNotBlank(description), "Description should not be empty");
        return log(function.apply((THIS) this));
    }

    default  <T> T get(Function<THIS, T> function) {
        return ofNullable(function).map(thistFunction -> get(function.toString(), function))
                .orElseThrow(() -> new IllegalArgumentException("Function which returns desired value was not defined"));
    }

    default  <T> T get(Supplier<Function<THIS, T>> functionSupplier) {
        checkNotNull(functionSupplier, "Supplier of the value to get was not defined");
        return get(functionSupplier.get());
    }

    private <T> T log(T value) {
        return value;
    }
}
