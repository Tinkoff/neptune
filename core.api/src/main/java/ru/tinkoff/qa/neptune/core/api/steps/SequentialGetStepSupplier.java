package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.AdditionalMetadata;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetList;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.time.Duration.ofMillis;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure.CaptureOnFailureReader.readCaptorsOnFailure;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess.CaptureOnSuccessReader.readCaptorsOnSuccess;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting.MaxDepthOfReportingReader.getMaxDepth;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefaultGetParameterReader.*;
import static ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter.StepParameterCreator.createStepParameter;
import static ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData.ThrowWhenNoDataReader.getDeclaredBy;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetList.getList;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromArray.getFromArray;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetObjectFromIterable.getFromIterable;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.core.api.steps.conditions.ToGetSubArray.getArray;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

/**
 * This class is designed to build and to supply sequential functions to get desired value.
 * There are protected methods to be overridden/re-used as public when it is necessary.
 *
 * @param <T>    is a type of input value
 * @param <R>    is a type of returned value
 * @param <M>    is a type of mediator value is used to get the required result
 * @param <P>    is a type of value checked by a {@link Predicate}.
 * @param <THIS> this is the self-type. It is used for the method chaining.
 */
@SuppressWarnings("unchecked")
@SequentialGetStepSupplier.DefineGetImperativeParameterName
@SequentialGetStepSupplier.DefineResultDescriptionParameterName
@ThrowWhenNoData
public abstract class SequentialGetStepSupplier<T, R, M, P, THIS extends SequentialGetStepSupplier<T, R, M, P, THIS>> implements Cloneable,
        Supplier<Function<T, R>>, StepParameterPojo {

    private String description;
    protected boolean toReport = true;

    final Set<Class<? extends Throwable>> ignored = new HashSet<>();
    final List<Criteria<P>> conditions = new ArrayList<>();
    private Criteria<P> condition;
    private Object from;

    Duration timeToGet;
    Duration sleepingTime;
    ExceptionSupplier exceptionSupplier;

    protected SequentialGetStepSupplier() {
        super();
    }

    public static <T extends SequentialGetStepSupplier<?, ?, ?, ?, ?>> T turnReportingOff(T t) {
        return (T) t.turnReportingOff();
    }

    protected THIS setDescription(String description) {
        this.description = description;
        return (THIS) this;
    }

    @Override
    public Map<String, String> getParameters() {
        var result = new LinkedHashMap<String, String>();
        fillCustomParameters(result);

        var cls = (Class<?>) this.getClass();
        fillCriteriaParameters(result);
        fillTimeParameters(result);

        ofNullable(getFromMetadata(cls, true)).ifPresent(metaData -> {
            if (isLoggable(from) && nonNull(from)) {
                result.put(translate(metaData), valueOf(from));
            }
        });

        if ((from instanceof SequentialGetStepSupplier<?, ?, ?, ?, ?>)
                && this.getClass().getAnnotation(IncludeParamsOfInnerGetterStep.class) != null) {
            var get = (SequentialGetStepSupplier<?, ?, ?, ?, ?>) from;
            get.fillCustomParameters(result);
            get.fillCriteriaParameters(result);
            get.fillTimeParameters(result);
        }

        return result;
    }

    /**
     * Means that the starting/ending/result of the build-step won't be reported
     *
     * @return self-reference
     */
    THIS turnReportingOff() {
        toReport = false;
        return (THIS) this;
    }

    void fillCustomParameters(Map<String, String> parameters) {
        parameters.putAll(StepParameterPojo.super.getParameters());
    }

    void fillCriteriaParameters(Map<String, String> parameters) {
        var cls = (Class<?>) this.getClass();

        ofNullable(getCriteriaMetadata(cls, true)).ifPresent(metaData -> {
            int i = 0;
            for (var c : conditions) {
                var criteria = i == 0 ? translate(metaData) : translate(metaData) + " " + (i + 1);
                parameters.put(criteria, c.toString());
                i++;
            }
        });
    }

    void fillTimeParameters(Map<String, String> parameters) {
        var cls = (Class<?>) this.getClass();

        ofNullable(getTimeOutMetadata(cls, true)).ifPresent(metaData ->
                ofNullable(timeToGet).ifPresent(duration -> {
                    if (duration.toMillis() > 0) {
                        parameters.put(translate(metaData), formatDurationHMS(duration.toMillis()));
                    }
                }));

        ofNullable(getPollingTimeMetadata(cls, true)).ifPresent(metaData ->
                ofNullable(sleepingTime).ifPresent(duration -> {
                    if (duration.toMillis() > 0) {
                        parameters.put(translate(metaData), formatDurationHMS(duration.toMillis()));
                    }
                }));
    }

    /**
     * Sometimes it is necessary to get a value that suits some criteria.
     * This method adds the criteria to filter values.
     * When this methods and/or {@link #criteria(String, Predicate)},
     * {@link #criteriaOr(Criteria[])}, {@link #criteriaOnlyOne(Criteria[])},
     * {@link #criteriaNot(Criteria[])} are invoked previously then it joins conditions with 'AND'.
     *
     * @param criteria is the criteria to get required value
     */
    THIS criteria(Criteria<? super P> criteria) {
        conditions.add((Criteria<P>) criteria);
        condition = AND(conditions);
        return (THIS) this;
    }

    /**
     * Sometimes it is necessary to get a value that suits some criteria.
     * This method adds the criteria to filter values.
     * When this method and/or {@link #criteria(Criteria)},
     * {@link #criteriaOr(Criteria[])}, {@link #criteriaOnlyOne(Criteria[])},
     * {@link #criteriaNot(Criteria[])} are invoked previously then it joins conditions with 'AND'.
     *
     * @param description is a description of the criteria
     * @param predicate   is the the criteria
     */
    THIS criteria(String description, Predicate<? super P> predicate) {
        return criteria(condition(description, predicate));
    }

    /**
     * Sometimes it is necessary to get a value that suits some composite criteria which
     * unites multiple simple criteria in one OR-expression
     * This method adds the criteria to filter values.
     * When this method and/or {@link #criteria(Criteria)},
     * {@link #criteria(String, Predicate)}, {@link #criteriaOnlyOne(Criteria[])},
     * {@link #criteriaNot(Criteria[])} are invoked previously then it joins conditions with 'AND'.
     *
     * @param criteria to unite in OR-expression
     */
    THIS criteriaOr(Criteria<? super P>... criteria) {
        return criteria(OR(criteria));
    }

    /**
     * Sometimes it is necessary to get a value that suits some composite criteria which
     * unites multiple simple criteria in one XOR-expression
     * This method adds the criteria to filter values.
     * When this method and/or {@link #criteria(Criteria)},
     * {@link #criteriaOr(Criteria[])}, {@link #criteria(String, Predicate)},
     * {@link #criteriaNot(Criteria[])} are invoked previously then it joins conditions with 'AND'.
     *
     * @param criteria to unite in XOR-expression
     */
    THIS criteriaOnlyOne(Criteria<? super P>... criteria) {
        return criteria(ONLY_ONE(criteria));
    }

    /**
     * Sometimes it is necessary to get a value that suits some inverted criteria.
     * This method adds the criteria to filter values.
     * When this method and/or {@link #criteria(Criteria)},
     * {@link #criteriaOr(Criteria[])} (Criteria)}, {@link #criteria(String, Predicate)},
     * {@link #criteriaOnlyOne{Criteria[])} are invoked previously then it joins conditions with 'AND'.
     *
     * @param criteria one or more criteria to be inverted
     */
    THIS criteriaNot(Criteria<? super P>... criteria) {
        return criteria(NOT(criteria));
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

    final THIS clearTimeout() {
        this.timeOut(ofMillis(0));
        this.pollingInterval(ofMillis(0));

        var from = getFrom();
        if (from instanceof SequentialGetStepSupplier) {
            var newFrom = ((SequentialGetStepSupplier<T, ? extends M, ?, ?, ?>) from).clone();
            newFrom.clearTimeout();
            from(newFrom);
        }
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
     * This method says that it is necessary to throw an exception and to fail a test when
     * no valuable data is returned.
     *
     * @return self-reference
     */
    public THIS throwOnNoResult() {
        this.exceptionSupplier = new ExceptionSupplier(this);
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

    protected THIS addIgnored(Collection<Class<? extends Throwable>> toBeIgnored) {
        ignored.addAll(toBeIgnored);
        return (THIS) this;
    }

    protected THIS addIgnored(Class<? extends Throwable> toBeIgnored) {
        ignored.add(toBeIgnored);
        return (THIS) this;
    }

    /**
     * Some additional action on start of the getting step-function
     *
     * @param m is a mediator value is used to get the required result
     */
    protected void onStart(M m) {
    }

    /**
     * Some additional action if the step is successful
     *
     * @param r is a result value
     */
    protected void onSuccess(R r) {
    }

    /**
     * Some additional action if the step is failed
     *
     * @param m         is a mediator value is used to get the required result
     * @param throwable is a thrown exception/error
     */
    protected void onFailure(M m, Throwable throwable) {
    }

    Map<String, String> calculatedParameters() {
        var result = new LinkedHashMap<String, String>();
        if ((from instanceof SequentialGetStepSupplier<?, ?, ?, ?, ?>)
                && this.getClass().getAnnotation(IncludeParamsOfInnerGetterStep.class) != null) {
            var get = (SequentialGetStepSupplier<?, ?, ?, ?, ?>) from;
            var additional = get.calculatedParameters();

            if (additional.size() > 0) {
                result.putAll(additional);
            }
        }

        var additional = additionalParameters();
        if (nonNull(additional) && additional.size() > 0) {
            result.putAll(additional);
        }
        return result;
    }

    /**
     * Returns additional parameters calculated during step execution
     *
     * @return additional parameters calculated during step execution
     */
    protected Map<String, String> additionalParameters() {
        return Map.of();
    }

    @Override
    public Function<T, R> get() {
        checkArgument(nonNull(from), "FROM-object is not defined");
        var composeWith = preparePreFunction();
        var endFunction = new Function<M, R>() {
            @Override
            public R apply(M m) {
                try {
                    onStart(m);
                    var result = getEndFunction().apply(m);
                    onSuccess(result);
                    return result;
                } catch (Throwable t) {
                    onFailure(m, t);
                    throw t;
                }
            }
        };
        checkNotNull(endFunction);

        var params = getParameters();
        var resultDescription = translate(getResultMetadata(this.getClass(), true));
        var description = translate(translate(getImperativeMetadata(this.getClass(), true)) + " " + this.getDescription()).trim();

        var toBeReturned = new Get<>(description, endFunction)
                .setResultDescription(resultDescription)
                .setParameters(params)
                .setMaxDepth(getMaxDepth(this.getClass()))
                .compose(composeWith);

        if (toReport && catchSuccessEvent()) {
            var successCaptors = new ArrayList<Captor<Object, Object>>();
            readCaptorsOnSuccess(this.getClass(), successCaptors);
            toBeReturned
                    .addOnSuccessAdditional(of(FieldValueCaptureMaker.onSuccess(this)))
                    .addSuccessCaptors(successCaptors);
        }

        if (toReport && catchFailureEvent()) {
            var failureCaptors = new ArrayList<Captor<Object, Object>>();
            readCaptorsOnFailure(this.getClass(), failureCaptors);
            toBeReturned
                    .addOnFailureAdditional(of(FieldValueCaptureMaker.onFailure(this)))
                    .addFailureCaptors(failureCaptors);
        }

        if (!toReport) {
            toBeReturned.turnReportingOff();
        }

        return toBeReturned.setAdditionalParams(this::calculatedParameters);
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
        return getDescription();
    }

    /**
     * The method {@link #toString()} is possible to be overridden. This method is for such cases when it is necessary to
     * have access to step description.
     *
     * @return step description.
     */
    protected String getDescription() {
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

    Criteria<P> getCriteria() {
        return condition;
    }

    protected final Object getFrom() {
        return from;
    }

    public static class GetSimpleStepSupplier<T, R, THIS extends GetSimpleStepSupplier<T, R, THIS>>
            extends SequentialGetStepSupplier<T, R, T, R, THIS> {

        private final Function<T, R> originalFunction;

        protected GetSimpleStepSupplier(Function<T, R> originalFunction) {
            super();
            this.originalFunction = originalFunction;
            from(t -> t);
        }

        @Override
        protected Function<T, R> getEndFunction() {
            return ofNullable(timeToGet)
                    .map(wait -> ofNullable(sleepingTime)
                            .map(sleep -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getSingle(originalFunction, wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                    .orElseGet(() -> getSingle(originalFunction, wait, sleep, ignored.toArray(new Class[]{}))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getSingle(originalFunction, wait, supplier, ignored.toArray(new Class[]{})))
                                    .orElseGet(() -> getSingle(originalFunction, wait, ignored.toArray(new Class[]{})))))

                    .orElseGet(() -> ofNullable(exceptionSupplier)
                            .map(supplier -> getSingle(originalFunction, supplier, ignored.toArray(new Class[]{})))
                            .orElse(getSingle(originalFunction, ignored.toArray(new Class[]{}))));
        }
    }

    private static abstract class PrivateGetObjectStepSupplier<T, R, M, THIS extends PrivateGetObjectStepSupplier<T, R, M, THIS>>
            extends SequentialGetStepSupplier<T, R, M, R, THIS> {

        private final Function<M, R> originalFunction;

        PrivateGetObjectStepSupplier(Function<M, R> originalFunction) {
            super();
            this.originalFunction = originalFunction;
        }

        @Override
        public THIS criteria(Criteria<? super R> criteria) {
            return super.criteria(criteria);
        }

        @Override
        public THIS criteria(String description, Predicate<? super R> predicate) {
            return super.criteria(description, predicate);
        }

        @Override
        public THIS criteriaOr(Criteria<? super R>... criteria) {
            return super.criteriaOr(criteria);
        }

        @Override
        public THIS criteriaOnlyOne(Criteria<? super R>... criteria) {
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public THIS criteriaNot(Criteria<? super R>... criteria) {
            return super.criteriaNot(criteria);
        }

        @Override
        protected Function<M, R> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, c.get(), wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getSingle(originalFunction, c.get(), wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, c.get(), wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getSingle(originalFunction, c.get(), wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getSingle(originalFunction, c.get(), supplier, ignored.toArray(new Class[]{})))
                                    .orElseGet(() -> getSingle(originalFunction, c.get(), ignored.toArray(new Class[]{})))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getSingle(originalFunction, wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getSingle(originalFunction, wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getSingle(originalFunction, wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getSingle(originalFunction, supplier, ignored.toArray(new Class[]{})))
                                    .orElse(getSingle(originalFunction, ignored.toArray(new Class[]{})))));
        }
    }

    /**
     * This class is designed to build and supply functions to get some desired object-value.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectStepSupplier<T, R, THIS extends GetObjectStepSupplier<T, R, THIS>>
            extends PrivateGetObjectStepSupplier<T, R, T, THIS> {

        protected GetObjectStepSupplier(Function<T, R> originalFunction) {
            super(originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get object-value.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value
     * @param <M>    is a type of mediator value is used to get the result
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectChainedStepSupplier<T, R, M, THIS extends GetObjectChainedStepSupplier<T, R, M, THIS>>
            extends PrivateGetObjectStepSupplier<T, R, M, THIS> {

        protected GetObjectChainedStepSupplier(Function<M, R> originalFunction) {
            super(originalFunction);
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

        protected <S extends Iterable<R>> PrivateGetObjectFromIterableStepSupplier(Function<M, S> originalFunction) {
            super();
            this.originalFunction = originalFunction;
        }

        @Override
        public THIS criteria(Criteria<? super R> criteria) {
            return super.criteria(criteria);
        }

        @Override
        public THIS criteria(String description, Predicate<? super R> predicate) {
            return super.criteria(description, predicate);
        }

        @Override
        public THIS criteriaOr(Criteria<? super R>... criteria) {
            return super.criteriaOr(criteria);
        }

        @Override
        public THIS criteriaOnlyOne(Criteria<? super R>... criteria) {
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public THIS criteriaNot(Criteria<? super R>... criteria) {
            return super.criteriaNot(criteria);
        }

        @Override
        protected Function<M, R> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, c.get(), wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getFromIterable(originalFunction, c.get(), wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, c.get(), wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getFromIterable(originalFunction, c.get(), wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromIterable(originalFunction, c.get(), supplier, ignored.toArray(new Class[]{})))
                                    .orElseGet(() -> getFromIterable(originalFunction, c.get(), ignored.toArray(new Class[]{})))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getFromIterable(originalFunction, wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromIterable(originalFunction, wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getFromIterable(originalFunction, wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromIterable(originalFunction, supplier, ignored.toArray(new Class[]{})))
                                    .orElse(getFromIterable(originalFunction, ignored.toArray(new Class[]{})))));
        }
    }

    /**
     * This class is designed to build and supply functions to get desired value using some iterable.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value. Also it is a type of item from iterable.
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromIterableStepSupplier<T, R, THIS extends GetObjectFromIterableStepSupplier<T, R, THIS>>
            extends PrivateGetObjectFromIterableStepSupplier<T, R, T, THIS> {

        protected <S extends Iterable<R>> GetObjectFromIterableStepSupplier(Function<T, S> originalFunction) {
            super(originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get desired value using some iterable.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value. Also it is a type of item from iterable.
     * @param <M>    is a type of mediator value is used to get the result
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromIterableChainedStepSupplier<T, R, M, THIS extends GetObjectFromIterableChainedStepSupplier<T, R, M, THIS>>
            extends PrivateGetObjectFromIterableStepSupplier<T, R, M, THIS> {

        protected <S extends Iterable<R>> GetObjectFromIterableChainedStepSupplier(Function<M, S> originalFunction) {
            super(originalFunction);
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

        PrivateGetObjectFromArrayStepSupplier(Function<M, R[]> originalFunction) {
            super();
            this.originalFunction = originalFunction;
        }

        @Override
        public THIS criteria(Criteria<? super R> criteria) {
            return super.criteria(criteria);
        }

        @Override
        public THIS criteria(String description, Predicate<? super R> predicate) {
            return super.criteria(description, predicate);
        }

        @Override
        public THIS criteriaOr(Criteria<? super R>... criteria) {
            return super.criteriaOr(criteria);
        }

        @Override
        public THIS criteriaOnlyOne(Criteria<? super R>... criteria) {
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public THIS criteriaNot(Criteria<? super R>... criteria) {
            return super.criteriaNot(criteria);
        }

        @Override
        protected Function<M, R> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, c.get(), wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getFromArray(originalFunction, c.get(), wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, c.get(), wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getFromArray(originalFunction, c.get(), wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromArray(originalFunction, c.get(), supplier, ignored.toArray(new Class[]{})))
                                    .orElseGet(() -> getFromArray(originalFunction, c.get(), ignored.toArray(new Class[]{})))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getFromArray(originalFunction, wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getFromArray(originalFunction, wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getFromArray(originalFunction, wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getFromArray(originalFunction, supplier, ignored.toArray(new Class[]{})))
                                    .orElse(getFromArray(originalFunction, ignored.toArray(new Class[]{})))));
        }
    }

    /**
     * This class is designed to build and supply functions to get desired value using some array.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value. Also it is a type of item from array.
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromArrayStepSupplier<T, R, THIS extends GetObjectFromArrayStepSupplier<T, R, THIS>>
            extends PrivateGetObjectFromArrayStepSupplier<T, R, T, THIS> {

        protected GetObjectFromArrayStepSupplier(Function<T, R[]> originalFunction) {
            super(originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get desired value using some array.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value. Also it is a type of item from array.
     * @param <M>    is a type of mediator value is used to get the result
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetObjectFromArrayChainedStepSupplier<T, R, M, THIS extends GetObjectFromArrayChainedStepSupplier<T, R, M, THIS>>
            extends PrivateGetObjectFromArrayStepSupplier<T, R, M, THIS> {

        protected GetObjectFromArrayChainedStepSupplier(Function<M, R[]> originalFunction) {
            super(originalFunction);
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

    private static class PrivateGetListStepSupplier<T, S extends Iterable<R>, M, R, THIS extends PrivateGetListStepSupplier<T, S, M, R, THIS>>
            extends SequentialGetStepSupplier<T, List<R>, M, R, THIS> {

        private final Function<M, S> originalFunction;

        PrivateGetListStepSupplier(Function<M, S> originalFunction) {
            super();
            this.originalFunction = originalFunction;
        }

        @Override
        public THIS criteria(Criteria<? super R> criteria) {
            return super.criteria(criteria);
        }

        @Override
        public THIS criteria(String description, Predicate<? super R> predicate) {
            return super.criteria(description, predicate);
        }

        @Override
        public THIS criteriaOr(Criteria<? super R>... criteria) {
            return super.criteriaOr(criteria);
        }

        @Override
        public THIS criteriaOnlyOne(Criteria<? super R>... criteria) {
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public THIS criteriaNot(Criteria<? super R>... criteria) {
            return super.criteriaNot(criteria);
        }

        @Override
        protected Function<M, List<R>> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                            ofNullable(exceptionSupplier)
                                                    .map(supplier -> ToGetList.getList(originalFunction, c.get(), wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                                    .orElseGet(() -> ToGetList.getList(originalFunction, c.get(), wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> ToGetList.getList(originalFunction, c.get(), wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> ToGetList.getList(originalFunction, c.get(), wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> ToGetList.getList(originalFunction, c.get(), supplier, ignored.toArray(new Class[]{})))
                                    .orElseGet(() -> ToGetList.getList(originalFunction, c.get(), ignored.toArray(new Class[]{})))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> ToGetList.getList(originalFunction, wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> ToGetList.getList(originalFunction, wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> ToGetList.getList(originalFunction, wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> ToGetList.getList(originalFunction, wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> ToGetList.getList(originalFunction, supplier, ignored.toArray(new Class[]{})))
                                    .orElse(getList(originalFunction, ignored.toArray(new Class[]{})))));
        }
    }

    /**
     * This class is designed to build and supply functions to get some immutable list-value.
     *
     * @param <T>    is a type of input value
     * @param <S>    is a type of iterable
     * @param <R>    is a type of item from resulted iterable
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetListStepSupplier<T, S extends Iterable<R>, R, THIS extends GetListStepSupplier<T, S, R, THIS>>
            extends PrivateGetListStepSupplier<T, S, T, R, THIS> {

        protected GetListStepSupplier(Function<T, S> originalFunction) {
            super(originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get some immutable list-value.
     *
     * @param <T>    is a type of input value
     * @param <S>    is a type of iterable
     * @param <M>    is a type of mediator value is used to get the result
     * @param <R>    is a type of item from resulted iterable
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetListChainedStepSupplier<T, S extends Iterable<R>, M, R, THIS extends GetListChainedStepSupplier<T, S, M, R, THIS>>
            extends PrivateGetListStepSupplier<T, S, M, R, THIS> {

        protected GetListChainedStepSupplier(Function<M, S> originalFunction) {
            super(originalFunction);
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

    private static class PrivateGetArrayStepSupplier<T, R, M, THIS extends PrivateGetArrayStepSupplier<T, R, M, THIS>>
            extends SequentialGetStepSupplier<T, R[], M, R, THIS> {

        private final Function<M, R[]> originalFunction;

        PrivateGetArrayStepSupplier(Function<M, R[]> originalFunction) {
            super();
            this.originalFunction = originalFunction;
        }

        @Override
        public THIS criteria(Criteria<? super R> criteria) {
            return super.criteria(criteria);
        }

        @Override
        public THIS criteria(String description, Predicate<? super R> predicate) {
            return super.criteria(description, predicate);
        }

        @Override
        public THIS criteriaOr(Criteria<? super R>... criteria) {
            return super.criteriaOr(criteria);
        }

        @Override
        public THIS criteriaOnlyOne(Criteria<? super R>... criteria) {
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public THIS criteriaNot(Criteria<? super R>... criteria) {
            return super.criteriaNot(criteria);
        }

        @Override
        protected Function<M, R[]> getEndFunction() {
            return ofNullable(getCriteria())
                    .map(c -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime).map(sleep ->
                                    ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, c.get(), wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getArray(originalFunction, c.get(), wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, c.get(), wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getArray(originalFunction, c.get(), wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getArray(originalFunction, c.get(), supplier, ignored.toArray(new Class[]{})))
                                    .orElseGet(() -> getArray(originalFunction, c.get(), ignored.toArray(new Class[]{})))))

                    .orElseGet(() -> ofNullable(timeToGet)
                            .map(wait -> ofNullable(sleepingTime)
                                    .map(sleep -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, wait, sleep, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getArray(originalFunction, wait, sleep, ignored.toArray(new Class[]{}))))

                                    .orElseGet(() -> ofNullable(exceptionSupplier)
                                            .map(supplier -> getArray(originalFunction, wait, supplier, ignored.toArray(new Class[]{})))
                                            .orElseGet(() -> getArray(originalFunction, wait, ignored.toArray(new Class[]{})))))

                            .orElseGet(() -> ofNullable(exceptionSupplier)
                                    .map(supplier -> getArray(originalFunction, supplier, ignored.toArray(new Class[]{})))
                                    .orElse(getArray(originalFunction, ignored.toArray(new Class[]{})))));
        }
    }

    /**
     * This class is designed to build and supply functions to get some desired array-value.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of item from resulted array
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetArrayStepSupplier<T, R, THIS extends GetArrayStepSupplier<T, R, THIS>>
            extends PrivateGetArrayStepSupplier<T, R, T, THIS> {

        protected GetArrayStepSupplier(Function<T, R[]> originalFunction) {
            super(originalFunction);
            from(t -> t);
        }
    }

    /**
     * This class is designed to build and supply chained functions to get some desired array-value.
     *
     * @param <T>    is a type of input value
     * @param <M>    is a type of mediator value is used to get the result
     * @param <R>    is a type of item from resulted array
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public static abstract class GetArrayChainedStepSupplier<T, R, M, THIS extends GetArrayChainedStepSupplier<T, R, M, THIS>>
            extends PrivateGetArrayStepSupplier<T, R, M, THIS> {

        protected GetArrayChainedStepSupplier(Function<M, R[]> originalFunction) {
            super(originalFunction);
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

    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefineGetImperativeParameterName {
        /**
         * Defines name of imperative of a step
         *
         * @return imperative of a step
         */
        String value() default "Get:";
    }

    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefineTimeOutParameterName {
        /**
         * Defines name of the timeout-parameter
         *
         * @return Defined name of the timeout-parameter
         * @see SequentialGetStepSupplier#timeOut(Duration)
         */
        String value() default "Timeout/time for retrying";
    }

    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefinePollingTimeParameterName {
        /**
         * Defines name of the polling/sleeping time-parameter
         *
         * @return Defined name of the polling/sleeping time-parameter
         * @see SequentialGetStepSupplier#pollingInterval(Duration)
         */
        String value() default "Polling time";
    }

    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefineCriteriaParameterName {
        /**
         * Defines name of the criteria-parameter
         *
         * @return Defined name of the criteria-parameter
         * @see SequentialGetStepSupplier#criteria(Criteria)
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        String value() default "Criteria";
    }

    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefineFromParameterName {
        /**
         * Defines name of the from-parameter
         *
         * @return Defined name of the from-parameter
         * @see SequentialGetStepSupplier#from(Object)
         * @see SequentialGetStepSupplier#from(SequentialGetStepSupplier)
         * @see SequentialGetStepSupplier#from(Function)
         */
        String value() default "Get from";
    }

    @Retention(RUNTIME)
    @Target({TYPE})
    public @interface DefineResultDescriptionParameterName {
        /**
         * Defines name of the parameter that describes a returned value
         */
        String value() default "Result";
    }

    public static final class DefaultGetParameterReader {

        private DefaultGetParameterReader() {
            super();
        }

        public static AdditionalMetadata<StepParameter> getFromMetadata(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefineFromParameterName.class, "from", useInheritance);
        }

        public static AdditionalMetadata<StepParameter> getPollingTimeMetadata(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefinePollingTimeParameterName.class, "pollingTime", useInheritance);
        }

        public static AdditionalMetadata<StepParameter> getTimeOutMetadata(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefineTimeOutParameterName.class, "timeOut", useInheritance);
        }

        public static AdditionalMetadata<StepParameter> getCriteriaMetadata(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefineCriteriaParameterName.class, "criteria", useInheritance);
        }

        public static AdditionalMetadata<StepParameter> getImperativeMetadata(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefineGetImperativeParameterName.class, "imperative", useInheritance);
        }

        public static AdditionalMetadata<StepParameter> getResultMetadata(Class<?> toRead, boolean useInheritance) {
            return readAnnotation(toRead, DefineResultDescriptionParameterName.class, "resultDescription", useInheritance);
        }

        public static AdditionalMetadata<StepParameter> getExceptionMessageStartMetadata(Class<?> toRead, boolean useInheritance) {
            var declaredBy = getDeclaredBy(toRead, useInheritance);
            if (declaredBy == null) {
                return null;
            }

            return new AdditionalMetadata<>(declaredBy, "errorMessageStartingOnEmptyResult", StepParameter.class, () -> {
                try {
                    return createStepParameter(declaredBy.getAnnotation(ThrowWhenNoData.class).startDescription());
                } catch (Exception t) {
                    throw new RuntimeException(t);
                }
            });
        }

        private static AdditionalMetadata<StepParameter> readAnnotation(Class<?> toRead, Class<? extends Annotation> annotationClass,
                                                                        String name, boolean useInheritance) {
            if (!SequentialGetStepSupplier.class.isAssignableFrom(toRead)) {
                return null;
            }

            var cls = toRead;
            while (!cls.equals(Object.class)) {
                var annotation = cls.getAnnotation(annotationClass);
                if (annotation != null) {
                    try {
                        var valueMethod = annotation.annotationType().getMethod("value");
                        valueMethod.setAccessible(true);
                        return new AdditionalMetadata<>(cls, name, StepParameter.class, () -> {
                            try {
                                return createStepParameter((String) valueMethod.invoke(annotation));
                            } catch (Exception t) {
                                throw new RuntimeException(t);
                            }
                        });
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (useInheritance) {
                    cls = cls.getSuperclass();
                } else {
                    return null;
                }
            }

            return null;
        }

    }
}
