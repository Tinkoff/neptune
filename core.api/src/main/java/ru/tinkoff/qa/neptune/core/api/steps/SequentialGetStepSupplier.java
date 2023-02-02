package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.ArrayCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.IterableCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.*;
import ru.tinkoff.qa.neptune.core.api.steps.conditions.ResultSelection;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.core.api.steps.selections.ItemsCountCondition;
import ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItem;
import ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItems;

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
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.*;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.DefaultGetParameterReader.*;
import static ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting.MaxDepthOfReportingReader.getMaxDepth;
import static ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter.StepParameterCreator.createStepParameter;
import static ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData.ThrowWhenNoDataReader.getDeclaredBy;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItem.selectItemOfArray;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItem.selectItemOfIterable;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItems.selectArray;
import static ru.tinkoff.qa.neptune.core.api.steps.selections.SelectionOfItems.selectList;
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

    private final Function<M, Object> stepFunction;
    private final FunctionFactory<M, Object, R, P> functionFactory;

    protected <S> SequentialGetStepSupplier(Function<M, S> stepFunction, FunctionFactory<M, S, R, P> functionFactory) {
        super();
        this.stepFunction = (Function<M, Object>) stepFunction;
        this.functionFactory = (FunctionFactory<M, Object, R, P>) functionFactory;
    }

    public static <T extends SequentialGetStepSupplier<?, ?, ?, ?, ?>> T turnReportingOff(T t) {
        return (T) t.turnReportingOff();
    }

    public static <T extends SequentialGetStepSupplier<?, ?, ?, ?, ?>> T makeACopy(T t) {
        return (T) t.copy();
    }

    public static <T extends SequentialGetStepSupplier<?, ?, ?, ?, ?>> T eraseTimeOut(T t) {
        return (T) t.clearTimeout();
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

    void fillSelectionParameters(Map<String, String> parameters) {

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

    void ignoreSelection() {

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
     * @param predicate   is the criteria
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
            ((SequentialGetStepSupplier<T, ? extends M, ?, ?, ?>) from).clearTimeout();
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
        return new AdditionalParameterSupplier(from, this, () -> {
            var result = new LinkedHashMap<>(this.additionalParameters());
            fillSelectionParameters(result);
            return result;
        }).get();
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

    final THIS copy() {
        try {
            var copy = (THIS) super.clone();
            var parentStep = copy.getFrom();
            if (parentStep instanceof SequentialGetStepSupplier) {
                copy.from(((SequentialGetStepSupplier<T, ? extends M, ?, ?, ?>) parentStep).copy());
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    protected Function<M, R> getEndFunction() {
        return ofNullable(getCriteria())
            .map(c -> ofNullable(timeToGet)
                .map(wait -> ofNullable(sleepingTime).map(sleep ->
                        ofNullable(exceptionSupplier)
                            .map(supplier -> functionFactory.createFunction(stepFunction, c.get(), wait, sleep, supplier, ignored))
                            .orElseGet(() -> functionFactory.createFunction(stepFunction, c.get(), wait, sleep, ignored)))

                    .orElseGet(() -> ofNullable(exceptionSupplier)
                        .map(supplier -> functionFactory.createFunction(stepFunction, c.get(), wait, supplier, ignored))
                        .orElseGet(() -> functionFactory.createFunction(stepFunction, c.get(), wait, ignored))))

                .orElseGet(() -> ofNullable(exceptionSupplier)
                    .map(supplier -> functionFactory.createFunction(stepFunction, c.get(), supplier, ignored))
                    .orElseGet(() -> functionFactory.createFunction(stepFunction, c.get(), ignored))))

            .orElseGet(() -> ofNullable(timeToGet)
                .map(wait -> ofNullable(sleepingTime)
                    .map(sleep -> ofNullable(exceptionSupplier)
                        .map(supplier -> functionFactory.createFunction(stepFunction, wait, sleep, supplier, ignored))
                        .orElseGet(() -> functionFactory.createFunction(stepFunction, wait, sleep, ignored)))

                    .orElseGet(() -> ofNullable(exceptionSupplier)
                        .map(supplier -> functionFactory.createFunction(stepFunction, wait, supplier, ignored))
                        .orElseGet(() -> functionFactory.createFunction(stepFunction, wait, ignored))))

                .orElseGet(() -> ofNullable(exceptionSupplier)
                    .map(supplier -> functionFactory.createFunction(stepFunction, supplier, ignored))
                    .orElse(functionFactory.createFunction(stepFunction, ignored))));
    }

    protected Criteria<P> getCriteria() {
        return condition;
    }

    protected final Object getFrom() {
        return from;
    }

    public abstract static class GetSimpleStepSupplier<T, R, THIS extends GetSimpleStepSupplier<T, R, THIS>>
        extends SequentialGetStepSupplier<T, R, T, R, THIS> {

        protected GetSimpleStepSupplier(Function<T, R> originalFunction) {
            super(originalFunction, new FunctionFactory.ObjectFunctionFactory<>());
            from(t -> t);
        }
    }

    private abstract static class PrivateGetConditionalStepSupplier<T, R, M, P, THIS extends PrivateGetConditionalStepSupplier<T, R, M, P, THIS>>
        extends SequentialGetStepSupplier<T, R, M, P, THIS> {

        final Captor<?, ?> captorForResultSelection;
        ResultSelection<?, ?> selection;

        protected <S> PrivateGetConditionalStepSupplier(Function<M, S> stepFunction,
                                                        FunctionFactory<M, S, R, P> functionFactory,
                                                        Captor<?, ?> captorForResultSelection) {
            super(stepFunction, functionFactory);
            this.captorForResultSelection = captorForResultSelection;
        }

        @Override
        public THIS criteria(Criteria<? super P> criteria) {
            return super.criteria(criteria);
        }

        @Override
        public THIS criteria(String description, Predicate<? super P> predicate) {
            return super.criteria(description, predicate);
        }

        @Override
        public THIS criteriaOr(Criteria<? super P>... criteria) {
            return super.criteriaOr(criteria);
        }

        @Override
        public THIS criteriaOnlyOne(Criteria<? super P>... criteria) {
            return super.criteriaOnlyOne(criteria);
        }

        @Override
        public THIS criteriaNot(Criteria<? super P>... criteria) {
            return super.criteriaNot(criteria);
        }

        @Override
        public Function<T, R> get() {
            var get = (Get<T, R>) super.get();
            if (nonNull(selection)) {
                get.setResultSelection(selection);
                get.setCaptorOfFailedResultSelection(captorForResultSelection);
            }
            return get;
        }
    }

    interface ReturnsOnCondition<T, THIS extends ReturnsOnCondition<T, THIS>> {

        /**
         * Defines a condition for entire set of found/suitable elements.
         *
         * @param condition a condition for entire set of items
         * @return self-reference
         */
        THIS returnOnCondition(Criteria<T> condition);

        /**
         * Defines a condition for entire set of found/suitable elements.
         *
         * @param description describes the condition
         * @param predicate   defines the condition
         * @return self-reference
         */
        default THIS returnOnCondition(String description, Predicate<T> predicate) {
            return returnOnCondition(condition(description, predicate));
        }

        /**
         * Defines a condition for entire set of found/suitable elements. Defined
         * criteria will be transformed into OR-expression
         *
         * @param condition condition for entire set of items
         * @return self-reference
         */
        default THIS returnOnConditionOr(Criteria<T>... condition) {
            return returnOnCondition(OR(condition));
        }

        /**
         * Defines a condition for entire set of found/suitable elements. Defined
         * criteria will be transformed into XOR-expression
         *
         * @param condition condition for entire set of items
         * @return self-reference
         */
        default THIS returnOnConditionOnlyOne(Criteria<T>... condition) {
            return returnOnCondition(ONLY_ONE(condition));
        }

        /**
         * Defines a condition for entire set of found/suitable elements. Defined
         * criteria will be inverted
         *
         * @param condition condition for entire set of items
         * @return self-reference
         */
        default THIS returnOnConditionOnlyNot(Criteria<T>... condition) {
            return returnOnCondition(NOT(condition));
        }
    }

    private static abstract class PrivateGetIterableStepSupplier<T, R, M, P, THIS extends PrivateGetIterableStepSupplier<T, R, M, P, THIS>>
        extends PrivateGetConditionalStepSupplier<T, R, M, P, THIS> {
        protected final FunctionFactory.IterableFunctionFactory<M, ?, R, P> iterableFunctionFactory;

        protected <S> PrivateGetIterableStepSupplier(Function<M, S> stepFunction,
                                                     FunctionFactory.IterableFunctionFactory<M, S, R, P> functionFactory,
                                                     Captor<?, ?> captorForResultSelection) {
            super(stepFunction, functionFactory, captorForResultSelection);
            this.iterableFunctionFactory = functionFactory;
        }

        @Override
        void fillSelectionParameters(Map<String, String> parameters) {
            ofNullable(iterableFunctionFactory.getResultSelection())
                .ifPresent(rs -> parameters.putAll(rs.getParameters()));
        }

        @Override
        void ignoreSelection() {
            iterableFunctionFactory.ignoreSelection();
        }

        interface SelectionOptionsForList<R, THIS extends SelectionOptionsForList<R, THIS>>
            extends ReturnsOnCondition<List<R>, THIS> {

            private PrivateGetIterableStepSupplier<?, List<R>, ?, ?, ?> cast() {
                return (PrivateGetIterableStepSupplier<?, List<R>, ?, ?, ?>) this;
            }

            private SelectionOfItems.SelectionOfList<R> getListSelection(PrivateGetIterableStepSupplier<?, List<R>, ?, ?, ?> casted) {
                return (SelectionOfItems.SelectionOfList<R>) ofNullable(casted.iterableFunctionFactory.getResultSelection())
                    .orElseGet(() -> {
                        var s = (SelectionOfItems.SelectionOfList<R>) selectList();
                        casted.iterableFunctionFactory.setResultSelection(s);
                        casted.selection = s;
                        return s;
                    });
            }

            /**
             * Sets count of items to take from the list of found/suitable elements.
             * Invocation of this method erases value set by {@link #returnItemsOfIndexes(Integer...)}
             *
             * @param size size of resulted list
             * @return self-reference
             */
            default THIS returnListOfSize(int size) {
                var casted = cast();
                var selection = getListSelection(casted);
                selection.ofCount(size);
                return (THIS) this;
            }

            /**
             * Defines indexes of found items to be returned.
             * Invocation of this method erases value set by {@link #returnListOfSize(int)}
             * and {@link #returnBeforeIndex(int)} / {@link #returnAfterIndex(int)}
             *
             * @param indexes indexes of items to be returned
             * @return self-reference
             */
            default THIS returnItemsOfIndexes(Integer... indexes) {
                var casted = cast();
                var selection = getListSelection(casted);
                selection.indexes(indexes);
                return (THIS) this;
            }


            /**
             * Sets upper list index (exclusively) to take items from the list of found/suitable elements.
             * Invocation of this method replaces value set by {@link #returnAfterIndex(int)} and
             * erases value set by {@link #returnItemsOfIndexes(Integer...)}
             *
             * @param index is exclusive value of the upper index
             * @return self-reference
             */
            default THIS returnBeforeIndex(int index) {
                var casted = cast();
                var selection = getListSelection(casted);
                selection.beforeIndex(index);
                return (THIS) this;
            }

            /**
             * Sets lower list index (exclusively) to take items from the list of found/suitable elements.
             * Invocation of this method replaces value set by {@link #returnBeforeIndex(int)} and
             * erases value set by {@link #returnItemsOfIndexes(Integer...)}
             *
             * @param index is exclusive value of the lower index
             * @return self-reference
             */
            default THIS returnAfterIndex(int index) {
                var casted = cast();
                var selection = getListSelection(casted);
                selection.afterIndex(index);
                return (THIS) this;
            }

            /**
             * Defines a size condition for entire list of found/suitable elements.
             *
             * @param sizeCondition a size condition for entire list
             * @return self-reference
             */
            default THIS returnIfEntireSize(ItemsCountCondition sizeCondition) {
                var casted = cast();
                var selection = getListSelection(casted);
                selection.whenCount(sizeCondition);
                return (THIS) this;
            }

            /**
             * Defines a condition for list of found/suitable elements.
             *
             * @param condition a condition for entire list
             * @return self-reference
             */
            @Override
            default THIS returnOnCondition(Criteria<List<R>> condition) {
                var casted = cast();
                var selection = getListSelection(casted);
                selection.onCondition(condition);
                return (THIS) this;
            }
        }

        interface SelectionOptionsForArray<R, THIS extends SelectionOptionsForArray<R, THIS>> extends
            ReturnsOnCondition<R[], THIS> {

            private PrivateGetIterableStepSupplier<?, R[], ?, ?, ?> cast() {
                return (PrivateGetIterableStepSupplier<?, R[], ?, ?, ?>) this;
            }

            private SelectionOfItems.SelectionOfArray<R> getArraySelection(PrivateGetIterableStepSupplier<?, R[], ?, ?, ?> casted) {
                return (SelectionOfItems.SelectionOfArray<R>) ofNullable(casted.iterableFunctionFactory.getResultSelection())
                    .orElseGet(() -> {
                        var s = (SelectionOfItems.SelectionOfArray<R>) selectArray();
                        casted.iterableFunctionFactory.setResultSelection(s);
                        casted.selection = s;
                        return s;
                    });
            }

            /**
             * Sets count of items to take from the array of found/suitable elements.
             * Invocation of this method erases value set by {@link #returnItemsOfIndexes(Integer...)}.
             *
             * @param length length of resulted array
             * @return self-reference
             */
            default THIS returnArrayOfLength(int length) {
                var casted = cast();
                var selection = getArraySelection(casted);
                selection.ofCount(length);
                return (THIS) this;
            }

            /**
             * Defines indexes of found items to be returned.
             * Invocation of this method erases value set by {@link #returnArrayOfLength(int)}
             * and {@link #returnBeforeIndex(int)} / {@link #returnAfterIndex(int)}
             *
             * @param indexes indexes of items to be returned
             * @return self-reference
             */
            default THIS returnItemsOfIndexes(Integer... indexes) {
                var casted = cast();
                var selection = getArraySelection(casted);
                selection.indexes(indexes);
                return (THIS) this;
            }

            /**
             * Sets upper array index (exclusively) to take items from the array of found/suitable elements.
             * Invocation of this method replaces value set by {@link #returnAfterIndex(int)} and
             * erases value set by {@link #returnItemsOfIndexes(Integer...)}
             *
             * @param index is exclusive value of the upper index
             * @return self-reference
             */
            default THIS returnBeforeIndex(int index) {
                var casted = cast();
                var selection = getArraySelection(casted);
                selection.beforeIndex(index);
                return (THIS) this;
            }

            /**
             * Sets lower array index (exclusively) to take items from the array of found/suitable elements.
             * Invocation of this method replaces value set by {@link #returnBeforeIndex(int)} and
             * erases value set by {@link #returnItemsOfIndexes(Integer...)}
             *
             * @param index is exclusive value of the lower index
             * @return self-reference
             */
            default THIS returnAfterIndex(int index) {
                var casted = cast();
                var selection = getArraySelection(casted);
                selection.afterIndex(index);
                return (THIS) this;
            }

            /**
             * Defines a length condition for entire array of found/suitable elements.
             *
             * @param lengthCondition a length condition for entire array
             * @return self-reference
             */
            default THIS returnIfEntireLength(ItemsCountCondition lengthCondition) {
                var casted = cast();
                var selection = getArraySelection(casted);
                selection.whenCount(lengthCondition);
                return (THIS) this;
            }

            /**
             * Defines a condition for array of found/suitable elements.
             *
             * @param condition a condition for entire array
             * @return self-reference
             */
            @Override
            default THIS returnOnCondition(Criteria<R[]> condition) {
                var casted = cast();
                var selection = getArraySelection(casted);
                selection.onCondition(condition);
                return (THIS) this;
            }
        }
    }

    /**
     * This class is designed to build and supply functions to get some desired object-value.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public abstract static class GetObjectStepSupplier<T, R, THIS extends GetObjectStepSupplier<T, R, THIS>>
        extends PrivateGetConditionalStepSupplier<T, R, T, R, THIS> {

        protected GetObjectStepSupplier(Function<T, R> originalFunction) {
            super(originalFunction, new FunctionFactory.ObjectFunctionFactory<>(), null);
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
    public abstract static class GetObjectChainedStepSupplier<T, R, M, THIS extends GetObjectChainedStepSupplier<T, R, M, THIS>>
        extends PrivateGetConditionalStepSupplier<T, R, M, R, THIS> {

        protected GetObjectChainedStepSupplier(Function<M, R> originalFunction) {
            super(originalFunction, new FunctionFactory.ObjectFunctionFactory<>(), null);
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

    private abstract static class PrivateGetItemStepSupplier<I, T, R, M, P, THIS extends PrivateGetItemStepSupplier<I, T, R, M, P, THIS>>
        extends PrivateGetConditionalStepSupplier<T, R, M, P, THIS> {

        protected final FunctionFactory.ItemFunctionFactory<M, I, R, P> itemFunctionFactory;

        protected <S extends I> PrivateGetItemStepSupplier(Function<M, S> stepFunction,
                                                           FunctionFactory.ItemFunctionFactory<M, I, R, P> functionFactory,
                                                           Captor<?, ?> captorForResultSelection) {
            super((Function<M, I>) stepFunction, functionFactory, captorForResultSelection);
            this.itemFunctionFactory = functionFactory;
        }

        @Override
        void fillSelectionParameters(Map<String, String> parameters) {
            ofNullable(itemFunctionFactory.getResultSelection())
                .ifPresent(rs -> parameters.putAll(rs.getParameters()));
        }

        @Override
        void ignoreSelection() {
            itemFunctionFactory.ignoreSelection();
        }

        interface SelectionOptionsForIterableItem<R, I extends Iterable<R>, THIS extends SelectionOptionsForIterableItem<R, I, THIS>>
            extends ReturnsOnCondition<I, THIS> {

            private PrivateGetItemStepSupplier<I, ?, R, ?, ?, ?> cast() {
                return (PrivateGetItemStepSupplier<I, ?, R, ?, ?, ?>) this;
            }

            private SelectionOfItem.SelectionOfIterableItem<R, I> getSelectionOfIterableItem(PrivateGetItemStepSupplier<I, ?, R, ?, ?, ?> casted) {
                return (SelectionOfItem.SelectionOfIterableItem<R, I>) ofNullable(casted.itemFunctionFactory.getResultSelection())
                    .orElseGet(() -> {
                        var s = (SelectionOfItem.SelectionOfIterableItem<R, I>) selectItemOfIterable();
                        casted.itemFunctionFactory.setResultSelection(s);
                        casted.selection = s;
                        return s;
                    });
            }

            /**
             * Defines index of the target element take from the iterable of found/suitable elements.
             *
             * @param size index of the target element
             * @return self-reference
             */
            default THIS returnItemOfIndex(int size) {
                var casted = cast();
                var selection = getSelectionOfIterableItem(casted);
                selection.index(size);
                return (THIS) this;
            }

            /**
             * Defines a size condition for entire iterable of found/suitable elements.
             *
             * @param sizeCondition a size condition for entire iterable
             * @return self-reference
             */
            default THIS returnIfEntireSize(ItemsCountCondition sizeCondition) {
                var casted = cast();
                var selection = getSelectionOfIterableItem(casted);
                selection.whenCount(sizeCondition);
                return (THIS) this;
            }

            /**
             * Defines a condition for iterable of found/suitable elements.
             *
             * @param condition a condition for entire iterable
             * @return self-reference
             */
            @Override
            default THIS returnOnCondition(Criteria<I> condition) {
                var casted = cast();
                var selection = getSelectionOfIterableItem(casted);
                selection.onCondition(condition);
                return (THIS) this;
            }
        }

        interface SelectionOptionsForArrayItem<R, THIS extends SelectionOptionsForArrayItem<R, THIS>>
            extends ReturnsOnCondition<R[], THIS> {

            private PrivateGetItemStepSupplier<R[], ?, R, ?, ?, ?> cast() {
                return (PrivateGetItemStepSupplier<R[], ?, R, ?, ?, ?>) this;
            }

            private SelectionOfItem.SelectionOfArrayItem<R> getSelectionOfArrayItem(PrivateGetItemStepSupplier<R[], ?, R, ?, ?, ?> casted) {
                return (SelectionOfItem.SelectionOfArrayItem<R>) ofNullable(casted.itemFunctionFactory.getResultSelection())
                    .orElseGet(() -> {
                        var s = (SelectionOfItem.SelectionOfArrayItem<R>) selectItemOfArray();
                        casted.itemFunctionFactory.setResultSelection(s);
                        casted.selection = s;
                        return s;
                    });
            }

            /**
             * Defines index of the target element take from the array of found/suitable elements.
             *
             * @param size index of the target element
             * @return self-reference
             */
            default THIS returnItemOfIndex(int size) {
                var casted = cast();
                var selection = getSelectionOfArrayItem(casted);
                selection.index(size);
                return (THIS) this;
            }

            /**
             * Defines a size condition for entire array of found/suitable elements.
             *
             * @param lengthCondition a length condition for entire array
             * @return self-reference
             */
            default THIS returnIfEntireLength(ItemsCountCondition lengthCondition) {
                var casted = cast();
                var selection = getSelectionOfArrayItem(casted);
                selection.whenCount(lengthCondition);
                return (THIS) this;
            }

            /**
             * Defines a condition for array of found/suitable elements.
             *
             * @param condition a condition for entire array
             * @return self-reference
             */
            @Override
            default THIS returnOnCondition(Criteria<R[]> condition) {
                var casted = cast();
                var selection = getSelectionOfArrayItem(casted);
                selection.onCondition(condition);
                return (THIS) this;
            }
        }
    }

    /**
     * This class is designed to build and supply functions to get desired value using some iterable.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value. Also it is a type of item from iterable.
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public abstract static class GetObjectFromIterableStepSupplier<T, R, THIS extends GetObjectFromIterableStepSupplier<T, R, THIS>>
        extends PrivateGetItemStepSupplier<Iterable<R>, T, R, T, R, THIS>
        implements PrivateGetItemStepSupplier.SelectionOptionsForIterableItem<R, Iterable<R>, THIS> {

        protected <S extends Iterable<R>> GetObjectFromIterableStepSupplier(Function<T, S> originalFunction) {
            super(originalFunction,
                new FunctionFactory.IterableItemFunctionFactory<>(),
                new IterableCaptor<>(new GotItems().toString()));
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
    public abstract static class GetObjectFromIterableChainedStepSupplier<T, R, M, THIS extends GetObjectFromIterableChainedStepSupplier<T, R, M, THIS>>
        extends PrivateGetItemStepSupplier<Iterable<R>, T, R, M, R, THIS>
        implements PrivateGetItemStepSupplier.SelectionOptionsForIterableItem<R, Iterable<R>, THIS> {

        protected <S extends Iterable<R>> GetObjectFromIterableChainedStepSupplier(Function<M, S> originalFunction) {
            super(originalFunction,
                new FunctionFactory.IterableItemFunctionFactory<>(),
                new IterableCaptor<>(new GotItems().toString()));
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
     * This class is designed to build and supply functions to get desired value using some array.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of result value. Also it is a type of item from array.
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public abstract static class GetObjectFromArrayStepSupplier<T, R, THIS extends GetObjectFromArrayStepSupplier<T, R, THIS>>
        extends PrivateGetItemStepSupplier<R[], T, R, T, R, THIS>
        implements PrivateGetItemStepSupplier.SelectionOptionsForArrayItem<R, THIS> {

        protected GetObjectFromArrayStepSupplier(Function<T, R[]> originalFunction) {
            super(originalFunction,
                new FunctionFactory.ArrayItemFunctionFactory<>(),
                new ArrayCaptor(new GotItems().toString()));
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
    public abstract static class GetObjectFromArrayChainedStepSupplier<T, R, M, THIS extends GetObjectFromArrayChainedStepSupplier<T, R, M, THIS>>
        extends PrivateGetItemStepSupplier<R[], T, R, M, R, THIS>
        implements PrivateGetItemStepSupplier.SelectionOptionsForArrayItem<R, THIS> {

        protected GetObjectFromArrayChainedStepSupplier(Function<M, R[]> originalFunction) {
            super(originalFunction,
                new FunctionFactory.ArrayItemFunctionFactory<>(),
                new ArrayCaptor(new GotItems().toString()));
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
     * This class is designed to build and supply functions to get some immutable list-value.
     *
     * @param <T>    is a type of input value
     * @param <S>    is a type of iterable
     * @param <R>    is a type of item from resulted iterable
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public abstract static class GetListStepSupplier<T, S extends Iterable<R>, R, THIS extends GetListStepSupplier<T, S, R, THIS>>
        extends PrivateGetIterableStepSupplier<T, List<R>, T, R, THIS>
        implements PrivateGetIterableStepSupplier.SelectionOptionsForList<R, THIS> {

        protected GetListStepSupplier(Function<T, S> originalFunction) {
            super(originalFunction,
                new FunctionFactory.ListFunctionFactory<>(),
                new CollectionCaptor(new GotItems().toString()));
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
    public abstract static class GetListChainedStepSupplier<T, S extends Iterable<R>, M, R, THIS extends GetListChainedStepSupplier<T, S, M, R, THIS>>
        extends PrivateGetIterableStepSupplier<T, List<R>, M, R, THIS>
        implements PrivateGetIterableStepSupplier.SelectionOptionsForList<R, THIS> {

        protected GetListChainedStepSupplier(Function<M, S> originalFunction) {
            super(originalFunction,
                new FunctionFactory.ListFunctionFactory<>(),
                new CollectionCaptor(new GotItems().toString()));
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
     * This class is designed to build and supply functions to get some desired array-value.
     *
     * @param <T>    is a type of input value
     * @param <R>    is a type of item from resulted array
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public abstract static class GetArrayStepSupplier<T, R, THIS extends GetArrayStepSupplier<T, R, THIS>>
        extends PrivateGetIterableStepSupplier<T, R[], T, R, THIS>
        implements PrivateGetIterableStepSupplier.SelectionOptionsForArray<R, THIS> {

        protected GetArrayStepSupplier(Function<T, R[]> originalFunction) {
            super(originalFunction,
                new FunctionFactory.ArrayFunctionFactory<>(),
                new ArrayCaptor(new GotItems().toString()));
            from(t -> t);
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

    /**
     * This class is designed to build and supply chained functions to get some desired array-value.
     *
     * @param <T>    is a type of input value
     * @param <M>    is a type of mediator value is used to get the result
     * @param <R>    is a type of item from resulted array
     * @param <THIS> this is the self-type. It is used for the method chaining.
     */
    public abstract static class GetArrayChainedStepSupplier<T, R, M, THIS extends GetArrayChainedStepSupplier<T, R, M, THIS>>
        extends PrivateGetIterableStepSupplier<T, R[], M, R, THIS>
        implements PrivateGetIterableStepSupplier.SelectionOptionsForArray<R, THIS> {

        protected GetArrayChainedStepSupplier(Function<M, R[]> originalFunction) {
            super(originalFunction,
                new FunctionFactory.ArrayFunctionFactory<>(),
                new ArrayCaptor(new GotItems().toString()));
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

    @Description("Got items")
    private static final class GotItems extends SelfDescribed {
    }
}
