package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseSequentialGetSupplier.*;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseSequentialGetSupplier.addConditionArray;
import static ru.tinkoff.qa.neptune.http.api.properties.TimeToGetDesiredResponseProperty.DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.TimeToSleepProperty.SLEEP_RESPONSE_TIME_PROPERTY;

/**
 * Utility class to build functions that get data from http body response. These functions return iterables/arrays.
 * Built functions sends requests till desired response is received or waiting time is expired.
 */
@Deprecated(since = "0.11.4-ALPHA", forRemoval = true)
public final class HttpGetObjectsFromResponseBody {

    private HttpGetObjectsFromResponseBody() {
        super();
    }

    /**
     * Creates an instance of {@link ResponseGetHttpBodyIterableStepSupplier} that builds a chained function to covert
     * the body of the response into some {@link Iterable} and then return it.
     *
     * @param responseFrom     is the response to get data from
     * @param descriptionValue is a string that describes the value to be returned
     * @param whatToGet        is a function that converts body of the response to {@link Iterable}
     * @param <S>              is a type of the response body
     * @param <V>              is a type of the resulted {@link Iterable}
     * @param <T>              is a type of an item from {@link Iterable}.
     * @return an instance of {@link ResponseGetHttpBodyIterableStepSupplier}
     */
    public static <V extends Iterable<T>, S, T> ResponseGetHttpBodyIterableStepSupplier<V, S, T> bodyIterableDataOf(HttpResponse<S> responseFrom,
                                                                                                                    String descriptionValue,
                                                                                                                    Function<S, V> whatToGet) {
        return new ResponseGetHttpBodyIterableStepSupplier<>(descriptionValue, whatToGet, responseFrom);
    }

    /**
     * Creates an instance of {@link WaitingResponseGetHttpBodyIterableStepSupplier} that builds a chained function to covert
     * the body of the response into some {@link Iterable} and then return it.
     *
     * @param howToGetResponseFrom is a description of the response to get data from. It is supposed to get the response
     *                             firstly and then get desired value.
     * @param descriptionValue     is a string that describes the value to be returned
     * @param whatToGet            is a function that converts body of the response to {@link Iterable}
     * @param <S>                  is a type of the response body
     * @param <V>                  is a type of the resulted {@link Iterable}
     * @param <T>                  is a type of an item from {@link Iterable}.
     * @return an instance of {@link WaitingResponseGetHttpBodyIterableStepSupplier}
     */
    public static <V extends Iterable<T>, S, T> WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T> bodyIterableDataOf(HttpResponseSequentialGetSupplier<S> howToGetResponseFrom,
                                                                                                                           String descriptionValue,
                                                                                                                           Function<S, V> whatToGet) {
        return new WaitingResponseGetHttpBodyIterableStepSupplier<>(descriptionValue, whatToGet, howToGetResponseFrom);
    }

    /**
     * Creates an instance of {@link ResponseGetHttpBodyArrayStepSupplier} that builds a chained function to covert
     * the body of the response into some array and then return it.
     *
     * @param responseFrom     is the response to get data from
     * @param descriptionValue is a string that describes the value to be returned
     * @param whatToGet        is a function that converts body of the response to array
     * @param <S>              is a type of the response body
     * @param <T>              is a type of an item from the array
     * @return an instance of {@link ResponseGetHttpBodyArrayStepSupplier}
     */
    public static <S, T> ResponseGetHttpBodyArrayStepSupplier<T, S> bodyArrayDataOf(HttpResponse<S> responseFrom,
                                                                                    String descriptionValue,
                                                                                    Function<S, T[]> whatToGet) {
        return new ResponseGetHttpBodyArrayStepSupplier<>(descriptionValue, whatToGet, responseFrom);
    }

