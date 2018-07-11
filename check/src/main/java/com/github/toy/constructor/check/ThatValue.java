package com.github.toy.constructor.check;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.MatcherAssert.assertThat;

public final class ThatValue<T> extends SequentialActionSupplier<Check, Object, ThatValue<T>> {

    private final T inspected;
    private final String description;
    private final List<AssertionError> caughtMismatches = new ArrayList<>();

    private ThatValue(String description, T inspected) {
        checkArgument(!isBlank(description),
                "Description of a value to be inspected should not be null");
        this.inspected = inspected;
        this.description = description;
    }

    /**
     * Creates an instance of {@link ThatValue} and defines value to be verified.
     *
     * @param description of a value to be verified.
     * @param t value to be verified.
     * @param <T> is a type to be verified.
     * @return an instance of {@link ThatValue} with defined value to be verified.
     */
    public static <T> ThatValue<T> thatValue(String description, T t) {
        return new ThatValue<>(description, t);
    }

    /**
     * Creates an instance of {@link ThatValue} and defines value to be verified.
     *
     * @param t value to be verified.
     * @param <T> is a type to be verified.
     * @return an instance of {@link ThatValue} with defined value to be verified.
     */
    public static <T> ThatValue<T> thatValue(T t) {
        return new ThatValue<>("inspected value", t);
    }

    /**
     * Adds more criteria to check the value.
     *
     * @param function value gets some value from an object which is needed to be matched
     * @param criteria matcher to be added
     * @param <R> the type of a value that should be returned by function and matched by criteria.
     * @return self-reference
     */
    public <R> ThatValue<T> suitsCriteria(String description,
                                          Function<T, R> function, Matcher<? super R> criteria) {
        return andThen("Assertion", toGet(description, check -> function.apply(inspected)), new BaseMatcher<T>() {
            @Override
            public void describeTo(Description description) {
                criteria.describeTo(description);
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {
                mismatchDescription.appendText(format("%s: \n\t", description));
                criteria.describeMismatch(item, mismatchDescription);
            }

            @Override
            public boolean matches(Object item) {
                return criteria.matches(item);
            }
        });
    }

    /**
     * Adds more criteria to check value.
     *
     * @param criteria matcher to be added
     * @return self-reference
     */
    public ThatValue<T> suitsCriteria(Matcher<? super T> criteria) {
        return andThen("Assertion", description, inspected, new BaseMatcher<T>() {
            @Override
            public void describeTo(Description description) {
                criteria.describeTo(description);
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {
                mismatchDescription.appendText(format("%s: \n\t", description));
                criteria.describeMismatch(item, mismatchDescription);
            }

            @Override
            public boolean matches(Object item) {
                return criteria.matches(item);
            }
        });
    }

    @Override
    public Consumer<Check> get() {
        return ofNullable(super.get())
                .map(checkConsumer -> action(format("Verify %s", description), (Consumer<Check>) check -> {
                    try {
                        checkConsumer.accept(check);
                    }
                    catch (Throwable t) {
                        caughtMismatches.clear();
                        throw t;
                    }
                    finally {
                        AssertionError assertionError = null;
                        if (caughtMismatches.size() > 0) {
                            String sb = "List of mismatches:\n\t" + String.join(";\n\t\n\t",
                                    caughtMismatches.stream().map(Throwable::getMessage)
                                            .collect(toList()));
                            assertionError = new AssertionError(sb);
                        }
                        caughtMismatches.clear();
                        ofNullable(assertionError).ifPresent(assertionError1 -> {throw  assertionError1;});
                    }
                }))
                .orElseThrow(() -> new IllegalArgumentException("At least one assertion should be defined there"));
    }

    @Override
    protected Consumer<Check> getActionSequence(Consumer<Check> action) {
        return ofNullable(super.get()).map(checkConsumer -> checkConsumer.andThen(check -> {
            try {
                action.accept(check);
            }
            catch (AssertionError e) {
                caughtMismatches.add(e);
            }
        })).orElseGet(() -> check -> {
            try {
                action.accept(check);
            }
            catch (AssertionError e) {
                caughtMismatches.add(e);
            }
        });


    }

    @Override
    @SuppressWarnings("unchecked")
    protected void performActionOn(Object value, Object... parameters) {
        Matcher matcher = Matcher.class.cast(parameters[0]);
        Consumer<Object> assertStep = action("Attempt to verify", o -> assertThat(o, matcher));
        assertStep.accept(value);
    }
}
