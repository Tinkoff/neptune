package ru.tinkoff.qa.neptune.core.api;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

@SuppressWarnings("unchecked")
public interface GetStep<THIS extends GetStep<THIS>> {

    default <T> T get(Function<THIS, T> function) {
        checkArgument(nonNull(function), "The function is not defined");
        return function.apply((THIS) this);
    }

    default <T> T get(Supplier<Function<THIS, T>> functionSupplier) {
        checkNotNull(functionSupplier, "Supplier of the value to get was not defined");
        return get(functionSupplier.get());
    }

    default <T> T get(String description, Function<THIS, T> function) {
        checkArgument(!isBlank(description), "Description should not be null or an empty string");
        return get(toGet(description, function));
    }
}
