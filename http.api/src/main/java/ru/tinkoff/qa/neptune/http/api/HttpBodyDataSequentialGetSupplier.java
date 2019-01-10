package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetObjectFromArray.getFromArray;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetObjectFromIterable.getFromIterable;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSubArray.getArray;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSubIterable.getIterable;
import static ru.tinkoff.qa.neptune.core.api.IsLoggableUtil.isLoggable;

/**
 * This class is designed to build chains of functions that get dody data from the response and convert it to the goal
 * objects/objects.
 * <p>Note</p>
 * It is designed to work with {@link HttpResponseSequentialGetSupplier} and with already received
 * {@link HttpResponse} as well. It is used as a precondition to get the result. If the instance of {@link HttpResponseSequentialGetSupplier}
 * is defined to find the response to get data from then all the conditions defined by
 * {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} become conditions to get the target
 * response and it is passed via {@link HttpResponseSequentialGetSupplier#conditionToReceiveDesiredResponse(Predicate)}.
 *
 * @param <S> is a type of body data from the response
 * @param <T> is a type of an object to get
 * @param <Q> is a type of an intermediate object. It is supposed that the result can not be got directly sometimes.
 *           {@code T} and {@code Q} can be the same if there is no intermediate object and the result is received directly.
 * @param <R> is a type of an object to be checked by the condition defined via{@link HttpResponseSequentialGetSupplier#conditionToReceiveDesiredResponse(Predicate)}.
 *           It may be some value got from the <Q>, e.g an item of a collection/array, some attribute of an object etc,
 *           It can be the object of types {@code Q} or even {@code T} as well. Then {@code Q} and {@code R},
 *           {@code T} and {@code Q} and {@code R} could be declared by the same type.
 */
