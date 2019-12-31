package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.steps.ConditionConcatenation;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.http.api.captors.RequestStringCaptor;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.net.http.HttpResponse.BodyHandlers.discarding;
import static java.util.Optional.ofNullable;
import static java.util.stream.StreamSupport.stream;
import static ru.tinkoff.qa.neptune.core.api.steps.Condition.condition;

/**
 * This class is designed to build chains of functions that get a response when a request is sent.
 *
 * @param <T> is a type of body of the received response
 */
@MakeStringCapturesOnFinishing
@MakeFileCapturesOnFinishing
public class HttpResponseSequentialGetSupplier<T> extends SequentialGetStepSupplier
        .GetObjectStepSupplier<HttpStepContext, HttpResponse<T>, HttpResponseSequentialGetSupplier<T>> implements Cloneable {

    private final PreparedHttpRequest request;

    private HttpResponseSequentialGetSupplier(PreparedHttpRequest request, HttpResponse.BodyHandler<T> handler) {
        super("HTTP response", httpStepContext -> {
            var builtRequest = request.build();
            try {
                return httpStepContext.getCurrentClient().send(builtRequest, handler);
            } catch (Throwable e) {
                new RequestStringCaptor("Request to get response").getCaptured(builtRequest);
                throw new RuntimeException(e);
            }
        });
        this.request = request;
    }

    static <T, S> void addCondition(HttpResponseSequentialGetSupplier<T> addTo,
                                    Function<T, S> extractObjectFromBody,
                                    Predicate<S> predicateToAdd) {
        Predicate<HttpResponse<T>> predicate = condition(predicateToAdd.toString(), tHttpResponse -> {
            var body = tHttpResponse.body();
            return ofNullable(body)
                    .map(t -> {
                        S s = extractObjectFromBody.apply(t);
                        return predicateToAdd.test(s);
                    })
                    .orElse(false);
        });
        addTo.criteria(predicate);
    }

    static <T, R, S extends Iterable<R>> void addConditionIterable(HttpResponseSequentialGetSupplier<T> addTo,
                                                                   Function<T, S> extractIterableFromBody,
                                                                   Predicate<R> predicateToAdd) {
        Predicate<HttpResponse<T>> predicate = condition(predicateToAdd.toString(), tHttpResponse -> {
            var body = tHttpResponse.body();
            return ofNullable(body)
                    .map(t -> {
                        S s = extractIterableFromBody.apply(t);
                        return stream(s.spliterator(), true).anyMatch(predicateToAdd);
                    })
                    .orElse(false);
        });
        addTo.criteria(predicate);
    }

    static <T, R> void addConditionArray(HttpResponseSequentialGetSupplier<T> addTo,
                                         Function<T, R[]> extractIterableFromBody,
                                         Predicate<R> predicateToAdd) {
        addConditionIterable(addTo, extractIterableFromBody.andThen(rs -> ofNullable(rs)
                        .map(Arrays::asList)
                        .orElse(null)),
                predicateToAdd);
    }

    /**
     * Builds a function to get a response of the given request to be sent.
     *
     * @param request is a request to get response of
     * @param handler to read the body of resulted response
     * @param <T>     is a type of the read body of resulted response
     * @return an instance of {@link HttpResponseSequentialGetSupplier}
     */
    public static <T> HttpResponseSequentialGetSupplier<T> responseOf(PreparedHttpRequest request,
                                                                      HttpResponse.BodyHandler<T> handler) {
        return new HttpResponseSequentialGetSupplier<>(request, handler);
    }

    /**
     * Builds a function to get a response of the given request to be sent.
     *
     * @param request is a request to get response of
     * @return an instance of {@link HttpResponseSequentialGetSupplier}
     */
    public static HttpResponseSequentialGetSupplier<Void> responseOf(PreparedHttpRequest request) {
        return responseOf(request, discarding());
    }

    @Override
    public Function<HttpStepContext, HttpResponse<T>> get() {
        return httpStepContext -> ofNullable(super.get()
                .apply(httpStepContext))
                .orElseGet(() -> {
                    new RequestStringCaptor("Request to get response").getCaptured(request.build());
                    return null;
                });
    }

    @Override
    protected HttpResponseSequentialGetSupplier<T> criteria(Predicate<? super HttpResponse<T>> condition) {
        return super.criteria(condition);
    }

    @Override
    protected HttpResponseSequentialGetSupplier<T> criteria(ConditionConcatenation concat, Predicate<? super HttpResponse<T>> condition) {
        return super.criteria(concat, condition);
    }

    @Override
    protected HttpResponseSequentialGetSupplier<T> criteria(ConditionConcatenation concat, String conditionDescription, Predicate<? super HttpResponse<T>> condition) {
        return super.criteria(concat, conditionDescription, condition);
    }

    @Override
    protected HttpResponseSequentialGetSupplier<T> criteria(String conditionDescription, Predicate<? super HttpResponse<T>> condition) {
        return super.criteria(conditionDescription, condition);
    }

    @Override
    protected HttpResponseSequentialGetSupplier<T> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    protected HttpResponseSequentialGetSupplier<T> pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    protected HttpResponseSequentialGetSupplier<T> throwOnEmptyResult(Supplier<? extends RuntimeException> exceptionSupplier) {
        return super.throwOnEmptyResult(exceptionSupplier);
    }

    @Override
    public HttpResponseSequentialGetSupplier<T> clone() {
        return super.clone();
    }
}
