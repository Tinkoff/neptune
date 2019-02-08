package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakesCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.exception.management.IgnoresThrowable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.List.copyOf;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Condition.NOT_DESCRIBED;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromArray.getFromArray;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromIterable.getFromIterable;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubArray.getArray;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubIterable.getIterable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

/**
 * This class is designed to build and supply chained functions to get desired value.
 * There are protected methods to be overridden/re-used as public when it is necessary.
 *
 * @param <T>    is a type of an input value
 * @param <R>    is a type of a returned value
 * @param <M>    is a type of a mediator value is used to get the required result
 * @param <P>    is a type of a value checked by a {@link Predicate}.
 * @param <THIS> this is the self-type. It is used for the method chaining.
 */
@SuppressWarnings("unchecked")
public abstract class SequentialGetStepSupplier<T, R, M, P, THIS extends SequentialGetStepSupplier<T, R, M, P, THIS>> implements Cloneable,
        Supplier<Function<T, R>>, IgnoresThrowable<THIS>, MakesCapturesOnFinishing<THIS> {

    private final String description;
    final Set<Class<? extends Throwable>> ignored = new HashSet<>();
    private final List<CaptorFilterByProducedType> captorFilters = new ArrayList<>();

    Predicate<P> condition;
    private Object from;
    Duration timeToGet;
    Duration sleepingTime;
    Supplier<? extends RuntimeException> exceptionSupplier;

    protected SequentialGetStepSupplier(String description) {
        this.description = description;
        MakesCapturesOnFinishing.makeCaptureSettings(this);
    }

    /**
     * Sometimes it is necessary to get a value that suits some criteria. This method defines the criteria to filter
     * values received after invocation of {@link Function#apply(Object)} on a resulted function.
     *
     * @param concat    AND, OR, XOR to build a logical expression. It concatenates defined {@link Predicate}
     *                  with a value set ut by the previous invocation of this method or {@link #criteria(ConditionConcatenation, String, Predicate)},
     *                  {@link #criteria(Predicate)}, {@link #criteria(String, Predicate)}. When there is no value has been set
     *                  up then this parameter is ignored and the method just sets up defined {@link Predicate}.
     * @param condition a condition to get desired value
     * @return self-reference
     */
    protected THIS criteria(ConditionConcatenation concat, Predicate<? super P> condition) {
        checkNotNull(concat);
        this.condition = ofNullable(this.condition)
                .map(pPredicate -> concat.concat(pPredicate, condition))
                .orElse((Predicate<P>) condition);
        return (THIS) this;
    }

    /**
     * Sometimes it is necessary to get a value that suits some criteria. This method defines the criteria to filter
     * values received after invocation of {@link Function#apply(Object)} on a resulted function.
     *
     * @param concat               AND, OR, XOR to build a logical expression. It concatenates defined {@link Predicate}
     *                             with a value set ut by the previous invocation of this method or {@link #criteria(ConditionConcatenation, Predicate)},
     *                             {@link #criteria(Predicate)}, {@link #criteria(String, Predicate)}. When there is no value has been set
     *                             up then this parameter is ignored and the method just sets up defined {@link Predicate}.
     * @param conditionDescription is a description of a condition to get desired value
     * @param condition            a condition to get desired value
     * @return self-reference
     */
    protected THIS criteria(ConditionConcatenation concat, String conditionDescription, Predicate<? super P> condition) {
        return criteria(concat, StoryWriter.condition(conditionDescription, condition));
    }

    /**
     * Sometimes it is necessary to get a value that suits some criteria. This method defines the criteria to filter
     * values received after invocation of {@link Function#apply(Object)} on a resulted function. It builds AND-expression
     * by default.
     *
     * @param condition is a condition to get desired value
     * @return self-reference
     * @see #criteria(ConditionConcatenation, Predicate)
     * @see #criteria(ConditionConcatenation, String, Predicate)
     */
    protected THIS criteria(Predicate<? super P> condition) {
        return criteria(ConditionConcatenation.AND, condition);
    }

    /**
     * Sometimes it is necessary to get a value that suits some criteria. This method defines the criteria to filter
     * values received after invocation of {@link Function#apply(Object)} on a resulted function. It builds AND-expression
     * by default.
     *
     * @param conditionDescription is a description of a condition to get desired value
     * @param condition            a condition to get desired value
     * @return self-reference
     * @see #criteria(ConditionConcatenation, Predicate)
     * @see #criteria(ConditionConcatenation, String, Predicate)
     */
    protected THIS criteria(String conditionDescription, Predicate<? super P> condition) {
        return criteria(ConditionConcatenation.AND, conditionDescription, condition);
    }

    /**
     * Sometimes it is necessary to wait until some result that may be considered valuable is returned. This method
     * is for defining the waiting time.
     *
     * @param timeOut is a time duration to get desired value
     * @return self-reference
     */
    protected THIS timeOut(Duration timeOut) {
        checkArgument(nonNull(timeOut), "Time out should not be a null value");
        checkArgument(!timeOut.isNegative(), "Time out should be a positive value");
        this.timeToGet = timeOut;
        return (THIS) this;
    }

    /**
     * Sometimes it is necessary to wait until some result that may be considered valuable is returned. This method
     * is for defining the waiting time.
     *
     * @param timeOut      is a time duration to get desired value
     * @param sleepingTime is a time duration between attempts to get desired value
     * @return self-reference
     */
    protected THIS timeOut(Duration timeOut, Duration sleepingTime) {
        checkArgument(nonNull(sleepingTime), "Sleeping time should not be a null value");
        checkArgument(!sleepingTime.isNegative(), "Sleeping time should be a positive value");
        this.timeOut(timeOut).sleepingTime = sleepingTime;
        return (THIS) this;
    }

    /**
     * This method defines an exception to be thrown when no valuable result is returned.
     *
     * @param exceptionSupplier is a supplier of exception to be thrown when invocation of {@link Function#apply(Object)}
     *                          doesn't return any valuable result. {@link Function#apply(Object)} is invoked on the resulted function
     * @return self-reference
     */
    protected THIS throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
        return (THIS) this;
    }

    /**
     * This method defines a function to get mediator value. This value is used to get desired result.
     *
     * @param from is a function that returns a value to get the result from
     * @return self-reference
     */
    THIS from(Function<T, ? extends M> from) {
        checkArgument(nonNull(from), "Function to get value from is not defined");
        this.from = from;
        return (THIS) this;
    }

    /**
     * This method defines a mediator value. This value is used to get desired result.
     *
     * @param from is a value to get the result from
     * @return self-reference
     */
    THIS from(M from) {
        checkArgument(nonNull(from), "The object to get value from is not defined");
        this.from = from;
        return (THIS) this;
    }

    /**
     * This method defines a supplier that builds and supplies a function to get mediator value.
     * This value is used to get desired result.
     *
     * @param from is a supplier that builds a supplies a function.
     *             This function returns a value to get the result from
     * @return self-reference
     */
    THIS from(SequentialGetStepSupplier<T, ? extends M, ?, ?, ?> from) {
        checkArgument(nonNull(from), "The supplier of a function is not defined");
        this.from = from;
        return (THIS) this;
    }

    @Override
    public final THIS addIgnored(List<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce a {@link java.awt.image.BufferedImage} after invocation of
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Function}.
     * This image is produced by {@link Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This image is produced if there is any subclass of {@link ImageCaptor}
     * or {@link Captor} that may produce a {@link java.awt.image.BufferedImage}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link ImageCaptor} or
     * {@link Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @return self-reference
     */
    @Override
    public THIS makeImageCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(BufferedImage.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce a {@link java.io.File} after invocation of
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Function}.
     * This image is produced by {@link Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This file is produced if there is any subclass of {@link FileCaptor}
     * or {@link Captor} that may produce a {@link java.io.File}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link FileCaptor} or
     * {@link Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @return self-reference
     */
    @Override
    public THIS makeFileCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(File.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce a {@link java.lang.StringBuilder} after invocation of
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Function}.
     * This image is produced by {@link Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This string builder is produced if there is any subclass of {@link StringCaptor}
     * or {@link Captor} that may produce a {@link java.lang.StringBuilder}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link StringCaptor} or
     * {@link Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @return self-reference
     */
    @Override
    public THIS makeStringCaptureOnFinish() {
        captorFilters.add(new CaptorFilterByProducedType(StringBuilder.class));
        return (THIS) this;
    }

    /**
     * Marks that it is needed to produce some value after invocation of
     * {@link java.util.function.Function#apply(Object)} on built resulted {@link java.util.function.Function}.
     * This image is produced by {@link Captor#getData(java.lang.Object)}
     *
     * <p>NOTE 1</p>
     * This value is produced if there is any subclass of {@link Captor} that
     * may produce  a value of type defined by {@param typeOfCapture}.
     *
     * <p>NOTE 2</p>
     * A subclass of {@link Captor} should be able to handle resulted values {@code R}
     * on success or input values {@code T} on failure.
     *
     * @param typeOfCapture is a type of a value to produce after the invocation of {@link java.util.function.Function#apply(Object)}
     *                      on the built function is finished.
     * @return self-reference
     */
    @Override
    public THIS onFinishMakeCaptureOfType(Class typeOfCapture) {
        captorFilters.add(new CaptorFilterByProducedType(typeOfCapture));
        return (THIS) this;
    }

    @Override
    public Function<T, R> get() {
        checkArgument(nonNull(from), "FROM-object is not defined");
        var composeWith = preparePreFunction();
        String resultedDescription = prepareStepDescription();
        var endFunction = getEndFunction();
        checkNotNull(endFunction);

        StepFunction<T, R> toBeReturned;
        if (StepFunction.class.isAssignableFrom(composeWith.getClass())) {
            var endFunctionStep  = toGet(resultedDescription, endFunction);
            endFunctionStep.addIgnored(copyOf(ignored));
            endFunctionStep.addCaptorFilters(captorFilters);
            toBeReturned = endFunctionStep.compose(composeWith);
        }
        else {
            toBeReturned = toGet(resultedDescription, endFunction.compose(composeWith));
        }

        toBeReturned.addIgnored(copyOf(ignored));
        toBeReturned.addCaptorFilters(captorFilters);
        return toBeReturned;
    }

    protected String prepareStepDescription() {
        var fromClazz = from.getClass();
        if (isLoggable(from)) {
            if (Function.class.isAssignableFrom(fromClazz)) {
                return format("%s. From %s", toString(), from.toString());
            }

            if (SequentialGetStepSupplier.class.isAssignableFrom(fromClazz)) {
                return format("%s. From %s", toString(), ((SequentialGetStepSupplier) from).description);
            }

            return format("%s. From %s", toString(), valueOf(from));
        }
        return toString();
    }

    protected Function<T, M> preparePreFunction() {
        var fromClazz = from.getClass();
        if (Function.class.isAssignableFrom(fromClazz)) {
            return (Function<T, M>) from;
        }

        if (SequentialGetStepSupplier.class.isAssignableFrom(fromClazz)) {
            return ((SequentialGetStepSupplier<T, M, ?, ?, ?>) from).get();
        }

        return t -> (M) from;
    }

    @Override
    public String toString() {
        var criteriaDescription = getCriteriaDescription();
        if (!isBlank(criteriaDescription)) {
            return format("%s [Criteria: %s]", description, criteriaDescription).trim();
        }
        return description;
    }

    @Override
    protected THIS clone() {
        try {
            return (THIS) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    abstract Function<M, R> getEndFunction();

    /**
     * Returns string description of the defined criteria. If there is some description and {@link Predicate#toString()}
     * returns some human-readable value then method returns this value. When criteria is defined then it returns
     * {@code '<not described condition>'}. When criteria is not defined then an empty string is returned.
     * @see StoryWriter#condition(String, Predicate)
     * @see #criteria(Predicate)
     * @see #criteria(String, Predicate)
     * @see #criteria(ConditionConcatenation, Predicate)
     * @see #criteria(ConditionConcatenation, String, Predicate)
     *
     * @return string description of the defined criteria
     */
    protected String getCriteriaDescription() {
        return ofNullable(condition)
                .map(pPredicate -> {
                    if (Condition.DescribedCondition.DescribedCondition.class.isAssignableFrom(pPredicate.getClass())) {
                        return pPredicate.toString();
                    }

                    if (isLoggable(pPredicate)) {
                        return pPredicate.toString();
                    }

                    return NOT_DESCRIBED;
                }).orElse(EMPTY);
    }

    private static abstract class PrivateGetObjectStepSupplier<T, R, M, THIS extends PrivateGetObjectStepSupplier<T, R, M, THIS>>
            extends SequentialGetStepSupplier<T, R, M, R, THIS> {

        private final Function<M, R> originalFunction;

        PrivateGetObjectStepSupplier(String description, Function<M, R> originalFunction) {
            super(description);
            this.originalFunction = originalFunction;
        }

        @Override
        Function<M, R> getEndFunction() {
            return ofNullable(condition)
                    .map(rPredicate -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, rPredicate, wait, sleep, supplier))
                                            .orElseGet(() -> getSingle(originalFunction, rPredicate, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, rPredicate, wait, supplier))
                                            .orElseGet(() -> getSingle(originalFunction, rPredicate, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getSingle(originalFunction, rPredicate, supplier))
                                    .orElseGet(() -> getSingle(originalFunction, rPredicate))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, wait, sleep, supplier))
                                            .orElseGet(() -> getSingle(originalFunction, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, wait, supplier))
                                            .orElseGet(() -> getSingle(originalFunction, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getSingle(originalFunction, supplier))
                                    .orElse(originalFunction)));
        }
    }

    /**
     *  This class is designed to build and supply functions to get some desired object-value.
     *
     * @param <T> is a type of an input value
     * @param <R> is a type of a result value
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectStepSupplier<T, R, THIS extends GetObjectStepSupplier<T, R, THIS>>
            extends PrivateGetObjectStepSupplier<T, R, T, THIS> {

        protected GetObjectStepSupplier(String description, Function<T, R> originalFunction) {
            super(description, originalFunction);
            from(t -> t);
        }
    }

    /**
     *  This class is designed to build and supply chained functions to get object-value.
     *
     * @param <T> is a type of an input value
     * @param <R> is a type of a result value
     * @param <M> is a type of a mediator value is used to get the result
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectChainedStepSupplier<T, R, M, THIS extends GetObjectChainedStepSupplier<T, R, M, THIS>>
            extends PrivateGetObjectStepSupplier<T, R, M, THIS> {

        protected GetObjectChainedStepSupplier(String description, Function<M, R> originalFunction) {
            super(description, originalFunction);
        }

        @Override
        protected THIS from(Function<T, ? extends M> from) {
            return super.from(from);
        }

        @Override
        protected THIS from(M from) {
            return super.from(from);
        }

        @Override
        protected THIS from(SequentialGetStepSupplier<T, ? extends M, ?, ?, ?> from) {
            return super.from(from);
        }
    }

    private static class PrivateGetObjectFromIterableStepSupplier<T, R, M, THIS extends PrivateGetObjectFromIterableStepSupplier<T, R, M, THIS>>
            extends SequentialGetStepSupplier<T, R, M, R, THIS> {

        private final Function<M, ? extends Iterable<R>> originalFunction;

        protected <S extends Iterable<R>> PrivateGetObjectFromIterableStepSupplier(String description, Function<M, S> originalFunction) {
            super(description);
            this.originalFunction = originalFunction;
        }

        @Override
        Function<M, R> getEndFunction() {
            return ofNullable(condition)
                    .map(rPredicate -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, rPredicate, wait, sleep, supplier))
                                            .orElseGet(() -> getFromIterable(originalFunction, rPredicate, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, rPredicate, wait, supplier))
                                            .orElseGet(() -> getFromIterable(originalFunction, rPredicate, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromIterable(originalFunction, rPredicate, supplier))
                                    .orElseGet(() -> getFromIterable(originalFunction, rPredicate))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, wait, sleep, supplier))
                                            .orElseGet(() -> getFromIterable(originalFunction, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, wait, supplier))
                                            .orElseGet(() -> getFromIterable(originalFunction, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromIterable(originalFunction, supplier))
                                    .orElse(getFromIterable(originalFunction))));
        }
    }

    /**
     * This class is designed to build and supply functions to get desired value using some iterable.
     *
     * @param <T> is a type of an input value
     * @param <R> is a type of a result value. Also it is a type of an item from iterable.
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromIterableStepSupplier<T, R, THIS extends GetObjectFromIterableStepSupplier<T, R, THIS>>
            extends PrivateGetObjectFromIterableStepSupplier<T, R, T, THIS> {

        protected <S extends Iterable<R>> GetObjectFromIterableStepSupplier(String description, Function<T, S> originalFunction) {
            super(description, originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get desired value using some iterable.
     *
     * @param <T> is a type of an input value
     * @param <R> is a type of a result value. Also it is a type of an item from iterable.
     * @param <M> is a type of a mediator value is used to get the result
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromIterableChainedStepSupplier<T, R, M, THIS extends GetObjectFromIterableChainedStepSupplier<T, R, M, THIS>>
            extends PrivateGetObjectFromIterableStepSupplier<T, R, M, THIS> {

        protected <S extends Iterable<R>> GetObjectFromIterableChainedStepSupplier(String description, Function<M, S> originalFunction) {
            super(description, originalFunction);
        }

        @Override
        protected THIS from(Function<T, ? extends M> from) {
            return super.from(from);
        }

        @Override
        protected THIS from(M from) {
            return super.from(from);
        }

        @Override
        protected THIS from(SequentialGetStepSupplier<T, ? extends M, ?, ?, ?> from) {
            return super.from(from);
        }
    }

    private static class PrivateGetObjectFromArrayStepSupplier<T, R, M, THIS extends PrivateGetObjectFromArrayStepSupplier<T, R, M, THIS>>
            extends SequentialGetStepSupplier<T, R, M, R, THIS> {

        private final Function<M, R[]> originalFunction;

        PrivateGetObjectFromArrayStepSupplier(String description, Function<M, R[]> originalFunction) {
            super(description);
            this.originalFunction = originalFunction;
        }

        @Override
        Function<M, R> getEndFunction() {
            return ofNullable(condition)
                    .map(rPredicate -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, rPredicate, wait, sleep, supplier))
                                            .orElseGet(() -> getFromArray(originalFunction, rPredicate, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, rPredicate, wait, supplier))
                                            .orElseGet(() -> getFromArray(originalFunction, rPredicate, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromArray(originalFunction, rPredicate, supplier))
                                    .orElseGet(() -> getFromArray(originalFunction, rPredicate))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, wait, sleep, supplier))
                                            .orElseGet(() -> getFromArray(originalFunction, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, wait, supplier))
                                            .orElseGet(() -> getFromArray(originalFunction, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromArray(originalFunction, supplier))
                                    .orElse(getFromArray(originalFunction))));
        }
    }

    /**
     * This class is designed to build and supply functions to get desired value using some array.
     *
     * @param <T> is a type of an input value
     * @param <R> is a type of a result value. Also it is a type of an item from array.
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromArrayStepSupplier<T, R, THIS extends GetObjectFromArrayStepSupplier<T, R, THIS>>
            extends PrivateGetObjectFromArrayStepSupplier<T, R, T, THIS> {

        GetObjectFromArrayStepSupplier(String description, Function<T, R[]> originalFunction) {
            super(description, originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get desired value using some array.
     *
     * @param <T> is a type of an input value
     * @param <R> is a type of a result value. Also it is a type of an item from array.
     * @param <M> is a type of a mediator value is used to get the result
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromArrayChainedStepSupplier<T, R, M, THIS extends GetObjectFromArrayChainedStepSupplier<T, R, M, THIS>>
            extends PrivateGetObjectFromArrayStepSupplier<T, R, M, THIS> {

        protected GetObjectFromArrayChainedStepSupplier(String description, Function<M, R[]> originalFunction) {
            super(description, originalFunction);
        }

        @Override
        protected THIS from(Function<T, ? extends M> from) {
            return super.from(from);
        }

        @Override
        protected THIS from(M from) {
            return super.from(from);
        }

        @Override
        protected THIS from(SequentialGetStepSupplier<T, ? extends M, ?, ?, ?> from) {
            return super.from(from);
        }
    }

    private static class PrivateGetIterableStepSupplier<T, S extends Iterable<R>, M, R, THIS extends PrivateGetIterableStepSupplier<T, S, M, R, THIS>>
            extends SequentialGetStepSupplier<T, S, M, R, THIS> {

        private final Function<M, S> originalFunction;

        PrivateGetIterableStepSupplier(String description, Function<M, S> originalFunction) {
            super(description);
            this.originalFunction = originalFunction;
        }

        @Override
        Function<M, S> getEndFunction() {
            return ofNullable(condition)
                    .map(rPredicate -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getIterable(originalFunction, rPredicate, wait, sleep, supplier))
                                            .orElseGet(() -> getIterable(originalFunction, rPredicate, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getIterable(originalFunction, rPredicate, wait, supplier))
                                            .orElseGet(() -> getIterable(originalFunction, rPredicate, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getIterable(originalFunction, rPredicate, supplier))
                                    .orElseGet(() -> getIterable(originalFunction, rPredicate))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getIterable(originalFunction, wait, sleep, supplier))
                                            .orElseGet(() -> getIterable(originalFunction, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getIterable(originalFunction, wait, supplier))
                                            .orElseGet(() -> getIterable(originalFunction, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getIterable(originalFunction, supplier))
                                    .orElse(originalFunction)));
        }
    }

    /**
     * This class is designed to build and supply functions to get some desired iterable-value.
     *
     * @param <T> is a type of an input value
     * @param <S> is a type of resulted iterable
     * @param <R> is a type of an an item from resulted iterable
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetIterableStepSupplier<T, S extends Iterable<R>, R, THIS extends GetIterableStepSupplier<T, S, R, THIS>>
            extends PrivateGetIterableStepSupplier<T, S, T, R, THIS> {

        GetIterableStepSupplier(String description, Function<T, S> originalFunction) {
            super(description, originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get some desired iterable-value.
     *
     * @param <T> is a type of an input value
     * @param <S> is a type of resulted iterable
     * @param <M> is a type of a mediator value is used to get the result
     * @param <R> is a type of an an item from resulted iterable
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetIterableChainedStepSupplier<T, S extends Iterable<R>, M, R, THIS extends GetIterableChainedStepSupplier<T, S, M, R, THIS>>
            extends PrivateGetIterableStepSupplier<T, S, M, R, THIS> {

        protected GetIterableChainedStepSupplier(String description, Function<M, S> originalFunction) {
            super(description, originalFunction);
        }

        @Override
        protected THIS from(Function<T, ? extends M> from) {
            return super.from(from);
        }

        @Override
        protected THIS from(M from) {
            return super.from(from);
        }

        @Override
        protected THIS from(SequentialGetStepSupplier<T, ? extends M, ?, ?, ?> from) {
            return super.from(from);
        }
    }

    private static class PrivateGetArrayStepSupplier<T, M, R, THIS extends PrivateGetArrayStepSupplier<T, M, R, THIS>>
            extends SequentialGetStepSupplier<T, R[], M, R, THIS> {

        private final Function<M, R[]> originalFunction;

        PrivateGetArrayStepSupplier(String description, Function<M, R[]> originalFunction) {
            super(description);
            this.originalFunction = originalFunction;
        }

        @Override
        Function<M, R[]> getEndFunction() {
            return ofNullable(condition)
                    .map(rPredicate -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, rPredicate, wait, sleep, supplier))
                                            .orElseGet(() -> getArray(originalFunction, rPredicate, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, rPredicate, wait, supplier))
                                            .orElseGet(() -> getArray(originalFunction, rPredicate, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getArray(originalFunction, rPredicate, supplier))
                                    .orElseGet(() -> getArray(originalFunction, rPredicate))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, wait, sleep, supplier))
                                            .orElseGet(() -> getArray(originalFunction, wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, wait, supplier))
                                            .orElseGet(() -> getArray(originalFunction, wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getArray(originalFunction, supplier))
                                    .orElse(originalFunction)));
        }
    }

    /**
     * This class is designed to build and supply functions to get some desired array-value.
     *
     * @param <T> is a type of an input value
     * @param <R> is a type of an an item from resulted array
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetArrayStepSupplier<T, R, THIS extends GetArrayStepSupplier<T, R, THIS>>
            extends PrivateGetArrayStepSupplier<T, T, R, THIS> {


        GetArrayStepSupplier(String description, Function<T, R[]> originalFunction) {
            super(description, originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get some desired array-value.
     *
     * @param <T> is a type of an input value
     * @param <M> is a type of a mediator value is used to get the result
     * @param <R> is a type of an an item from resulted array
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetArrayChainedStepSupplier<T, M, R, THIS extends GetArrayChainedStepSupplier<T, M, R, THIS>>
            extends PrivateGetArrayStepSupplier<T, M, R, THIS> {

        GetArrayChainedStepSupplier(String description, Function<M, R[]> originalFunction) {
            super(description, originalFunction);
        }

        @Override
        protected THIS from(Function<T, ? extends M> from) {
            return super.from(from);
        }

        @Override
        protected THIS from(M from) {
            return super.from(from);
        }

        @Override
        protected THIS from(SequentialGetStepSupplier<T, ? extends M, ?, ?, ?> from) {
            return super.from(from);
        }
    }
}