@MakeCaptureOnFinishing(typeOfCapture = Object.class)
public abstract class HttpBodyDataSequentialGetSupplier<S, T, Q, R> extends SequentialGetStepSupplier<HttpStepPerformer, T, Q,
        HttpBodyDataSequentialGetSupplier<S, T, Q, R>> {

    private final Function<S, Q> transformingFunction;
    final String description;

    private final HttpResponseSequentialGetSupplier<S, ?> howToGetResponseFrom;
    private final HttpResponse<S> responseFrom;
    Predicate<R> conditionToGetData;
    Supplier<ResponseHasNoDesiredDataException> exceptionSupplier;

    private HttpBodyDataSequentialGetSupplier(String description,
                                              HttpResponseSequentialGetSupplier<S, ?> howToGetResponseFrom,
                                              Function<S, Q> transformingFunction) {
        checkArgument(!isBlank(description), "Description of a value to get should not be a null or empty string");
        checkArgument(nonNull(howToGetResponseFrom), "The way how to get desired response to " +
                "get data from should not be a null value");
        this.description = description;
        this.transformingFunction = s -> ofNullable(s).map(transformingFunction).orElse(null);
        this.howToGetResponseFrom = howToGetResponseFrom;
        responseFrom = null;
    }

    private HttpBodyDataSequentialGetSupplier(String description,
                                              HttpResponse<S> responseFrom,
                                              Function<S, Q> transformingFunction) {
        checkArgument(!isBlank(description), "Description of a value to get should not be a null or empty string");
        checkArgument(nonNull(responseFrom), "Response to get data from should not be a null value");
        this.description = description;
        this.transformingFunction = s -> ofNullable(s).map(transformingFunction).orElse(null);
        this.responseFrom = responseFrom;
        this.howToGetResponseFrom = null;
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to some object and then return it. It can use condition defined by
     * {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it can get the result as it is.
     * If there is no response that has desired data then the result is {@code null}.
     *
     * @param descriptionValue     is a string that describes the value to be returned
     * @param howToGetResponseFrom is a description of the response to get data from
     * @param whatToGet            is a function that converts body of the response to data for the further conversion
     * @param <S>                  is a type of the response body
     * @param <T>                  is a type of the expected result
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, T> HttpBodyDataSequentialGetSupplier<S, T, T, T> bodyDataOf(HttpResponseSequentialGetSupplier<S, ?> howToGetResponseFrom,
                                                                                  String descriptionValue,
                                                                                  Function<S, T> whatToGet) {
        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, howToGetResponseFrom, whatToGet) {
            @Override
            Predicate<T> getConvenientPredicate() {
                return conditionToGetData;
            }

            @Override
            protected Function<T, T> getEndFunction() {
                var func = (Function<T, T>) t -> t;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getSingle(description, func,
                                        conditionToGetData, true, errorSupplier))

                                .orElseGet(() -> getSingle(description, func,
                                        conditionToGetData, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getSingle(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, func)));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to some object and then return it. It can use condition defined by
     * {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it can get the result as it is.
     * If the response has no desired data then the result is {@code null}.
     *
     * @param descriptionValue is a string that describes the value to be returned
     * @param responseFrom     is the response to get data from
     * @param whatToGet        is a function that converts body of the response to data for the further conversion
     * @param <S>              is a type of the response body
     * @param <T>              is a type of the expected result
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, T> HttpBodyDataSequentialGetSupplier<S, T, T, T> bodyDataOf(HttpResponse<S> responseFrom,
                                                                                  String descriptionValue,
                                                                                  Function<S, T> whatToGet) {
        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, responseFrom, whatToGet) {
            @Override
            Predicate<T> getConvenientPredicate() {
                return conditionToGetData;
            }

            @Override
            protected Function<T, T> getEndFunction() {
                var func = (Function<T, T>) t -> t;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getSingle(description, func,
                                        conditionToGetData, true, errorSupplier))

                                .orElseGet(() -> getSingle(description, func,
                                        conditionToGetData, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getSingle(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, func)));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to {@link Iterable} of type {@code V} and then return it. It can use condition defined by
     * {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it can get the result as it is.
     * If there is no response that has desired data then the result is {@code null}
     *
     * @param descriptionValue     is a string that describes the value to be returned
     * @param howToGetResponseFrom is a description of the response to get data from
     * @param whatToGet            is a function that converts body of the response to {@link Iterable} for the further conversion
     * @param <S>                  is a type of the response body
     * @param <V>                  is a type of the resulted {@link Iterable}
     * @param <T>                  is a type of an item from {@link Iterable}
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, V extends Iterable<T>, T> HttpBodyDataSequentialGetSupplier<S, V, V, T> bodyIterableDataOf(HttpResponseSequentialGetSupplier<S, ?> howToGetResponseFrom,
                                                                                                                 String descriptionValue,
                                                                                                                 Function<S, V> whatToGet) {

        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, howToGetResponseFrom, whatToGet) {
            @Override
            Predicate<V> getConvenientPredicate() {
                return v -> stream(v.spliterator(), false).anyMatch(conditionToGetData);
            }

            @Override
            protected Function<V, V> getEndFunction() {
                var func = (Function<V, V>) ts -> ts;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getIterable(description, func,
                                        conditionToGetData, false, true, errorSupplier))

                                .orElseGet(() -> getIterable(description, func,
                                        conditionToGetData, false, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getIterable(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, func)));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to {@link Iterable} of type {@code V} and then return it. It can use condition defined by
     * {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it can get the result as it is.
     * If the response has no desired data then the result is an empty {@link Iterable} of type {@code V} or {@code null}
     *
     * @param descriptionValue is a string that describes the value to be returned
     * @param responseFrom     is the response to get data from
     * @param whatToGet        is a function that converts body of the response to {@link Iterable} for the further conversion
     * @param <S>              is a type of the response body
     * @param <V>              is a type of the resulted {@link Iterable}
     * @param <T>              is a type of an item from {@link Iterable}
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, V extends Iterable<T>, T> HttpBodyDataSequentialGetSupplier<S, V, V, T> bodyIterableDataOf(HttpResponse<S> responseFrom,
                                                                                                                 String descriptionValue,
                                                                                                                 Function<S, V> whatToGet) {

        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, responseFrom, whatToGet) {
            @Override
            Predicate<V> getConvenientPredicate() {
                return v -> stream(v.spliterator(), false).anyMatch(conditionToGetData);
            }

            @Override
            protected Function<V, V> getEndFunction() {
                var func = (Function<V, V>) ts -> ts;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getIterable(description, func,
                                        conditionToGetData, false, true, errorSupplier))

                                .orElseGet(() -> getIterable(description, func,
                                        conditionToGetData, false, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getIterable(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, func)));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to array of type {@code T} and then return it. It can use condition defined by
     * {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it can get the result as it is.
     * If there is no response that has desired data then the result is {@code null}
     *
     * @param descriptionValue     is a string that describes the value to be returned
     * @param howToGetResponseFrom is a description of the response to get data from
     * @param whatToGet            is a function that converts body of the response to array for the further conversion
     * @param <S>                  is a type of the response body
     * @param <T>                  is a type of an item from the resulted array
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, T> HttpBodyDataSequentialGetSupplier<S, T[], T[], T> bodyArrayDataOf(HttpResponseSequentialGetSupplier<S, ?> howToGetResponseFrom,
                                                                                           String descriptionValue,
                                                                                           Function<S, T[]> whatToGet) {
        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, howToGetResponseFrom, whatToGet) {
            @Override
            Predicate<T[]> getConvenientPredicate() {
                return ts -> Arrays.stream(ts).anyMatch(conditionToGetData);
            }

            @Override
            protected Function<T[], T[]> getEndFunction() {
                var func = (Function<T[], T[]>) ts -> ts;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getArray(description, func,
                                        conditionToGetData, false, true, errorSupplier))

                                .orElseGet(() -> getArray(description, func,
                                        conditionToGetData, false, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getArray(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, func)));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to array of type {@code T} and then return it. It can use condition defined by
     * {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it can get the result as it is.
     * If the response has no desired data then the result is an empty array of type {@code T} or {@code null}
     *
     * @param descriptionValue is a string that describes the value to be returned
     * @param responseFrom     is the response to get data from
     * @param whatToGet        is a function that converts body of the response to array for the further conversion
     * @param <S>              is a type of the response body
     * @param <T>              is a type of an item from the resulted array
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, T> HttpBodyDataSequentialGetSupplier<S, T[], T[], T> bodyArrayDataOf(HttpResponse<S> responseFrom,
                                                                                           String descriptionValue,
                                                                                           Function<S, T[]> whatToGet) {
        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, responseFrom, whatToGet) {
            @Override
            Predicate<T[]> getConvenientPredicate() {
                return ts -> Arrays.stream(ts).anyMatch(conditionToGetData);
            }

            @Override
            protected Function<T[], T[]> getEndFunction() {
                var func = (Function<T[], T[]>) ts -> ts;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getArray(description, func,
                                        conditionToGetData, false, true, errorSupplier))

                                .orElseGet(() -> getArray(description, func,
                                        conditionToGetData, false, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getArray(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, func)));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to {@link Iterable} of type {@code V} and then return a suitable/first found item. It can
     * use condition defined by {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it
     * can get the first found item. If there is no response that has desired data then the result is {@code null}.
     *
     * @param descriptionValue     is a string that describes the value to be returned
     * @param howToGetResponseFrom is a description of the response to get data from
     * @param whatToGet            is a function that converts body of the response to {@link Iterable} for the further conversion
     * @param <S>                  is a type of the response body
     * @param <V>                  is a type of the resulted {@link Iterable}
     * @param <T>                  is a type of an item from {@link Iterable}. It is the type of the result also
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, V extends Iterable<T>, T> HttpBodyDataSequentialGetSupplier<S, T, V, T> bodyDataFromIterable(HttpResponseSequentialGetSupplier<S, ?> howToGetResponseFrom,
                                                                                                                   String descriptionValue,
                                                                                                                   Function<S, V> whatToGet) {
        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, howToGetResponseFrom, whatToGet) {

            @Override
            Predicate<V> getConvenientPredicate() {
                return v -> stream(v.spliterator(), false).anyMatch(conditionToGetData);
            }

            @Override
            protected Function<V, T> getEndFunction() {
                var func = (Function<V, V>) ts -> ts;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getFromIterable(description, func,
                                        conditionToGetData, false, true, errorSupplier))

                                .orElseGet(() -> getFromIterable(description, func,
                                        conditionToGetData, false, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getFromIterable(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, v -> stream(v.spliterator(), false).findFirst().orElse(null))));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to {@link Iterable} of type {@code V} and then return a suitable/first found item. It can
     * use condition defined by {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it
     * can get the first found item. If the response has no desired data then the result is {@code null}.
     *
     * @param descriptionValue is a string that describes the value to be returned
     * @param responseFrom     is the response to get data from
     * @param whatToGet        is a function that converts body of the response to {@link Iterable} for the further conversion
     * @param <S>              is a type of the response body
     * @param <V>              is a type of the resulted {@link Iterable}
     * @param <T>              is a type of an item from {@link Iterable}. It is the type of the result also
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, V extends Iterable<T>, T> HttpBodyDataSequentialGetSupplier<S, T, V, T> bodyDataFromIterable(HttpResponse<S> responseFrom,
                                                                                                                   String descriptionValue,
                                                                                                                   Function<S, V> whatToGet) {
        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, responseFrom, whatToGet) {

            @Override
            Predicate<V> getConvenientPredicate() {
                return v -> stream(v.spliterator(), false).anyMatch(conditionToGetData);
            }

            @Override
            protected Function<V, T> getEndFunction() {
                var func = (Function<V, V>) ts -> ts;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getFromIterable(description, func,
                                        conditionToGetData, false, true, errorSupplier))

                                .orElseGet(() -> getFromIterable(description, func,
                                        conditionToGetData, false, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getFromIterable(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, v -> stream(v.spliterator(), false).findFirst().orElse(null))));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to array of type {@code T} and then return a suitable/first found item. It can
     * use condition defined by {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it
     * can get the first found item. If there is no response that has desired data then the result is {@code null}.
     *
     * @param descriptionValue     is a string that describes the value to be returned
     * @param howToGetResponseFrom is a description of the response to get data from
     * @param whatToGet            is a function that converts body of the response to array for the further conversion
     * @param <S>                  is a type of the response body
     * @param <T>                  is a type of an item from array. It is the type of the result also
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, T> HttpBodyDataSequentialGetSupplier<S, T, T[], T> bodyDataFromArray(HttpResponseSequentialGetSupplier<S, ?> howToGetResponseFrom,
                                                                                           String descriptionValue,
                                                                                           Function<S, T[]> whatToGet) {
        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, howToGetResponseFrom, whatToGet) {

            @Override
            Predicate<T[]> getConvenientPredicate() {
                return v -> Arrays.stream(v).anyMatch(conditionToGetData);
            }

            @Override
            protected Function<T[], T> getEndFunction() {
                var func = (Function<T[], T[]>) ts -> ts;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getFromArray(description, func,
                                        conditionToGetData, false, true, errorSupplier))

                                .orElseGet(() -> getFromArray(description, func,
                                        conditionToGetData, false, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getFromArray(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, ts -> Arrays.stream(ts).findFirst().orElse(null))));
            }
        };
    }

    /**
     * Creates an instance of {@link HttpBodyDataSequentialGetSupplier} that builds a chain of functions that covert
     * the body of response to array of type {@code T} and then return a suitable/first found item. It can
     * use condition defined by {@link HttpBodyDataSequentialGetSupplier#withConditionToGetData(Predicate)} or it
     * can get the first found item. If the response has no desired data then the result is {@code null}.
     *
     * @param descriptionValue is a string that describes the value to be returned
     * @param responseFrom     is the response to get data from
     * @param whatToGet        is a function that converts body of the response to array for the further conversion
     * @param <S>              is a type of the response body
     * @param <T>              is a type of an item from array. It is the type of the result also
     * @return an instance of {@link HttpBodyDataSequentialGetSupplier}
     */
    public static <S, T> HttpBodyDataSequentialGetSupplier<S, T, T[], T> bodyDataFromArray(HttpResponse<S> responseFrom,
                                                                                           String descriptionValue,
                                                                                           Function<S, T[]> whatToGet) {
        return new HttpBodyDataSequentialGetSupplier<>(descriptionValue, responseFrom, whatToGet) {

            @Override
            Predicate<T[]> getConvenientPredicate() {
                return v -> Arrays.stream(v).anyMatch(conditionToGetData);
            }

            @Override
            protected Function<T[], T> getEndFunction() {
                var func = (Function<T[], T[]>) ts -> ts;
                return ofNullable(conditionToGetData)
                        .map(tPredicate -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getFromArray(description, func,
                                        conditionToGetData, false, true, errorSupplier))

                                .orElseGet(() -> getFromArray(description, func,
                                        conditionToGetData, false, true)))

                        .orElseGet(() -> ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getFromArray(description, func, errorSupplier))

                                .orElseGet(() -> toGet(description, ts -> Arrays.stream(ts).findFirst().orElse(null))));
            }
        };
    }

    public Function<HttpStepPerformer, T> get() {
        checkArgument(nonNull(howToGetResponseFrom) || nonNull(responseFrom),
                "Response to get data from or the way how to get receive to get data from is not defined");

        ofNullable(howToGetResponseFrom)
                .ifPresentOrElse(sHttpResponseSequentialGetSupplier -> ofNullable(conditionToGetData)
                                .ifPresentOrElse(tPredicate -> {
                                    checkArgument(isLoggable(tPredicate), "Condition to get data from the " +
                                            "body of the response should be described");

                                    Predicate<HttpResponse<S>> condition =
                                            condition(format("Body of the response has data '%s' that meets the criteria '%s'",
                                                    description, tPredicate.toString()), sHttpResponse ->
                                                    getConvenientPredicate().test(transformingFunction
                                                            .apply(sHttpResponse.body())));

                                    var definedCondition = sHttpResponseSequentialGetSupplier
                                            .getCondition();

                                    sHttpResponseSequentialGetSupplier
                                            .conditionToReceiveDesiredResponse(ofNullable(definedCondition)
                                                    .map(httpResponsePredicate -> httpResponsePredicate.and(condition))
                                                    .orElse(condition));

                                    ofNullable(exceptionSupplier).ifPresent(errorSupplier ->
                                            sHttpResponseSequentialGetSupplier.toThrowIfNotReceived(() -> {
                                                var exception = errorSupplier.get();
                                                return new DesiredResponseHasNotBeenReceivedException(exception.getMessage(), exception);
                                            }));

                                    super.from(transformingFunction.compose(httpSteps ->
                                            ofNullable(httpSteps.get(sHttpResponseSequentialGetSupplier)).map(HttpResponse::body)
                                                    .orElse(null)));
                                }, () -> {
                                    ofNullable(exceptionSupplier).ifPresent(errorSupplier ->
                                            sHttpResponseSequentialGetSupplier.toThrowIfNotReceived(() -> {
                                                var exception = errorSupplier.get();
                                                return new DesiredResponseHasNotBeenReceivedException(exception.getMessage(), exception);
                                            }));
                                    super.from(transformingFunction.compose(httpSteps ->
                                            ofNullable(httpSteps.get(sHttpResponseSequentialGetSupplier)).map(HttpResponse::body)
                                                    .orElse(null)));
                                }),

                        () -> super.from(transformingFunction.compose(httpSteps -> responseFrom.body())));
        return super.get();
    }

    /**
     * This method defined the condition to get expected data from the response.
     * <p>NOTE!</p>
     * If an instance of {@link HttpResponseSequentialGetSupplier} is defined to find the response that is expected to
     * contain desired data then the given {@link Predicate} becomes a condition to get the response.
     *
     * @param conditionToGetData a condition to get expected data from the response.
     * @return self-reference
     */
    public HttpBodyDataSequentialGetSupplier<S, T, Q, R> withConditionToGetData(Predicate<R> conditionToGetData) {
        this.conditionToGetData = conditionToGetData;
        return this;
    }

    /**
     * This method allows to customize the exception to be thrown when there is no such response that contains desired
     * data.
     * <p>NOTE!</p>
     * If an instance of {@link HttpResponseSequentialGetSupplier} is defined to find the response that is expected to
     * contain desired data then {@link DesiredResponseHasNotBeenReceivedException} is thrown. Defined
     * {@link ResponseHasNoDesiredDataException} becomes a cause if the thrown exception.
     *
     * @param exceptionSupplier is a supplier of customized {@link ResponseHasNoDesiredDataException} to
     *                          be thrown when response has no desired data or there is no response that
     *                          is expected to contain desired data
     * @return self-reference
     */
    public HttpBodyDataSequentialGetSupplier<S, T, Q, R> toThrowIfNotReceived(Supplier<ResponseHasNoDesiredDataException> exceptionSupplier) {
        checkArgument(nonNull(exceptionSupplier), "Supplier of an exception should be defined");
        this.exceptionSupplier = exceptionSupplier;
        return this;
    }

    abstract Predicate<Q> getConvenientPredicate();

    @Override
    protected abstract Function<Q, T> getEndFunction();
}
