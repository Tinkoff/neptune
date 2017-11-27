package com.github.toy.constructor.core.api;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface GetStep<THIS extends GetStep<THIS>> {

    default  <T> T get(String description, Function<THIS, T> function) {
        checkArgument(isNotBlank(description), "Description should not be empty");
        checkNotNull(function);
        return function.apply((THIS) this);
    }
}
