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
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.AND;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.StepFunction.toGet;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromArray.getFromArray;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromIterable.getFromIterable;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubArray.getArray;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubIterable.getIterable;

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
@SequentialGetStepSupplier.DefaultParameterNames
public abstract class SequentialGetStepSupplier<T, R, M, P, THIS extends SequentialGetStepSupplier<T, R, M, P, THIS>> implements Cloneable,
        Supplier<Function<T, R>>, IgnoresThrowable<THIS>, MakesCapturesOnFinishing<THIS> {

    private final String description;

    final Set<Class<? extends Throwable>> ignored = new HashSet<>();

    private final List<CaptorFilterByProducedType> captorFilters = new ArrayList<>();

    final List<Criteria<P>> conditions = new ArrayList<>();

    private Criteria<P> condition;

    Object from;

    Duration timeToGet;

    Duration sleepingTime;

    Supplier<? extends RuntimeException> exceptionSupplier;

    protected SequentialGetStepSupplier(String description) {
        this.description = description;
        MakesCapturesOnFinishing.makeCaptureSettings(this);
    }

    protected Map<String, String> getParameters() {
        return DefaultReportStepParameterFactory.getParameters(this);
    }

    /**
     * Sometimes it is necessary to get a value that suits some criteria.
     * This method adds the criteria to filter values.
     * When this method and/or {@link #criteria(String, Predicate)} are invoked previously then it joins conditions with 'AND'.
     *
     * @param criteria is the criteria to get required value
     */
    protected THIS criteria(Criteria<? super P> criteria) {
        conditions.add((Criteria<P>) criteria);
        condition = AND(conditions);
        return (THIS) this;
    }

    /**
     * Sometimes it is necessary to get a value that suits some criteria.
     * This method adds the criteria to filter values.
     * When this method and/or {@link #criteria(Criteria)} are invoked previously then it joins conditions with 'AND'.
     *
     * @param description is a description of the criteria
     * @param predicate   is the the criteria
     */
    protected THIS criteria(String description, Predicate<? super P> predicate) {
        return criteria(condition(description, predicate));
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
     * Sometimes it is necessary to wait until some result that may be considered valuable is returned.
     * It sets how often the function should be evaluated.
     *
     * @param pollingTime The timeout duration.
     * @return A self reference.
     */
    protected THIS pollingInterval(Duration pollingTime) {
        checkArgument(nonNull(pollingTime), "Sleeping time should not be a null value");
        checkArgument(!pollingTime.isNegative(), "Sleeping time should be a positive value");
        this.sleepingTime = pollingTime;
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
    public THIS addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return (THIS) this;
    }

    @Override
    public THIS addIgnored(Class<? extends Throwable> toBeIgnored) {
        ignored.add(toBeIgnored);
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
    public THIS onFinishMakeCaptureOfType(Class<?> typeOfCapture) {
        captorFilters.add(new CaptorFilterByProducedType(typeOfCapture));
        return (THIS) this;
    }

    @Override
    public Function<T, R> get() {
        checkArgument(nonNull(from), "FROM-object is not defined");
        var composeWith = preparePreFunction();
        var endFunction = getEndFunction();
        checkNotNull(endFunction);

        StepFunction<T, R> toBeReturned;
        if (StepFunction.class.isAssignableFrom(composeWith.getClass())) {
            var endFunctionStep = toGet(description, endFunction);
            endFunctionStep.addIgnored(ignored);
            endFunctionStep.addCaptorFilters(captorFilters);
            toBeReturned = endFunctionStep.compose(composeWith);
            endFunctionStep.setParameters(getParameters());
        } else {
            toBeReturned = toGet(description, endFunction.compose(composeWith));
        }

        toBeReturned.addIgnored(ignored);
        toBeReturned.addCaptorFilters(captorFilters);
        return toBeReturned.setParameters(getParameters());
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

    protected abstract Function<M, R> getEndFunction();

    protected Criteria<P> getCriteria() {
        return condition;
    }

    private static abstract class PrivateGetObjectStepSupplier<T, R, M, THIS extends PrivateGetObjectStepSupplier<T, R, M, THIS>>
            extends SequentialGetStepSupplier<T, R, M, R, THIS> {

        private final Function<M, R> originalFunction;

        PrivateGetObjectStepSupplier(String description, Function<M, R> originalFunction) {
            super(description);
            this.originalFunction = originalFunction;
        }

        @Override
        protected Function<M, R> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, c.get(), wait, sleep, supplier))
                                            .orElseGet(() -> getSingle(originalFunction, c.get(), wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, c.get(), wait, supplier))
                                            .orElseGet(() -> getSingle(originalFunction, c.get(), wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getSingle(originalFunction, c.get(), supplier))
                                    .orElseGet(() -> getSingle(originalFunction, c.get()))))

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
     * This class is designed to build and supply functions to get some desired object-value.
     *
     * @param <T>    is a type of an input value
     * @param <R>    is a type of a result value
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
     * This class is designed to build and supply chained functions to get object-value.
     *
     * @param <T>    is a type of an input value
     * @param <R>    is a type of a result value
     * @param <M>    is a type of a mediator value is used to get the result
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
        protected Function<M, R> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, c.get(), wait, sleep, supplier))
                                            .orElseGet(() -> getFromIterable(originalFunction, c.get(), wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, c.get(), wait, supplier))
                                            .orElseGet(() -> getFromIterable(originalFunction, c.get(), wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromIterable(originalFunction, c.get(), supplier))
                                    .orElseGet(() -> getFromIterable(originalFunction, c.get()))))

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
     * @param <T>    is a type of an input value
     * @param <R>    is a type of a result value. Also it is a type of an item from iterable.
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
     * @param <T>    is a type of an input value
     * @param <R>    is a type of a result value. Also it is a type of an item from iterable.
     * @param <M>    is a type of a mediator value is used to get the result
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
        protected Function<M, R> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, c.get(), wait, sleep, supplier))
                                            .orElseGet(() -> getFromArray(originalFunction, c.get(), wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, c.get(), wait, supplier))
                                            .orElseGet(() -> getFromArray(originalFunction, c.get(), wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromArray(originalFunction, c.get(), supplier))
                                    .orElseGet(() -> getFromArray(originalFunction, c.get()))))

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
     * @param <T>    is a type of an input value
     * @param <R>    is a type of a result value. Also it is a type of an item from array.
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromArrayStepSupplier<T, R, THIS extends GetObjectFromArrayStepSupplier<T, R, THIS>>
            extends PrivateGetObjectFromArrayStepSupplier<T, R, T, THIS> {

        protected GetObjectFromArrayStepSupplier(String description, Function<T, R[]> originalFunction) {
            super(description, originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get desired value using some array.
     *
     * @param <T>    is a type of an input value
     * @param <R>    is a type of a result value. Also it is a type of an item from array.
     * @param <M>    is a type of a mediator value is used to get the result
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
        protected Function<M, S> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getIterable(originalFunction, c.get(), wait, sleep, supplier))
                                            .orElseGet(() -> getIterable(originalFunction, c.get(), wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getIterable(originalFunction, c.get(), wait, supplier))
                                            .orElseGet(() -> getIterable(originalFunction, c.get(), wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getIterable(originalFunction, c.get(), supplier))
                                    .orElseGet(() -> getIterable(originalFunction, c.get()))))

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
     * @param <T>    is a type of an input value
     * @param <S>    is a type of resulted iterable
     * @param <R>    is a type of an an item from resulted iterable
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetIterableStepSupplier<T, S extends Iterable<R>, R, THIS extends GetIterableStepSupplier<T, S, R, THIS>>
            extends PrivateGetIterableStepSupplier<T, S, T, R, THIS> {

        protected GetIterableStepSupplier(String description, Function<T, S> originalFunction) {
            super(description, originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get some desired iterable-value.
     *
     * @param <T>    is a type of an input value
     * @param <S>    is a type of resulted iterable
     * @param <M>    is a type of a mediator value is used to get the result
     * @param <R>    is a type of an an item from resulted iterable
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
        protected Function<M, R[]> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, c.get(), wait, sleep, supplier))
                                            .orElseGet(() -> getArray(originalFunction, c.get(), wait, sleep)))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, c.get(), wait, supplier))
                                            .orElseGet(() -> getArray(originalFunction, c.get(), wait))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getArray(originalFunction, c.get(), supplier))
                                    .orElseGet(() -> getArray(originalFunction, c.get()))))

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
     * @param <T>    is a type of an input value
     * @param <R>    is a type of an an item from resulted array
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetArrayStepSupplier<T, R, THIS extends GetArrayStepSupplier<T, R, THIS>>
            extends PrivateGetArrayStepSupplier<T, T, R, THIS> {


        protected GetArrayStepSupplier(String description, Function<T, R[]> originalFunction) {
            super(description, originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get some desired array-value.
     *
     * @param <T>    is a type of an input value
     * @param <M>    is a type of a mediator value is used to get the result
     * @param <R>    is a type of an an item from resulted array
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetArrayChainedStepSupplier<T, M, R, THIS extends GetArrayChainedStepSupplier<T, M, R, THIS>>
            extends PrivateGetArrayStepSupplier<T, M, R, THIS> {

        protected GetArrayChainedStepSupplier(String description, Function<M, R[]> originalFunction) {
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

    /**
     * This annotation is designed to mark subclasses of {@link SequentialGetStepSupplier}. It is
     * used for the reading of timeouts, polling intervals, {@code from}-values, criteria and for the
     * forming of parameters of a resulted step-function.
     *
     * @see SequentialGetStepSupplier#timeOut(Duration)
     * @see SequentialGetStepSupplier#pollingInterval(Duration)
     * @see SequentialGetStepSupplier#criteria(Criteria)
     * @see SequentialGetStepSupplier#criteria(String, Predicate)
     * @see SequentialGetStepSupplier#from(Object)
     * @see SequentialGetStepSupplier#from(SequentialGetStepSupplier)
     * @see SequentialGetStepSupplier#from(Function)
     */
    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefaultParameterNames {

        /**
         * Defines name of the timeout-parameter
         *
         * @return Defined name of the timeout-parameter
         * @see SequentialGetStepSupplier#timeOut(Duration)
         */
        String timeOut() default "Timeout/time for retrying";

        /**
         * Defines name of the polling/sleeping time-parameter
         *
         * @return Defined name of the polling/sleeping time-parameter
         * @see SequentialGetStepSupplier#pollingInterval(Duration)
         */
        String pollingTime() default "Polling time";

        /**
         * Defines name of the criteria-parameter
         *
         * @return Defined name of the criteria-parameter
         * @see SequentialGetStepSupplier#criteria(Criteria)
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        String criteria() default "Result criteria";

        /**
         * Defines name of the from-parameter
         *
         * @return Defined name of the from-parameter
         * @see SequentialGetStepSupplier#from(Object)
         * @see SequentialGetStepSupplier#from(SequentialGetStepSupplier)
         * @see SequentialGetStepSupplier#from(Function)
         */
        String from() default "Result criteria";
    }
}
