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
import static ru.tinkoff.qa.neptune.http.api.properties.TimeToGetDesiredResponseProperty.DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.TimeToSleepProperty.SLEEP_RESPONSE_TIME_PROPERTY;

/**
 * Utility class to build functions that get data from http body response. These functions return a single object.
 * Built functions sends requests till desired response is received or waiting time is expired.
 */
@Deprecated(since = "0.11.4-ALPHA", forRemoval = true)
public final class HttpGetObjectFromResponseBody {

    private HttpGetObjectFromResponseBody() {
        super();
    }

    /**
     * Creates an instance of {@link ResponseGetHttpBodyObjectStepSupplier} that builds a chained function to covert
     * the body of the response into some object and then return it.
     *
     * @param responseFrom     is the response to get data from
     * @param descriptionValue is a string that describes the value to be returned
     * @param whatToGet        is a function that converts body of the response to data for the further conversion
     * @param <S>              is a type of the response body
     * @param <T>              is a type of the expected result
     * @return an instance of {@link ResponseGetHttpBodyObjectStepSupplier}
     */
    public static <T, S> ResponseGetHttpBodyObjectStepSupplier<T, S> bodyDataOf(HttpResponse<S> responseFrom,
                                                                                String descriptionValue,
                                                                                Function<S, T> whatToGet) {
        return new ResponseGetHttpBodyObjectStepSupplier<>(descriptionValue, whatToGet, responseFrom);
    }

    /**
     * Creates an instance of {@link WaitingResponseGetHttpBodyObjectStepSupplier} that builds a chained function to covert
     * the body of the response into some object and then return it.
     *
     * @param howToGetResponseFrom is a description of the response to get data from. It is supposed to get the response
     *                             firstly and then get desired value.
     * @param descriptionValue     is a string that describes the value to be returned
     * @param whatToGet            is a function that converts body of the response to data for the further conversion
     * @param <S>                  is a type of the response body
     * @param <T>                  is a type of the expected result
     * @return an instance of {@link WaitingResponseGetHttpBodyObjectStepSupplier}
     */
    public static <S, T> WaitingResponseGetHttpBodyObjectStepSupplier<T, S> bodyDataOf(HttpResponseSequentialGetSupplier<S> howToGetResponseFrom,
                                                                                       String descriptionValue,
                                                                                       Function<S, T> whatToGet) {
        return new WaitingResponseGetHttpBodyObjectStepSupplier<>(descriptionValue, whatToGet, howToGetResponseFrom);
    }

    /**
     * Creates an instance of {@link ResponseGetHttpBodyObjectFromIterableStepSupplier} that builds a chained function to covert
     * the body of the response into some object and then return it.
     *
     * @param responseFrom is the response to get data from
     * @param descriptionValue is a string that describes the value to be returned
     * @param whatToGet is a function that converts body of the response to {@link Iterable}
     * @param <S> is a type of the response body
     * @param <V> is a type of the resulted {@link Iterable}
     * @param <T> is a type of an item from {@link Iterable}. It is the type of the result also
     * @return an instance of {@link ResponseGetHttpBodyObjectFromIterableStepSupplier}
     */
    public static <S, V extends Iterable<T>, T> ResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> bodyDataFromIterable(HttpResponse<S> responseFrom,
                                                                                                                             String descriptionValue,
                                                                                                                             Function<S, V> whatToGet) {
        return new ResponseGetHttpBodyObjectFromIterableStepSupplier<>(descriptionValue, whatToGet, responseFrom);
    }

    /**
     * Creates an instance of {@link WaitingResponseGetHttpBodyObjectFromIterableStepSupplier} that builds a chained function to covert
     * the body of the response into some object and then return it.
     *
     * @param howToGetResponseFrom is a description of the response to get data from. It is supposed to get the response
     *                             firstly and then get desired value.
     * @param descriptionValue is a string that describes the value to be returned
     * @param whatToGet is a function that converts body of the response to {@link Iterable}
     * @param <S> is a type of the response body
     * @param <V> is a type of the resulted {@link Iterable}
     * @param <T> is a type of an item from {@link Iterable}. It is the type of the result also
     * @return an instance of {@link WaitingResponseGetHttpBodyObjectFromIterableStepSupplier}
     */
    public static <S, V extends Iterable<T>, T> WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> bodyDataFromIterable(HttpResponseSequentialGetSupplier<S> howToGetResponseFrom,
                                                                                                                                    String descriptionValue,
                                                                                                                                    Function<S, V> whatToGet) {
        return new WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<>(descriptionValue, whatToGet, howToGetResponseFrom);
    }

