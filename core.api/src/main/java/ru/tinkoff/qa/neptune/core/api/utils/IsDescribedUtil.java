package ru.tinkoff.qa.neptune.core.api.utils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class IsDescribedUtil {

    private IsDescribedUtil() {
        super();
    }

    private static boolean isDescribed(Object toBeDescribed) {
        if (toBeDescribed == null) {
            return false;
        }

        String stringDescription = valueOf(toBeDescribed);
        return !isBlank(stringDescription) && !valueOf(toBeDescribed).equals(format("%s@%s", toBeDescribed.getClass().getName(),
                toHexString(toBeDescribed.hashCode())));
    }

    /**
     * Check is {@link Function} described or not. It means that toString() returns a value that differs from the
     * result of {@link Object#toString()}.
     *
     * @param toBeDescribed is a function which is supposed to be described.
     * @return {@code true} if the function is not null-value and toString() returns a value that differs from blank
     * string and from the result of {@link Object#toString()}. {@code false} is returned otherwise.
     */
    public static boolean isDescribed(Function<?, ?> toBeDescribed) {
        return isDescribed((Object) toBeDescribed);
    }

    /**
     * Check is {@link Consumer} described or not. It means that toString() returns a value that differs from the result
     * of {@link Object#toString()}.
     *
     * @param toBeDescribed is a function which is supposed to be described.
     * @return {@code true} if the function is not null-value and toString() returns a value that differs from blank
     * string and from the result of {@link Object#toString()}. {@code false} is returned otherwise.
     */
    public static boolean isDescribed(Consumer<?> toBeDescribed) {
        return isDescribed((Object) toBeDescribed);
    }

    /**
     * Check is {@link Predicate} described or not. It means that toString() returns a value that differs from the result
     * of {@link Object#toString()}.
     *
     * @param toBeDescribed is a function which is supposed to be described.
     * @return {@code true} if the function is not null-value and toString() returns a value that differs from blank
     * string and from the result of {@link Object#toString()}. {@code false} is returned otherwise.
     */
    public static boolean isDescribed(Predicate<?> toBeDescribed) {
        return isDescribed((Object) toBeDescribed);
    }
}