    /**
     * Creates an instance of {@link WaitingResponseGetHttpBodyArrayStepSupplier} that builds a chained function to covert
     * the body of the response into some array and then return it.
     *
     * @param howToGetResponseFrom is a description of the response to get data from. It is supposed to get the response
     *                             firstly and then get desired value.
     * @param descriptionValue     is a string that describes the value to be returned
     * @param whatToGet            is a function that converts body of the response to array
     * @param <S>                  is a type of the response body
     * @param <T>                  is a type of an item from the array
     * @return an instance of {@link WaitingResponseGetHttpBodyArrayStepSupplier}
     */
    public static <S, T> WaitingResponseGetHttpBodyArrayStepSupplier<T, S> bodyArrayDataOf(HttpResponseSequentialGetSupplier<S> howToGetResponseFrom,
                                                                                           String descriptionValue,
                                                                                           Function<S, T[]> whatToGet) {
        return new WaitingResponseGetHttpBodyArrayStepSupplier<>(descriptionValue, whatToGet, howToGetResponseFrom);
    }


    @MakeCaptureOnFinishing(typeOfCapture = Object.class)
    private static class GetHttpBodyIterableStepSupplier<V extends Iterable<T>, S, T, R extends GetHttpBodyIterableStepSupplier<V, S, T, R>> extends SequentialGetStepSupplier
            .GetIterableChainedStepSupplier<HttpStepContext, V, HttpResponse<S>, T, R> {

        final Function<S, ? extends Iterable<T>> getFromBody;

        private GetHttpBodyIterableStepSupplier(String description, Function<S, V> getFromBody) {
            super(description, sHttpResponse -> ofNullable(sHttpResponse).map(response -> {
                try {
                    return getFromBody.apply(response.body());
                } catch (Throwable t) {
                    return null;
                }
            }).orElse(null));
            checkArgument(nonNull(getFromBody), "Function to convert http response body to a target object is not defined");
            this.getFromBody = getFromBody;
        }

        @Override
        public R criteria(Criteria<? super T> condition) {
            return super.criteria(condition);
        }

        @Override
        public R criteria(String conditionDescription, Predicate<? super T> condition) {
            return super.criteria(conditionDescription, condition);
        }
    }