    /**
     * Creates an instance of {@link ResponseGetHttpBodyObjectFromArrayStepSupplier} that builds a chained function to covert
     * the body of the response into some object and then return it.
     *
     * @param responseFrom is the response to get data from
     * @param descriptionValue is a string that describes the value to be returned
     * @param whatToGet is a function that converts body of the response to array
     * @param <S> is a type of the response body
     * @param <T> is a type of an item from the array
     * @return an instance of {@link ResponseGetHttpBodyObjectFromArrayStepSupplier}
     */
    public static <S, T> ResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> bodyDataFromArray(HttpResponse<S> responseFrom,
                                                                                                   String descriptionValue,
                                                                                                   Function<S, T[]> whatToGet) {
        return new ResponseGetHttpBodyObjectFromArrayStepSupplier<>(descriptionValue, whatToGet, responseFrom);
    }

    /**
     * Creates an instance of {@link WaitingResponseGetHttpBodyObjectFromArrayStepSupplier} that builds a chained function to covert
     * the body of the response into some object and then return it.
     *
     * @param howToGetResponseFrom is a description of the response to get data from. It is supposed to get the response
     *                             firstly and then get desired value.
     * @param descriptionValue is a string that describes the value to be returned
     * @param whatToGet is a function that converts body of the response to array
     * @param <S> is a type of the response body
     * @param <T> is a type of an item from the array
     * @return an instance of {@link WaitingResponseGetHttpBodyObjectFromArrayStepSupplier}
     */
    public static <S, T> WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> bodyDataFromArray(HttpResponseSequentialGetSupplier<S> howToGetResponseFrom,
                                                                                                       String descriptionValue,
                                                                                                       Function<S, T[]> whatToGet) {
        return new WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<>(descriptionValue, whatToGet, howToGetResponseFrom);
    }

    @MakeCaptureOnFinishing(typeOfCapture = Object.class)
    private static class GetHttpBodyObjectStepSupplier<T, S, R extends GetHttpBodyObjectStepSupplier<T, S, R>> extends SequentialGetStepSupplier
            .GetObjectChainedStepSupplier<HttpStepContext, T, HttpResponse<S>, R> {

        final Function<S, T> getFromBody;

        private GetHttpBodyObjectStepSupplier(String description, Function<S, T> getFromBody) {
            super(description, sHttpResponse -> ofNullable(sHttpResponse).map(response -> {
                try {
                    return getFromBody.apply(response.body());
                }
                catch (Throwable t) {
                    return null;
                }
            }).orElse(null));
            checkArgument(nonNull(getFromBody), "Function to convert http response body to a target object is not defined");
            this.getFromBody = getFromBody;
        }


        @Override
        public R criteria(Criteria<? super T> criteria) {
            return super.criteria(criteria);
        }

        @Override
        public R criteria(String conditionDescription, Predicate<? super T> condition) {
            return super.criteria(conditionDescription, condition);
        }
    }

