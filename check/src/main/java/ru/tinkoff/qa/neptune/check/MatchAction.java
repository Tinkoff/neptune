package ru.tinkoff.qa.neptune.check;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.any.AnyMatcher.anyOne;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.only.one.OnlyOneMatcher.onlyOne;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * This class is designed to perform the matching of values.
 *
 * @param <T> is a type of an object to be checked. Also this may be some object that is used to evaluate a
 *            value to be checked
 * @param <R> is a generic type of {@link Matcher}
 */
@SequentialActionSupplier.DefinePerformImperativeParameterName("Assert:")
@MaxDepthOfReporting(1)
public class MatchAction<T, R> extends SequentialActionSupplier<T, R, MatchAction<T, R>> {

    private final String assertDescription;
    private final Matcher<? super R> criteria;

    MatchAction(String assertDescription, Matcher<? super R> criteria) {
        super();
        checkArgument(!Objects.equals(EMPTY, valueOf(assertDescription).trim()),
                "Description shouldn't be an empty string");
        this.assertDescription = assertDescription;
        this.criteria = criteria;
    }

    @Description("{matcher}")
    private static <T> MatchAction<T, T> matchPrivate(@DescriptionFragment("matcher") Matcher<? super T> matcher) {
        return new MatchAction<T, T>(null, matcher)
                .performOn(t -> t);
    }

    @Description("{description} {matcher}")
    private static <T, R> MatchAction<T, R> matchPrivate(
            @DescriptionFragment(
                    value = "description",
                    makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class) String description,
            Function<T, R> eval,
            @DescriptionFragment("matcher") Matcher<? super R> matcher) {
        var translated = translate(description);
        return new MatchAction<T, R>(translated, matcher)
                .performOn(new CalculateGetSupplier<>(eval).setDescription(translated));
    }


