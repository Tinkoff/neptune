package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.StepAction;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakesCapturesOnFinishing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.action;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.MatcherAssert.assertThat;

public final class ThatValue<T> extends SequentialActionSupplier<CheckStepPerformer, T, ThatValue<T>> {

    private final List<AssertionError> caughtMismatches = new ArrayList<>();
    private final List<Consumer<T>> checkList = new ArrayList<>();
    private final String description;

    private ThatValue(String description) {
        super(format("Verify %s", description));
        this.description = description;
    }

    /**
     * Creates an instance of {@link ThatValue} and defines value to be verified.
     *
     * @param description of a value to be verified.
     * @param t           value to be verified.
     * @param <T>         is a type of a value to be verified.
     * @return an instance of {@link ThatValue} with defined value to be verified.
     */
    public static <T> ThatValue<T> thatValue(String description, T t) {
        checkArgument(!isBlank(description), "Description of a value to be inspected should not be null");
        return new ThatValue<T>(description).performOn(t);
    }

    /**
     * Creates an instance of {@link ThatValue} and defines value to be verified.
     *
     * @param t   value to be verified.
     * @param <T> is a type to be verified.
     * @return an instance of {@link ThatValue} with defined value to be verified.
     */
    public static <T> ThatValue<T> thatValue(T t) {
        return new ThatValue<T>(format("inspected value %s", t)).performOn(t);
    }

    private <S extends MakesCapturesOnFinishing<S>> S makeCaptureOmFinishing(S s) {
        return s.onFinishMakeCaptureOfType(Object.class);
    }

    /**
     * Adds more criteria to check the value.
     *
     * @param description of a function
     * @param function    value gets some value from an object which is needed to be matched
     * @param criteria    matcher to be added
     * @param <R>         the type of a value that should be returned by function and matched by criteria.
     * @return self-reference
     */
    @SuppressWarnings("unchecked")
    public <R> ThatValue<T> suitsCriteria(String description,
                                          Function<T, R> function,
                                          Matcher<? super R> criteria) {
        checkArgument(!isBlank(description), "Description of a value to be inspected should not be null");
        checkArgument(nonNull(criteria), "Criteria matcher should not be null");
        checkArgument(nonNull(function), "Function to get value to check should not be null");
        var describedFunction = makeCaptureOmFinishing(toGet(description, function));

        StepAction<T> action = makeCaptureOmFinishing(action(format("Check %s. Assert: %s", description, criteria),
                t -> {
                    var toBeChecked = describedFunction.apply(t); //for logging
                    makeCaptureOmFinishing(action(format("Assert %s", criteria),
                            r -> assertThat((R) r, new InnerMatcher<>(description, criteria))))

                            .accept(toBeChecked); //for logging
                }));

        checkList.add(action);
        return this;
    }

    /**
     * Adds more criteria to check value.
     *
     * @param criteria matcher to be added
     * @return self-reference
     */
    public ThatValue<T> suitsCriteria(Matcher<? super T> criteria) {
        checkArgument(nonNull(criteria), "Criteria matcher should not be null");
        StepAction<T> action = makeCaptureOmFinishing(action(format("Assert %s", criteria),
                t -> assertThat(t, new InnerMatcher<>(description, criteria))));
        checkList.add(action);
        return this;
    }

    @Override
    protected void performActionOn(T value) {
        checkArgument(checkList.size() > 0, "At least one assertion should be defined there");
        checkList.forEach(tConsumer -> {
            try {
                tConsumer.accept(value);
            } catch (AssertionError e) {
                caughtMismatches.add(e);
            }
        });

        AssertionError assertionError = null;
        if (caughtMismatches.size() > 0) {
            var sb = "List of mismatches:\n\t" + caughtMismatches.stream().map(Throwable::getMessage)
                    .collect(joining(";\n\t\n\t"));
            assertionError = new AssertionError(sb);
        }
        caughtMismatches.clear();
        ofNullable(assertionError).ifPresent(assertionError1 -> {
            throw assertionError1;
        });
    }

    private static class InnerMatcher<T> extends BaseMatcher<T> {

        private final String description;
        private final Matcher<? super T> criteria;

        private InnerMatcher(String description, Matcher<? super T> criteria) {
            this.description = description;
            this.criteria = criteria;
        }

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
    }
}
