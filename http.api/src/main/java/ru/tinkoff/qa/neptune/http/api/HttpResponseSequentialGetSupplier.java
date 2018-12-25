package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.http.api.properties.TimeToGetDesiredResponseProperty.DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY;

/**
 * This class is designed to build chains of functions that get a response when a request is sent.
 * @param <T> is a type of body of the received response
 */
@SuppressWarnings("unchecked")
public abstract class HttpResponseSequentialGetSupplier<T, R extends HttpResponseSequentialGetSupplier<T, R>> extends SequentialGetStepSupplier<HttpSteps, HttpResponse<T>,
        HowToGetResponse, R> implements Cloneable {

    private final HttpRequestGetSupplier request;
    private final HttpResponse.BodyHandler<T> handler;
    private final Function<HowToGetResponse, HttpResponse<T>> getResponse = new Function<>() {
        @Override
        public HttpResponse<T> apply(HowToGetResponse howToGetResponse) {
            try {
                return howToGetResponse.getClient().send(howToGetResponse.getRequest(), handler);
            } catch (Throwable e) {
                throw new DesiredResponseHasNotBeenReceivedException("Response has not been received", e);
            }
        }
    };

    private Predicate<HttpResponse<T>> condition;
    private Duration timeToWaitForCondition;
    private Supplier<DesiredResponseHasNotBeenReceivedException> exceptionSupplier;

    private HttpResponseSequentialGetSupplier(HttpRequestGetSupplier request, HttpResponse.BodyHandler<T> handler) {
        this.request = request;
        this.handler = handler;
    }

    /**
     * Builds a function to get a response of the given GET-request to be sent.
     *
     * @param request is a description of GET-request to get response of
     * @param handler to read the body of resulted response
     * @param <T>     is a type of the read body of resulted response
     * @return an instance of {@link HttpResponseOnConditionSequentialGetSupplier}
     */
    public static <T> HttpResponseOnConditionSequentialGetSupplier<T> responseOf(HttpRequestGetSupplier.GetHttpRequestSupplier request,
                                                                                 HttpResponse.BodyHandler<T> handler) {
        return new HttpResponseOnConditionSequentialGetSupplier<>(request, handler);
    }

    /**
     * Builds a function to get a response of the given POST-request to be sent.
     *
     * @param request is a description of POST-request to get response of
     * @param handler to read the body of resulted response
     * @param <T>     is a type of the read body of resulted response
     * @return an instance of {@link HttpResponseSimpleSequentialGetSupplier}
     */
    public static <T> HttpResponseSimpleSequentialGetSupplier<T> responseOf(HttpRequestGetSupplier.PostHttpRequestSupplier request,
                                                                            HttpResponse.BodyHandler<T> handler) {
        return new HttpResponseSimpleSequentialGetSupplier<>(request, handler);
    }

    /**
     * Builds a function to get a response of the given PUT-request to be sent.
     *
     * @param request is a description of PUT-request to get response of
     * @param handler to read the body of resulted response
     * @param <T>     is a type of the read body of resulted response
     * @return an instance of {@link HttpResponseSimpleSequentialGetSupplier}
     */
    public static <T> HttpResponseSimpleSequentialGetSupplier<T> responseOf(HttpRequestGetSupplier.PutHttpRequestSupplier request,
                                                                            HttpResponse.BodyHandler<T> handler) {
        return new HttpResponseSimpleSequentialGetSupplier<>(request, handler);
    }

    /**
     * Builds a function to get a response of the given DELETE-request to be sent.
     *
     * @param request is a description of DELETE-request to get response of
     * @param handler to read the body of resulted response
     * @param <T>     is a type of the read body of resulted response
     * @return an instance of {@link HttpResponseSimpleSequentialGetSupplier}
     */
    public static <T> HttpResponseSimpleSequentialGetSupplier<T> responseOf(HttpRequestGetSupplier.DeleteHttpRequestSupplier request,
                                                                            HttpResponse.BodyHandler<T> handler) {
        return new HttpResponseSimpleSequentialGetSupplier<>(request, handler);
    }

    /**
     * Builds a function to get a response of the given request to be sent.
     *
     * @param request is a description of request to get response of
     * @param handler to read the body of resulted response
     * @param <T>     is a type of the read body of resulted response
     * @return an instance of {@link HttpResponseOnConditionSequentialGetSupplier}
     */
    public static <T> HttpResponseOnConditionSequentialGetSupplier<T> responseOf(HttpRequestGetSupplier.MethodHttpRequestSupplier request,
                                                                                 HttpResponse.BodyHandler<T> handler) {
        return new HttpResponseOnConditionSequentialGetSupplier<>(request, handler);
    }

    /**
     * This method defined the condition to get expected http response.
     *
     * @param condition a condition to get expected http response.
     * @return self-reference
     */
    protected R conditionToReceiveDesiredResponse(Predicate<HttpResponse<T>> condition) {
        checkArgument(nonNull(condition), "Condition should be defined");
        this.condition = condition;
        ofNullable(timeToWaitForCondition).ifPresentOrElse(duration -> {},
                () -> timeToWaitForCondition = DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get());
        return (R) this;
    }

    /**
     * This method defines the time to get expected http response. It has sense when some {@link Predicate}
     * that describes expected http response is set up via {@link #conditionToReceiveDesiredResponse(Predicate)}.
     * This time is ignored otherwise.
     *
     * @param time a time to get expected http response.
     * @return self-reference
     */
    protected R timeToReceiveDesiredResponse(Duration time) {
        checkArgument(nonNull(time), "Time to get desired response should be defined");
        this.timeToWaitForCondition = time;
        return (R) this;
    }

    /**
     * This method allows to customize the exception to be thrown when desired response is not received or
     * nothing is received at all.
     *
     * @param exceptionSupplier is a supplier of customized {@link DesiredResponseHasNotBeenReceivedException} to
     *                          be thrown when desired response is not received or nothing is received at all.
     * @return self-reference
     */
    public R toThrowIfNotReceived(Supplier<DesiredResponseHasNotBeenReceivedException> exceptionSupplier) {
        checkArgument(nonNull(exceptionSupplier), "Supplier of an exception should be defined");
        this.exceptionSupplier = exceptionSupplier;
        return (R) this;
    }

    @Override
    protected Function<HowToGetResponse, HttpResponse<T>> getEndFunction() {
        return ofNullable(condition)
                .map(tPredicate ->
                        ofNullable(exceptionSupplier)
                                .map(errorSupplier -> getSingle(toString(), getResponse, tPredicate,
                                        timeToWaitForCondition, true, errorSupplier))

                                .orElseGet(() -> getSingle(toString(), getResponse, tPredicate,
                                        timeToWaitForCondition, true)))
                .orElseGet(() -> ofNullable(exceptionSupplier)
                        .map(errorSupplier -> getSingle(toString(), getResponse, errorSupplier))
                        .orElseGet(() -> toGet(toString(), getResponse)));
    }

    public String toString() {
        return "HTTP response";
    }

    @Override
    public Function<HttpSteps, HttpResponse<T>> get() {
        return ofNullable(super.get()).orElseGet(() -> {
            from(request);
            return super.get();
        });
    }

    /**
     * This class is designed to allow a user to get responses using specific conditions via
     * {@link #conditionToReceiveDesiredResponse(Predicate)} and {@link #timeToReceiveDesiredResponse(Duration)}
     *
     * @param <T> is a type of body of the received response
     */
    public static final class HttpResponseOnConditionSequentialGetSupplier<T> extends HttpResponseSequentialGetSupplier<T, HttpResponseOnConditionSequentialGetSupplier<T>> {

        private HttpResponseOnConditionSequentialGetSupplier(HttpRequestGetSupplier request, HttpResponse.BodyHandler<T> handler) {
            super(request, handler);
        }

        @Override
        public HttpResponseOnConditionSequentialGetSupplier<T> conditionToReceiveDesiredResponse(Predicate<HttpResponse<T>> condition) {
            return super.conditionToReceiveDesiredResponse(condition);
        }

        @Override
        public HttpResponseOnConditionSequentialGetSupplier<T> timeToReceiveDesiredResponse(Duration time) {
            return super.timeToReceiveDesiredResponse(time);
        }
    }

    public static final class HttpResponseSimpleSequentialGetSupplier<T> extends HttpResponseSequentialGetSupplier<T, HttpResponseSimpleSequentialGetSupplier<T>> {
        private HttpResponseSimpleSequentialGetSupplier(HttpRequestGetSupplier request, HttpResponse.BodyHandler<T> handler) {
            super(request, handler);
        }
    }

    public HttpResponseSequentialGetSupplier<T, R> clone() {
        try {
            return (HttpResponseSequentialGetSupplier<T, R>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    Predicate<HttpResponse<T>> getCondition() {
        return condition;
    }
}