    /**
     * Creates an instance that performs the matching of an object.
     *
     * @param matcher is a criteria matcher
     * @param <T>     is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    public static <T> MatchAction<T, T> match(Matcher<? super T> matcher) {
        return matchPrivate(all(matcher));
    }

    /**
     * Creates an instance that performs the matching of an object.
     *
     * @param duration is time to wait for positive result of the matching. It is recommended to use it when some
     *                 mutable property or value is checked.
     * @param matcher  is a criteria matcher
     * @param <T>      is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    public static <T> MatchAction<T, T> match(Duration duration, Matcher<? super T> matcher) {
        return matchPrivate(new MatcherWithTime<>(duration, all(matcher)));
    }

    /**
     * Creates an instance that performs the matching of a value which is evaluated from checked object
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param matcher     is a criteria matcher
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    public static <T, R> MatchAction<T, R> match(String description, Function<T, R> eval, Matcher<? super R> matcher) {
        return matchPrivate(description, eval, all(matcher));
    }

    /**
     * Creates an instance that performs the matching of a value which is evaluated from checked object
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param duration    is time to wait for positive result of the matching. It is recommended to use it when some
     *                    mutable property or value is checked.
     * @param matcher     is a criteria matcher
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    public static <T, R> MatchAction<T, R> match(String description,
                                                 Function<T, R> eval,
                                                 Duration duration,
                                                 Matcher<? super R> matcher) {
        return matchPrivate(description, eval, new MatcherWithTime<>(duration, all(matcher)));
    }


    /**
     * Creates an instance that performs the matching of an object with inverted criteria.
     *
     * @param matcher is a criteria to be inverted
     * @param <T>     is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    public static <T> MatchAction<T, T> matchNot(Matcher<? super T> matcher) {
        return matchPrivate(notOf(matcher));
    }

    /**
     * Creates an instance that performs the matching of an object with inverted criteria.
     *
     * @param duration is time to wait for positive result of the matching. It is recommended to use it when some
     *                 mutable property or value is checked.
     * @param matcher  is a criteria to be inverted
     * @param <T>      is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    public static <T> MatchAction<T, T> matchNot(Duration duration, Matcher<? super T> matcher) {
        return matchPrivate(new MatcherWithTime<>(duration, notOf(matcher)));
    }

    /**
     * Creates an instance that performs the matching of a value which is evaluated from checked object.
     * The checking uses inverted criteria.
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param matcher     is a criteria to be inverted
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    public static <T, R> MatchAction<T, R> matchNot(String description, Function<T, R> eval, Matcher<? super R> matcher) {
        return matchPrivate(description, eval, notOf(matcher));
    }

    /**
     * Creates an instance that performs the matching of a value which is evaluated from checked object.
     * The checking uses inverted criteria.
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param duration    is time to wait for positive result of the matching. It is recommended to use it when some
     *                    mutable property or value is checked.
     * @param matcher     is a criteria to be inverted
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    public static <T, R> MatchAction<T, R> matchNot(String description,
                                                    Function<T, R> eval,
                                                    Duration duration,
                                                    Matcher<? super R> matcher) {
        return matchPrivate(description, eval, new MatcherWithTime<>(duration, notOf(matcher)));
    }


    /**
     * Creates an instance that performs the matching of an object with OR-criteria.
     *
     * @param matchers are criteria of the OR-checking
     * @param <T>      is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    @SafeVarargs
    public static <T> MatchAction<T, T> matchAny(Matcher<? super T>... matchers) {
        return matchPrivate(anyOne(matchers));
    }

    /**
     * Creates an instance that performs the matching of an object with OR-criteria.
     *
     * @param duration is time to wait for positive result of the matching. It is recommended to use it when some
     *                 mutable property or value is checked.
     * @param matchers are criteria of the OR-checking
     * @param <T>      is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    @SafeVarargs
    public static <T> MatchAction<T, T> matchAny(Duration duration, Matcher<? super T>... matchers) {
        return matchPrivate(new MatcherWithTime<>(duration, anyOne(matchers)));
    }

    /**
     * Creates an instance that performs the matching of a value which is evaluated from checked object.
     * The checking uses OR-criteria.
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param matchers    are criteria of the OR-checking
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    @SafeVarargs
    public static <T, R> MatchAction<T, R> matchAny(String description, Function<T, R> eval, Matcher<? super R>... matchers) {
        return matchPrivate(description, eval, anyOne(matchers));
    }

    /**
     * Creates an instance that performs the matching of a value which is evaluated from checked object.
     * The checking uses OR-criteria.
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param duration    is time to wait for positive result of the matching. It is recommended to use it when some
     *                    mutable property or value is checked.
     * @param matchers    are criteria of the OR-checking
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    @SafeVarargs
    public static <T, R> MatchAction<T, R> matchAny(String description,
                                                    Function<T, R> eval,
                                                    Duration duration,
                                                    Matcher<? super R>... matchers) {
        return matchPrivate(description, eval, new MatcherWithTime<>(duration, anyOne(matchers)));
    }

    /**
     * Creates an instance that performs the matching of an object with XOR-criteria (only one should match).
     *
     * @param matchers are criteria of the XOR-checking
     * @param <T>      is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    @SafeVarargs
    public static <T> MatchAction<T, T> matchOnlyOne(Matcher<? super T>... matchers) {
        return matchPrivate(onlyOne(matchers));
    }

    /**
     * Creates an instance that performs the matching of an object with XOR-criteria (only one should match).
     *
     * @param duration is time to wait for positive result of the matching. It is recommended to use it when some
     *                 mutable property or value is checked.
     * @param matchers are criteria of the XOR-checking
     * @param <T>      is a type of a value to be checked
     * @return a new {@link MatchAction}
     */
    @SafeVarargs
    public static <T> MatchAction<T, T> matchOnlyOne(Duration duration, Matcher<? super T>... matchers) {
        return matchPrivate(new MatcherWithTime<>(duration, onlyOne(matchers)));
    }

    /**
     * Creates an instance that performs the matching of a value which is evaluated from checked object.
     * The checking uses XOR-criteria (only one should match).
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param matchers    are criteria of the XOR-checking
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    @SafeVarargs
    public static <T, R> MatchAction<T, R> matchOnlyOne(String description, Function<T, R> eval, Matcher<? super R>... matchers) {
        return matchPrivate(description, eval, onlyOne(matchers));
    }

    /**
     * Creates an instance that performs the matching of a value which is evaluated from checked object.
     * The checking uses XOR-criteria (only one should match).
     *
     * @param description of evaluated value
     * @param eval        a function that performs evaluation
     * @param duration    is time to wait for positive result of the matching. It is recommended to use it when some
     *                    mutable property or value is checked.
     * @param matchers    are criteria of the XOR-checking
     * @param <T>         is a type of a value to be checked
     * @param <R>         is a type of a value to be evaluated
     * @return a new {@link MatchAction}
     */
    @SafeVarargs
    public static <T, R> MatchAction<T, R> matchOnlyOne(String description,
                                                        Function<T, R> eval,
                                                        Duration duration,
                                                        Matcher<? super R>... matchers) {
        return matchPrivate(description, eval, new MatcherWithTime<>(duration, onlyOne(matchers)));
    }

    @Override
    protected void howToPerform(R value) {
        var matches = criteria.matches(value);

        if (!matches) {
            var mismatchDescription = new StringDescription();
            criteria.describeMismatch(value, mismatchDescription);
            throw new AssertionError(new AssertMismatchDescriber(ofNullable(assertDescription)
                    .map(s -> " '" + s + "'")
                    .orElse(EMPTY),
                    criteria.toString(),
                    valueOf(value),
                    mismatchDescription.toString()).toString());
        }
    }
}
