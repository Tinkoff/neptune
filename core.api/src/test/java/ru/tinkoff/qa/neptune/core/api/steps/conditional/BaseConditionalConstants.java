package ru.tinkoff.qa.neptune.core.api.steps.conditional;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;

abstract class BaseConditionalConstants {
    static Duration ONE_SECOND = ofSeconds(1);
    static Duration ONE_HUNDRED_MILLIS = ofMillis(100);
    static Duration ONE_MILLISECOND = ofMillis(1);
    static final String A_UPPER = "A";
    static final String A_LOWER = "a";
    static final String ONE_NUM = "1";

    static final Predicate<String> VALUE_A = A_UPPER::equalsIgnoreCase;
    static final Predicate<String> VALUE_W = "W"::equalsIgnoreCase;
    static final Predicate<String> VALUE_ONE_NUMBER = ONE_NUM::equalsIgnoreCase;
    static final Predicate<String> MALFORMED_PREDICATE = s -> {
        throw new RuntimeException("Exception for the unit testing!");
    };

    static final List<String> LITERAL_LIST = of(ONE_NUM, A_LOWER, A_UPPER, "C", "c", "D");
    static final List<String> EMPTY_LIST = List.of();

    final Function<List<String>, Set<String>> CONVERT_LIST_TO_SET = HashSet::new;
    final Function<List<String>, String[]> CONVERT_LIST_TO_ARRAY = strings -> strings.toArray(new String[]{});
    final Function<List<String>, String> GET_FIRST_OBJECT_FROM_LIST = strings -> strings.get(0);

    static <T, R> Function<T, R> createMalformedFunction() {
        return t -> {
            throw new IllegalStateException("Test exception");
        };
    }

}