    /**
     * Builds function that returns an {@link Iterable} from the response body. It uses the response that has been received.
     * It is expected that response body may contain an {@link Iterable} of target objects.
     *
     * @param <V> is a type of an {@link Iterable} to return
     * @param <S> is a type of the response body
     * @param <T> is a type of an item from {@link Iterable}
     */
    public static class ResponseGetHttpBodyIterableStepSupplier<V extends Iterable<T>, S, T> extends
            GetHttpBodyIterableStepSupplier<V, S, T, ResponseGetHttpBodyIterableStepSupplier<V, S, T>> {

        private ResponseGetHttpBodyIterableStepSupplier(String description, Function<S, V> getFromBody,
                                                        HttpResponse<S> response) {
            super(description, getFromBody);
            from(response);
        }

        /**
         * This methods says that an exception should be thrown when there is no desired data taken from the http response.
         *
         * @param exceptionSupplier is a suppler of exception to be thrown.
         * @return self-reference.
         */
        public ResponseGetHttpBodyIterableStepSupplier<V, S, T> throwWhenResultEmpty(Supplier<ResponseHasNoDesiredDataException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }

    /**
     * Builds function that returns an {@link Iterable} from the response body. It waits for response that contains
     * desired data. It is expected that response body may contain an {@link Iterable} of target objects.
     *
     * @param <V> is a type of an {@link Iterable} to return
     * @param <S> is a type of the response body
     * @param <T> is a type of an item from {@link Iterable}
     */
    public static class WaitingResponseGetHttpBodyIterableStepSupplier<V extends Iterable<T>, S, T>
            extends GetHttpBodyIterableStepSupplier<V, S, T, WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T>> {

        private final HttpResponseSequentialGetSupplier<S> response;

        private WaitingResponseGetHttpBodyIterableStepSupplier(String description, Function<S, V> getFromBody,
                                                               HttpResponseSequentialGetSupplier<S> response) {
            super(description, getFromBody);
            this.response = response.clone();
            this.response.timeOut(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get());
            this.response.pollingInterval(SLEEP_RESPONSE_TIME_PROPERTY.get());
        }

        @SuppressWarnings("unchecked")
        public Function<HttpStepContext, V> get() {
            var criteria = getCriteria();
            var thisReference = this;
            var responseClone = this.response.clone();
            from(responseClone);
            ofNullable(criteria).ifPresentOrElse(tPredicate ->
                            addConditionIterable(responseClone,
                                    getFromBody,
                                    condition(format("Contains %s", thisReference.toString()),
                                            t -> nonNull(t) && tPredicate.test((T) t))),
                    () -> addConditionIterable(responseClone, getFromBody, condition(format("Contains %s", thisReference.toString()),
                            Objects::nonNull)));
            return super.get();
        }

        /**
         * The time to wait for the response that contains desired values.
         *
         * @param timeOut is a time duration of the waiting for the response that contains the desired value.
         * @return self-reference
         */
        @Override
        public WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T> timeOut(Duration timeOut) {
            response.timeOut(timeOut);
            return this;
        }

        @Override
        public WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T> pollingInterval(Duration pollingTime) {
            response.pollingInterval(pollingTime);
            return this;
        }

        /**
         * Defines the criteria that desired values should meet. Also it defines criteria to check body of the received
         * response.
         *
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        @Override
        public WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T> criteria(Criteria<? super T> criteria) {
            return super.criteria(criteria);
        }

        /**
         * Defines the criteria that desired values should meet. Also it defines criteria to check body of the received
         * response.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        @Override
        public WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T> criteria(String conditionDescription,
                                                                                Predicate<? super T> condition) {
            return super.criteria(conditionDescription, condition);
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        public WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T> criteriaOfResponse(Criteria<? super HttpResponse<S>> criteria) {
            response.criteria(criteria);
            return this;
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        public WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T> criteriaOfResponse(String conditionDescription,
                                                                                          Predicate<? super HttpResponse<S>> condition) {
            response.criteria(conditionDescription, condition);
            return this;
        }

        /**
         * This methods says that an exception should be thrown when there is no response that contains desired data.
         *
         * @param exceptionSupplier is a suppler of exception to be thrown.
         * @return self-reference.
         */
        public WaitingResponseGetHttpBodyIterableStepSupplier<V, S, T> throwWhenResultEmpty(Supplier<DesiredResponseHasNotBeenReceivedException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }

    @MakeCaptureOnFinishing(typeOfCapture = Object.class)
    private static class GetHttpBodyArrayStepSupplier<T, S, R extends GetHttpBodyArrayStepSupplier<T, S, R>> extends SequentialGetStepSupplier
            .GetArrayChainedStepSupplier<HttpStepContext, HttpResponse<S>, T, R> {

        final Function<S, T[]> getFromBody;

        private GetHttpBodyArrayStepSupplier(String description, Function<S, T[]> getFromBody) {
            super(description, sHttpResponse -> ofNullable(sHttpResponse).map(response -> {
                try {
                    return getFromBody.apply(response.body());
                } catch (Throwable t) {
                    return null;
                }
            }).orElse(null));
            checkArgument(nonNull(getFromBody), "Function to convert http response body to a target object is not defined");
            this.getFromBody = getFromBody;
        }

        @Override
        public R criteria(Criteria<? super T> condition) {
            return super.criteria(condition);
        }

        @Override
        public R criteria(String conditionDescription, Predicate<? super T> condition) {
            return super.criteria(conditionDescription, condition);
        }
    }

    /**
     * Builds function that returns an array from the response body. It uses the response that has been received.
     * It is expected that response body may contain an array of target objects.
     *
     * @param <T> is a type of an item from resulted array
     * @param <S> is a type of the response body
     */
    public static class ResponseGetHttpBodyArrayStepSupplier<T, S> extends
            GetHttpBodyArrayStepSupplier<T, S, ResponseGetHttpBodyArrayStepSupplier<T, S>> {

        private ResponseGetHttpBodyArrayStepSupplier(String description, Function<S, T[]> getFromBody,
                                                     HttpResponse<S> response) {
            super(description, getFromBody);
            from(response);
        }

        /**
         * This methods says that an exception should be thrown when there is no desired data taken from the http response.
         *
         * @param exceptionSupplier is a suppler of exception to be thrown.
         * @return self-reference.
         */
        public ResponseGetHttpBodyArrayStepSupplier<T, S> throwWhenResultEmpty(Supplier<ResponseHasNoDesiredDataException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }

    /**
     * Builds function that returns a single object from the response body. It waits for response that contains
     * desired data. It is expected that response body may contain an array of target objects.
     *
     * @param <T> is a type of an item from resulted array
     * @param <S> is a type of the response body
     */
    public static class WaitingResponseGetHttpBodyArrayStepSupplier<T, S>
            extends GetHttpBodyArrayStepSupplier<T, S, WaitingResponseGetHttpBodyArrayStepSupplier<T, S>> {

        private final HttpResponseSequentialGetSupplier<S> response;

        private WaitingResponseGetHttpBodyArrayStepSupplier(String description, Function<S, T[]> getFromBody,
                                                            HttpResponseSequentialGetSupplier<S> response) {
            super(description, getFromBody);
            this.response = response.clone();
            this.response.timeOut(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get());
            this.response.pollingInterval(SLEEP_RESPONSE_TIME_PROPERTY.get());
        }

        @SuppressWarnings("unchecked")
        public Function<HttpStepContext, T[]> get() {
            var criteria = getCriteria();
            var thisReference = this;
            var responseClone = this.response.clone();
            from(responseClone);
            ofNullable(criteria).ifPresentOrElse(tPredicate ->
                            addConditionArray(responseClone,
                                    getFromBody,
                                    condition(format("Contains %s", thisReference.toString()),
                                            t -> nonNull(t) && tPredicate.test((T) t))),
                    () -> addConditionArray(responseClone, getFromBody, condition(format("Contains %s", thisReference.toString()),
                            Objects::nonNull)));
            return super.get();
        }

        /**
         * The time to wait for the response that contains desired values.
         *
         * @param timeOut is a time duration of the waiting for the response that contains the desired value.
         * @return self-reference
         */
        @Override
        public WaitingResponseGetHttpBodyArrayStepSupplier<T, S> timeOut(Duration timeOut) {
            this.response.timeOut(timeOut);
            return this;
        }

        @Override
        public WaitingResponseGetHttpBodyArrayStepSupplier<T, S> pollingInterval(Duration pollingTime) {
            response.pollingInterval(pollingTime);
            return this;
        }

        /**
         * Defines the criteria that desired values should meet. Also it defines criteria to check body of the received
         * response.
         *
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        @Override
        public WaitingResponseGetHttpBodyArrayStepSupplier<T, S> criteria(Criteria<? super T> condition) {
            return super.criteria(condition);
        }

        /**
         * Defines the criteria that desired values should meet. Also it defines criteria to check body of the received
         * response.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        @Override
        public WaitingResponseGetHttpBodyArrayStepSupplier<T, S> criteria(String conditionDescription,
                                                                          Predicate<? super T> condition) {
            return super.criteria(conditionDescription, condition);
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        public WaitingResponseGetHttpBodyArrayStepSupplier<T, S> criteriaOfResponse(Criteria<? super HttpResponse<S>> condition) {
            response.criteria(condition);
            return this;
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        public WaitingResponseGetHttpBodyArrayStepSupplier<T, S> criteriaOfResponse(String conditionDescription,
                                                                                    Predicate<? super HttpResponse<S>> condition) {
            response.criteria(conditionDescription, condition);
            return this;
        }

        /**
         * This methods says that an exception should be thrown when there is no response that contains desired data.
         *
         * @param exceptionSupplier is a suppler of exception to be thrown.
         * @return self-reference.
         */
        public WaitingResponseGetHttpBodyArrayStepSupplier<T, S> throwWhenResultEmpty(Supplier<DesiredResponseHasNotBeenReceivedException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }
}