    /**
     * Builds function that returns a single object from thee response body. It uses the response that has been received.
     *
     * @param <T> is a type of a value to return
     * @param <S> is a type of the response body
     */
    public static class ResponseGetHttpBodyObjectStepSupplier<T, S> extends
            GetHttpBodyObjectStepSupplier<T, S, ResponseGetHttpBodyObjectStepSupplier<T, S>> {

        private ResponseGetHttpBodyObjectStepSupplier(String description, Function<S, T> getFromBody,
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
        public ResponseGetHttpBodyObjectStepSupplier<T, S> throwWhenResultEmpty(Supplier<ResponseHasNoDesiredDataException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }

    /**
     * Builds function that returns a single object from thee response body. It waits for response that contains
     * desired data.
     *
     * @param <T> is a type of a value to return
     * @param <S> is a type of the response body
     */
    public static class WaitingResponseGetHttpBodyObjectStepSupplier<T, S>
            extends GetHttpBodyObjectStepSupplier<T, S, WaitingResponseGetHttpBodyObjectStepSupplier<T, S>> {

        private final HttpResponseSequentialGetSupplier<S> response;

        private WaitingResponseGetHttpBodyObjectStepSupplier(String description, Function<S, T> getFromBody,
                                                             HttpResponseSequentialGetSupplier<S> response) {
            super(description, getFromBody);
            this.response = response.clone();
            this.response.timeOut(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get());
            this.response.pollingInterval(SLEEP_RESPONSE_TIME_PROPERTY.get());
        }

        public Function<HttpStepContext, T> get() {
            var criteria = getCriteria();
            var thisReference = this;
            var responseClone = this.response.clone();
            from(responseClone);
            ofNullable(criteria).ifPresentOrElse(tPredicate ->
                            addCondition(responseClone,
                                    getFromBody,
                                    condition(format("Contains %s", thisReference.toString()),
                                            t -> nonNull(t) && tPredicate.test((T) t))),
                    () -> addCondition(responseClone, getFromBody, condition(format("Contains %s", thisReference.toString()),
                            Objects::nonNull)));
            return super.get();
        }

        /**
         * The time to wait for the response that contains the desired value.
         *
         * @param timeOut is a time duration of the waiting for the response that contains the desired value.
         * @return self-reference
         */
        @Override
        public WaitingResponseGetHttpBodyObjectStepSupplier<T, S> timeOut(Duration timeOut) {
            response.timeOut(timeOut);
            return this;
        }

        @Override
        public WaitingResponseGetHttpBodyObjectStepSupplier<T, S> pollingInterval(Duration pollingTime) {
            response.pollingInterval(pollingTime);
            return this;
        }

        /**
         * Defines the criteria that the desired value should meet. Also it defines criteria to check body of the received
         * response.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        @Override
        public WaitingResponseGetHttpBodyObjectStepSupplier<T, S> criteria(String conditionDescription,
                                                                           Predicate<? super T> condition) {
            return super.criteria(conditionDescription, condition);
        }

        /**
         * Defines the criteria that the desired value should meet. Also it defines criteria to check body of the received
         * response.
         *
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        @Override
        public WaitingResponseGetHttpBodyObjectStepSupplier<T, S> criteria(Criteria<? super T> criteria) {
            return super.criteria(criteria);
        }


        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        public WaitingResponseGetHttpBodyObjectStepSupplier<T, S> criteriaOfResponse(Criteria<? super HttpResponse<S>> condition) {
            response.criteria(condition);
            return this;
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        public WaitingResponseGetHttpBodyObjectStepSupplier<T, S> criteriaOfResponse(String conditionDescription,
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
        public WaitingResponseGetHttpBodyObjectStepSupplier<T, S> throwWhenResultEmpty(Supplier<DesiredResponseHasNotBeenReceivedException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }

    @MakeCaptureOnFinishing(typeOfCapture = Object.class)
    private static class GetHttpBodyObjectFromIterableStepSupplier<T, S, R extends GetHttpBodyObjectFromIterableStepSupplier<T, S, R>> extends SequentialGetStepSupplier
            .GetObjectFromIterableChainedStepSupplier<HttpStepContext, T, HttpResponse<S>, R> {

        final Function<S, ? extends Iterable<T>> getFromBody;

        private <Q extends Iterable<T>> GetHttpBodyObjectFromIterableStepSupplier(String description, Function<S, Q> getFromBody) {
            super(description, sHttpResponse -> ofNullable(sHttpResponse).map(httpResponse -> {
                try {
                    return getFromBody.apply(httpResponse.body());
                }
                catch (Throwable t) {
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
     * Builds function that returns a single object from thee response body. It uses the response that has been received.
     * It is expected that response body may contain an {@link Iterable} of target objects.
     *
     * @param <T> is a type of a value to return
     * @param <S> is a type of the response body
     */
    public static class ResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> extends
            GetHttpBodyObjectFromIterableStepSupplier<T, S, ResponseGetHttpBodyObjectFromIterableStepSupplier<T, S>> {

        private <Q extends Iterable<T>> ResponseGetHttpBodyObjectFromIterableStepSupplier(String description, Function<S, Q> getFromBody,
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
        public ResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> throwWhenResultEmpty(Supplier<ResponseHasNoDesiredDataException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }

    }

    /**
     * Builds function that returns a single object from thee response body. It waits for response that contains
     * desired data. It is expected that response body may contain an {@link Iterable} of target objects.
     *
     * @param <T> is a type of a value to return
     * @param <S> is a type of the response body
     */
    public static class WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S>
            extends GetHttpBodyObjectFromIterableStepSupplier<T, S, WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S>> {

        private final HttpResponseSequentialGetSupplier<S> response;

        private <Q extends Iterable<T>> WaitingResponseGetHttpBodyObjectFromIterableStepSupplier(String description, Function<S, Q> getFromBody,
                                                                                                 HttpResponseSequentialGetSupplier<S> response) {
            super(description, getFromBody);
            this.response = response.clone();
            this.response.timeOut(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get());
            this.response.pollingInterval(SLEEP_RESPONSE_TIME_PROPERTY.get());
        }

        public Function<HttpStepContext, T> get() {
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
        public WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> timeOut(Duration timeOut) {
            response.timeOut(timeOut);
            return this;
        }

        @Override
        public WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> pollingInterval(Duration pollingTime) {
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
        public WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> criteria(Criteria<? super T> condition) {
            return super.criteria(condition);
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        public WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> criteriaOfResponse(Criteria<? super HttpResponse<S>> condition) {
            response.criteria(condition);
            return this;
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        public WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S> criteriaOfResponse(String conditionDescription,
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
        public WaitingResponseGetHttpBodyObjectFromIterableStepSupplier<T, S>  throwWhenResultEmpty(Supplier<DesiredResponseHasNotBeenReceivedException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }

    @MakeCaptureOnFinishing(typeOfCapture = Object.class)
    private static class GetHttpBodyObjectFromArrayStepSupplier<T, S, R extends GetHttpBodyObjectFromArrayStepSupplier<T, S, R>> extends SequentialGetStepSupplier
            .GetObjectFromArrayChainedStepSupplier<HttpStepContext, T, HttpResponse<S>, R> {

        final Function<S, T[]> getFromBody;

        private GetHttpBodyObjectFromArrayStepSupplier(String description, Function<S, T[]> getFromBody) {
            super(description, sHttpResponse -> ofNullable(sHttpResponse).map(response -> {
                try {
                    return getFromBody.apply(response.body());
                }
                catch (Throwable t) {
                    return null;
                }
            }).orElse(null));
            checkArgument(nonNull(getFromBody), "Function to convert http response body to a target object is not defined");
            this.getFromBody = getFromBody;
        }

        @Override
        public R criteria(Criteria<? super T> criteria) {
            return super.criteria(criteria);
        }

        @Override
        public R criteria(String conditionDescription, Predicate<? super T> condition) {
            return super.criteria(conditionDescription, condition);
        }
    }

    /**
     * Builds function that returns a single object from thee response body. It uses the response that has been received.
     * It is expected that response body may contain an array of target objects.
     *
     * @param <T> is a type of a value to return
     * @param <S> is a type of the response body
     */
    public static class ResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> extends
            GetHttpBodyObjectFromArrayStepSupplier<T, S, ResponseGetHttpBodyObjectFromArrayStepSupplier<T, S>> {

        private ResponseGetHttpBodyObjectFromArrayStepSupplier(String description, Function<S, T[]> getFromBody,
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
        public ResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> throwWhenResultEmpty(Supplier<ResponseHasNoDesiredDataException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }

    /**
     * Builds function that returns a single object from thee response body. It waits for response that contains
     * desired data. It is expected that response body may contain an array of target objects.
     *
     * @param <T> is a type of a value to return
     * @param <S> is a type of the response body
     */
    public static class WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S>
            extends GetHttpBodyObjectFromArrayStepSupplier<T, S, WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S>> {

        private final HttpResponseSequentialGetSupplier<S> response;

        private WaitingResponseGetHttpBodyObjectFromArrayStepSupplier(String description, Function<S, T[]> getFromBody,
                                                                      HttpResponseSequentialGetSupplier<S> response) {
            super(description, getFromBody);
            this.response = response.clone();
            this.response.timeOut(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get());
            this.response.pollingInterval(SLEEP_RESPONSE_TIME_PROPERTY.get());
        }

        public Function<HttpStepContext, T> get() {
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
        public WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> timeOut(Duration timeOut) {
            response.timeOut(timeOut);
            return this;
        }

        @Override
        public WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> pollingInterval(Duration pollingTime) {
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
        public WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> criteria(Criteria<? super T> condition) {
            return super.criteria(condition);
        }

        /**
         * Defines the criteria that desired values should meet. Also it defines criteria to check body of the received
         * response.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        @Override
        public WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> criteria(String conditionDescription,
                                                                                    Predicate<? super T> condition) {
            return super.criteria(conditionDescription, condition);
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(Criteria)
         */
        public WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> criteriaOfResponse(Criteria<? super HttpResponse<S>> condition) {
            response.criteria(condition);
            return this;
        }

        /**
         * Defines the criteria that response to get data from should meet.
         *
         * @see SequentialGetStepSupplier#criteria(String, Predicate)
         */
        public WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> criteriaOfResponse(String conditionDescription,
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
        public WaitingResponseGetHttpBodyObjectFromArrayStepSupplier<T, S> throwWhenResultEmpty(Supplier<DesiredResponseHasNotBeenReceivedException> exceptionSupplier) {
            return super.throwOnEmptyResult(exceptionSupplier);
        }
    }
}
