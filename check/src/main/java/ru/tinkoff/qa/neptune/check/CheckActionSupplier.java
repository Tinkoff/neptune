package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.core.api.steps.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.stream;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.createStep;

@SequentialActionSupplier.DefinePerformImperativeParameterName("Check")
public final class CheckActionSupplier<R, T> extends SequentialActionSupplier<R, T, CheckActionSupplier<R, T>> {

    private static final String LINE_SEPARATOR = "\r\n";

    private final List<AssertionError> caughtMismatches = new ArrayList<>();
    private final List<StepAction<T>> checkList = new ArrayList<>();

    private CheckActionSupplier() {
        super();
    }

    /**
     * Creates an instance of {@link CheckActionSupplier};
     * Defines a value to be verified and verification criteria;
     * Value check is performed.
     *
     * @param description  Value description
     * @param t            value to be verified.
     * @param matchActions is an array of {@link MatchAction}
     * @param <T>          is a type of a value to be verified.
     */
    @SafeVarargs
    public static <T> void check(String description, T t, MatchAction<T, ?>... matchActions) {
        checkActionSupplier(description, (Function<T, T>) t1 -> t1, matchActions)
                .get()
                .accept(t);
    }

    @SafeVarargs
    @Description("{description}")
    static <T, R> CheckActionSupplier<R, T> checkActionSupplier(
            @DescriptionFragment(value = "description",
                    makeReadableBy = DescriptionTranslator.class) String description,
            Function<R, T> f,
            MatchAction<T, ?>... matchActions) {
        checkArgument(!isBlank(description), "Value description to be inspected should not be blank");
        return new CheckActionSupplier<R, T>()
                .performOn(f)
                .matches(matchActions);
    }

    /**
     * Creates an instance of {@link CheckActionSupplier};
     * Evaluates value to be checked;
     * Value check is performed.
     *
     * @param description  description of a value to get and then check it
     * @param toGet        is how to get a value
     * @param matchActions is an array of {@link MatchAction}
     * @param <T>          is a type of a value to be verified.
     */
    @SafeVarargs
    public static <T> void evaluateAndCheck(String description,
                                            Supplier<T> toGet,
                                            MatchAction<T, ?>... matchActions) {
        checkActionSupplier(description, new Function<Step<T>, T>() {
            @Override
            public T apply(Step<T> tStep) {
                return tStep.perform();
            }

            public String toString() {
                return description;
            }
        }, matchActions).get().accept(createStep(description, toGet));
    }

    /**
     * Creates an instance of {@link CheckActionSupplier};
     * Defines a value to be verified and verification criteria;
     * Value check is performed.
     *
     * @param t            value to be verified.
     * @param matchActions is an array of {@link MatchAction}
     * @param <T>          is a type to be verified.
     */
    @SafeVarargs
    public static <T> void check(T t, MatchAction<T, ?>... matchActions) {
        checkActionSupplier(t, matchActions)
                .get().accept(t);
    }

    @SafeVarargs
    @Description("inspected value {t}")
    @SuppressWarnings("unused")
    static <T> CheckActionSupplier<T, ?> checkActionSupplier(@DescriptionFragment("t") T t,
                                                             MatchAction<T, ?>... matchActions) {
        return new CheckActionSupplier<T, T>()
                .performOn(o -> o)
                .matches(matchActions);
    }

    @SafeVarargs
    private CheckActionSupplier<R, T> matches(MatchAction<T, ?>... matchActions) {
        checkArgument(nonNull(matchActions), "Criteria to check value should not be a null value");
        checkArgument(matchActions.length > 0, "At least one criteria to check value should be defined");
        checkList.addAll(stream(matchActions).map(MatchAction::get).collect(toList()));
        return this;
    }

    @Override
    protected void performActionOn(T value) {
        checkList.forEach(tConsumer -> {
            try {
                tConsumer.accept(value);
            } catch (AssertionError e) {
                caughtMismatches.add(e);
            }
        });

        AssertionError assertionError = null;
        if (caughtMismatches.size() > 0) {
            var sb = "List of mismatches:" + LINE_SEPARATOR + caughtMismatches.stream().map(Throwable::getMessage)
                    .collect(joining(";" + LINE_SEPARATOR + LINE_SEPARATOR));
            assertionError = new AssertionError(sb);
        }

        ofNullable(assertionError).ifPresent(assertionError1 -> {
            throw assertionError1;
        });
    }
}
