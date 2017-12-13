package com.github.toy.constructor.core.api;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface GetStep<THIS extends GetStep<THIS>> {

    default  <T> T get(String description, Function<THIS, T> function) {
        checkArgument(isNotBlank(description), "Description should not be empty");
        checkNotNull(function);
        return log(function.apply((THIS) this));
    }

    default  <T> T get(Function<THIS, T> function) {
        return ofNullable(function).map(thistFunction -> get(function.toString(), function))
                .orElseThrow(() -> new IllegalArgumentException("Function which returns desired value was not defined"));
    }

    private <T> T log(T value) {
        return value;
    }
}
