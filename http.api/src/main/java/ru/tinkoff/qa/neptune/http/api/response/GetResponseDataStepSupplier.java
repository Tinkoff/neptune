package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.GetRequest;
import ru.tinkoff.qa.neptune.http.api.request.MethodRequest;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.object;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

/**
 * Builds a step-function to receive http response and extract desired data from the response body.
 *
 * @param <R> is a type of resulted value
 * @param <T> is a type of a response body
 * @param <P> is a type of a value that should be filtered by criteria.
 * @param <S> if a type of a class that extends {@link GetResponseDataStepSupplier}
 * @see #dataCriteria(Criteria)
 * @see #dataCriteria(String, Predicate)
 */
@SuppressWarnings("unchecked")
@MakeCaptureOnFinishing(typeOfCapture = Object.class)
public abstract class GetResponseDataStepSupplier<R, T, P, S extends GetResponseDataStepSupplier<R, T, P, S>> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<HttpStepContext, R, R, S> {

    SequentialGetStepSupplier<HttpStepContext, ? extends R, ?, P, ?> from;
    Object responseFunction;

    private GetResponseDataStepSupplier(String description) {
        super(description, r -> r);
    }

    private static <T> void addHowToGetResponse(SequentialGetStepSupplier<HttpStepContext, ?, ?, ?, ?> from,
                                                HttpResponse<T> response) {
        var clazz = from.getClass();

        if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromArrayBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponse(response);
            return;
        }

        if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponse(response);
            return;
        }

        if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromIterableBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponse(response);
            return;
        }

        if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectsFromArrayBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponse(response);
            return;
        }

        if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectsFromIterableBodyStepSupplier<T, ?, ?>) from).getOriginalFunction().setResponse(response);
        }
    }

    private static <T> void addHowToGetResponse(SequentialGetStepSupplier<HttpStepContext, ?, ?, ?, ?> from,
                                                ResponseSequentialGetSupplier<T> response) {
        var clazz = from.getClass();

        if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromArrayBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponse(response);
            return;
        }

        if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponse(response);
            return;
        }

        if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectFromIterableBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponse(response);
            return;
        }

        if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectsFromArrayBodyStepSupplier<T, ?>) from).getOriginalFunction().setResponse(response);
            return;
        }

        if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
            ((GetObjectsFromIterableBodyStepSupplier<T, ?, ?>) from).getOriginalFunction().setResponse(response);
        }
    }

    private static <R, T, P> Common<R, T, P> responseBodyCommon(HttpResponse<T> response,
                                                                SequentialGetStepSupplier<HttpStepContext, R, ?, P, ?> whatToGet) {
        checkArgument(nonNull(response), "Http response to get body data should be defined");
        checkArgument(nonNull(whatToGet), "Object that describes what to get from the response body should be defined");
        return new Common<R, T, P>(whatToGet.toString())
                .use(whatToGet)
                .fromResponse(response);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that returns an item of array.
     * An array is retrieved from body of the received http response.
     *
     * @param response   is a received http response.
     * @param oneOfArray is an object that describes resulted object and how to get an array from response body
     * @param <R>        is a type of an an item of an array
     * @param <T>        is a type of a response body
     * @return instance of {@link Common}
     */
    public static <R, T> Common<R, T, R> body(HttpResponse<T> response, GetObjectFromArrayBodyStepSupplier<T, R> oneOfArray) {
        return responseBodyCommon(response, oneOfArray);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that returns an object.
     * This object is retrieved from body of the received http response.
     *
     * @param response is a received http response.
     * @param obj      is an object that describes resulted object
     * @param <R>      is a type of resulted object
     * @param <T>      is a type of a response body
     * @return instance of {@link Common}
     */
    public static <R, T> Common<R, T, R> body(HttpResponse<T> response, GetObjectFromBodyStepSupplier<T, R> obj) {
        return responseBodyCommon(response, obj);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that returns an item of {@link Iterable}.
     * An iterable is retrieved from body of the received http response.
     *
     * @param response      is a received http response.
     * @param oneOfIterable is an object that describes resulted object and how to get an {@link Iterable} from response body
     * @param <R>           is a type of an item of iterable
     * @param <T>           is a type of a response body
     * @return instance of {@link Common}
     */
    public static <R, T> Common<R, T, R> body(HttpResponse<T> response, GetObjectFromIterableBodyStepSupplier<T, R> oneOfIterable) {
        return responseBodyCommon(response, oneOfIterable);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that returns an array.
     * An array is retrieved from body of the received http response.
     *
     * @param response is a received http response.
     * @param array    is an object that describes resulted array and how to get it from response body
     * @param <R>      is a type of an an item of an array
     * @param <T>      is a type of a response body
     * @return instance of {@link Common}
     */
    public static <R, T> Common<R[], T, R> body(HttpResponse<T> response, GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyCommon(response, array);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that returns an {@link Iterable}.
     * An iterable is retrieved from body of the received http response.
     *
     * @param response is a received http response.
     * @param iterable is an object that describes resulted iterable and how to get it from response body
     * @param <T>      is a type of a response body
     * @param <R>      is a type of an item of iterable
     * @param <S>      is a type of {@link Iterable}
     * @return instance of {@link Common}
     */
    public static <T, R, S extends Iterable<R>> Common<S, T, R> body(HttpResponse<T> response, GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return responseBodyCommon(response, iterable);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that returns body of a
     * received http response
     *
     * @param response is a received http response.
     * @param <T>      is a type of a response body
     * @return instance of {@link Common}
     */
    public static <T> Common<T, T, T> body(HttpResponse<T> response) {
        return body(response, object("Body", t -> t));
    }

    private static <R, T, P> Common2<R, T, P> responseBodyCommon2(RequestBuilder request,
                                                                  HttpResponse.BodyHandler<T> bodyHandler,
                                                                  SequentialGetStepSupplier<HttpStepContext, R, ?, P, ?> whatToGet) {
        checkArgument(nonNull(request), "Http request should be defined");
        checkArgument(nonNull(bodyHandler), "Body handler of http response should be defined");
        checkArgument(nonNull(whatToGet), "Object that describes what to get from the response body should be defined");
        return new Common2<R, T, P>(whatToGet.toString())
                .use(whatToGet)
                .fromResponse(response(request, bodyHandler));
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an item of array. An array is retrieved from body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler is a body handler of received response
     * @param oneOfArray  is an object that describes resulted object and how to get an array from response body
     * @param <R>         is a type of an an item of an array
     * @param <T>         is a type of a response body
     * @return instance of {@link Common2}
     */
    public static <R, T> Common2<R, T, R> body(RequestBuilder request,
                                               HttpResponse.BodyHandler<T> bodyHandler,
                                               GetObjectFromArrayBodyStepSupplier<T, R> oneOfArray) {
        return responseBodyCommon2(request, bodyHandler, oneOfArray);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an object. The object is retrieved from body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler is a body handler of received response
     * @param obj         is an object that describes resulted object
     * @param <R>         is a type of resulted object
     * @param <T>         is a type of a response body
     * @return instance of {@link Common2}
     */
    public static <R, T> Common2<R, T, R> body(RequestBuilder request,
                                               HttpResponse.BodyHandler<T> bodyHandler,
                                               GetObjectFromBodyStepSupplier<T, R> obj) {
        return responseBodyCommon2(request, bodyHandler, obj);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an item of {@link Iterable}. An iterable is retrieved from body of the http response.
     *
     * @param request       is a description of an http request to send
     * @param bodyHandler   is a body handler of received response
     * @param oneOfIterable is an object that describes resulted object and how to get an {@link Iterable} from response body
     * @param <R>           is a type of an item of iterable
     * @param <T>           is a type of a response body
     * @return instance of {@link Common2}
     */
    public static <R, T> Common2<R, T, R> body(RequestBuilder request,
                                               HttpResponse.BodyHandler<T> bodyHandler,
                                               GetObjectFromIterableBodyStepSupplier<T, R> oneOfIterable) {
        return responseBodyCommon2(request, bodyHandler, oneOfIterable);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an array. The array is retrieved from body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler is a body handler of received response
     * @param array       is an object that describes resulted array and how to get it from response body
     * @param <R>         is a type of an an item of an array
     * @param <T>         is a type of a response body
     * @return instance of {@link Common2}
     */
    public static <R, T> Common2<R[], T, R> body(RequestBuilder request,
                                                 HttpResponse.BodyHandler<T> bodyHandler,
                                                 GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyCommon2(request, bodyHandler, array);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an {@link Iterable}. The iterable is retrieved from body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler is a body handler of received response
     * @param iterable    is an object that describes resulted iterable and how to get it from response body
     * @param <T>         is a type of a response body
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of {@link Iterable}
     * @return instance of {@link Common2}
     */
    public static <T, R, S extends Iterable<R>> Common2<S, T, R> body(RequestBuilder request,
                                                                      HttpResponse.BodyHandler<T> bodyHandler,
                                                                      GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return responseBodyCommon2(request, bodyHandler, iterable);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler s a body handler of received response
     * @param <T>         is a type of a response body
     * @return instance of {@link Common2}
     */
    public static <T> Common2<T, T, T> body(RequestBuilder request,
                                            HttpResponse.BodyHandler<T> bodyHandler) {
        return body(request, bodyHandler, object("Body", t -> t));
    }

    private static <R, T, P> Retrying<R, T, P> responseBodyRetrying(RequestBuilder request,
                                                                    HttpResponse.BodyHandler<T> bodyHandler,
                                                                    SequentialGetStepSupplier<HttpStepContext, R, ?, P, ?> whatToGet) {
        checkArgument(nonNull(request), "Http request should be defined");
        checkArgument(nonNull(bodyHandler), "Body handler of http response should be defined");
        checkArgument(nonNull(whatToGet), "Object that describes what to get from the response body should be defined");
        return new Retrying<R, T, P>(whatToGet.toString())
                .use(whatToGet)
                .fromResponse(response(request, bodyHandler));
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http GET-request,
     * then receives a response and then returns an item of array. An array is retrieved from body of the http response.
     *
     * @param request     is a description of an http GET-request to send
     * @param bodyHandler is a body handler of received response
     * @param oneOfArray  is an object that describes resulted object and how to get an array from response body
     * @param <R>         is a type of an an item of an array
     * @param <T>         is a type of a response body
     * @return instance of {@link Retrying}
     * @see GetRequest
     */
    public static <R, T> Retrying<R, T, R> body(GetRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromArrayBodyStepSupplier<T, R> oneOfArray) {
        return responseBodyRetrying(request, bodyHandler, oneOfArray);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http GET-request,
     * then receives a response and then returns an object. The object is retrieved from body of the http response.
     *
     * @param request     is a description of an http GET-request to send
     * @param bodyHandler is a body handler of received response
     * @param obj         is an object that describes resulted object
     * @param <R>         is a type of resulted object
     * @param <T>         is a type of a response body
     * @return instance of {@link Retrying}
     * @see GetRequest
     */
    public static <R, T> Retrying<R, T, R> body(GetRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromBodyStepSupplier<T, R> obj) {
        return responseBodyRetrying(request, bodyHandler, obj);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http GET-request,
     * then receives a response and then returns an item of {@link Iterable}. An iterable is retrieved from body of the http response.
     *
     * @param request       is a description of an http GET-request to send
     * @param bodyHandler   is a body handler of received response
     * @param oneOfIterable is an object that describes resulted object and how to get an {@link Iterable} from response body
     * @param <R>           is a type of an item of iterable
     * @param <T>           is a type of a response body
     * @return instance of {@link Retrying}
     * @see GetRequest
     */
    public static <R, T> Retrying<R, T, R> body(GetRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromIterableBodyStepSupplier<T, R> oneOfIterable) {
        return responseBodyRetrying(request, bodyHandler, oneOfIterable);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http GET-request,
     * then receives a response and then returns an array. The array is retrieved from body of the http response.
     *
     * @param request     is a description of an http GET-request to send
     * @param bodyHandler is a body handler of received response
     * @param array       is an object that describes resulted array and how to get it from response body
     * @param <R>         is a type of an an item of an array
     * @param <T>         is a type of a response body
     * @return instance of {@link Retrying}
     * @see GetRequest
     */
    public static <R, T> Retrying<R[], T, R> body(GetRequest request,
                                                  HttpResponse.BodyHandler<T> bodyHandler,
                                                  GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyRetrying(request, bodyHandler, array);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http GET-request,
     * then receives a response and then returns an {@link Iterable}. The iterable is retrieved from body of the http response.
     *
     * @param request     is a description of an http GET-request to send
     * @param bodyHandler is a body handler of received response
     * @param iterable    is an object that describes resulted iterable and how to get it from response body
     * @param <T>         is a type of a response body
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of {@link Iterable}
     * @return instance of {@link Retrying}
     * @see GetRequest
     */
    public static <T, R, S extends Iterable<R>> Retrying<S, T, R> body(GetRequest request,
                                                                       HttpResponse.BodyHandler<T> bodyHandler,
                                                                       GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return responseBodyRetrying(request, bodyHandler, iterable);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http GET-request,
     * then receives a response and then returns body of the http response.
     *
     * @param request     is a description of an http GET-request to send
     * @param bodyHandler s a body handler of received response
     * @param <T>         is a type of a response body
     * @return instance of {@link Retrying}
     * @see GetRequest
     */
    public static <T> Retrying<T, T, T> body(GetRequest request,
                                             HttpResponse.BodyHandler<T> bodyHandler) {
        return responseBodyRetrying(request, bodyHandler,
                GetObjectFromBodyStepSupplier.<T, T>object("Body", t -> t));
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an item of array. An array is retrieved from body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler is a body handler of received response
     * @param oneOfArray  is an object that describes resulted object and how to get an array from response body
     * @param <R>         is a type of an an item of an array
     * @param <T>         is a type of a response body
     * @return instance of {@link Retrying}
     * @see MethodRequest
     */
    public static <R, T> Retrying<R, T, R> body(MethodRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromArrayBodyStepSupplier<T, R> oneOfArray) {
        return responseBodyRetrying(request, bodyHandler, oneOfArray);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an object. The object is retrieved from body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler is a body handler of received response
     * @param obj         is an object that describes resulted object
     * @param <R>         is a type of resulted object
     * @param <T>         is a type of a response body
     * @return instance of {@link Retrying}
     * @see MethodRequest
     */
    public static <R, T> Retrying<R, T, R> body(MethodRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromBodyStepSupplier<T, R> obj) {
        return responseBodyRetrying(request, bodyHandler, obj);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an item of {@link Iterable}. An iterable is retrieved from body of the http response.
     *
     * @param request       is a description of an http request to send
     * @param bodyHandler   is a body handler of received response
     * @param oneOfIterable is an object that describes resulted object and how to get an {@link Iterable} from response body
     * @param <R>           is a type of an item of iterable
     * @param <T>           is a type of a response body
     * @return instance of {@link Retrying}
     * @see MethodRequest
     */
    public static <R, T> Retrying<R, T, R> body(MethodRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromIterableBodyStepSupplier<T, R> oneOfIterable) {
        return responseBodyRetrying(request, bodyHandler, oneOfIterable);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an array. The array is retrieved from body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler is a body handler of received response
     * @param array       is an object that describes resulted array and how to get it from response body
     * @param <R>         is a type of an an item of an array
     * @param <T>         is a type of a response body
     * @return instance of {@link Retrying}
     * @see MethodRequest
     */
    public static <R, T> Retrying<R[], T, R> body(MethodRequest request,
                                                  HttpResponse.BodyHandler<T> bodyHandler,
                                                  GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyRetrying(request, bodyHandler, array);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns an {@link Iterable}. The iterable is retrieved from body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler is a body handler of received response
     * @param iterable    is an object that describes resulted iterable and how to get it from response body
     * @param <T>         is a type of a response body
     * @param <R>         is a type of an item of iterable
     * @param <S>         is a type of {@link Iterable}
     * @return instance of {@link Retrying}
     * @see MethodRequest
     */
    public static <T, R, S extends Iterable<R>> Retrying<S, T, R> body(MethodRequest request,
                                                                       HttpResponse.BodyHandler<T> bodyHandler,
                                                                       GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return responseBodyRetrying(request, bodyHandler, iterable);
    }

    /**
     * Creates an instance of {@link GetResponseDataStepSupplier}. It builds a step-function that sends http request,
     * then receives a response and then returns body of the http response.
     *
     * @param request     is a description of an http request to send
     * @param bodyHandler s a body handler of received response
     * @param <T>         is a type of a response body
     * @return instance of {@link Retrying}
     * @see MethodRequest
     */
    public static <T> Retrying<T, T, T> body(MethodRequest request,
                                             HttpResponse.BodyHandler<T> bodyHandler) {
        return responseBodyRetrying(request, bodyHandler,
                GetObjectFromBodyStepSupplier.<T, T>object("Body", t -> t));
    }

    S use(SequentialGetStepSupplier<HttpStepContext, R, ?, P, ?> from) {
        this.from = from;
        return (S) this;
    }

    S fromResponse(HttpResponse<T> response) {
        this.responseFunction = response;
        return (S) this;
    }

    S fromResponse(ResponseSequentialGetSupplier<T> getResponse) {
        this.responseFunction = getResponse.clone();
        return (S) this;
    }

    /**
     * Defines message text of an exception to be thrown when received http response has no desired data or
     * expected http response has not been received.
     *
     * @param exceptionMessage a message text of an exception to be thrown when received http response has no
     *                         desired data or expected http response has not been received.
     * @return self-reference
     */
    public S throwWhenNothing(String exceptionMessage) {
        ofNullable(from).ifPresent(s -> {
            var clazz = s.getClass();

            if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromArrayBodyStepSupplier<?, ?>) s).throwWhenNothing(exceptionMessage);
                return;
            }

            if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromBodyStepSupplier<?, ?>) s).throwWhenNothing(exceptionMessage);
                return;
            }

            if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromIterableBodyStepSupplier<?, ?>) s).throwWhenNothing(exceptionMessage);
                return;
            }

            if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectsFromArrayBodyStepSupplier<?, ?>) s).throwWhenNothing(exceptionMessage);
                return;
            }

            if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectsFromIterableBodyStepSupplier<?, ?, ?>) s).throwWhenNothing(exceptionMessage);
            }
        });
        return (S) this;
    }

    /**
     * Defines criteria that resulted data should meet.
     *
     * @param criteria is a criteria that resulted data should meet.
     * @return self-reference
     */
    public S dataCriteria(Criteria<? super P> criteria) {
        ofNullable(from).ifPresent(s -> {
            var clazz = s.getClass();

            if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromArrayBodyStepSupplier<?, P>) s).criteria(criteria);
                return;
            }

            if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromBodyStepSupplier<?, P>) s).criteria(criteria);
                return;
            }

            if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromIterableBodyStepSupplier<?, P>) s).criteria(criteria);
                return;
            }

            if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectsFromArrayBodyStepSupplier<?, P>) s).criteria(criteria);
                return;
            }

            if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectsFromIterableBodyStepSupplier<?, P, ?>) s).criteria(criteria);
            }
        });
        return (S) this;
    }

    /**
     * Defines criteria that resulted data should meet.
     *
     * @param description is a description of a criteria
     * @param predicate   is a {@link Predicate} that describes the criteria
     * @return self-reference
     */
    public S dataCriteria(String description, Predicate<? super P> predicate) {
        return dataCriteria(condition(description, predicate));
    }

    /**
     * Defines criteria that received response should meet.
     *
     * @param criteria is a criteria that received response should meet.
     * @return self-reference
     */
    protected S responseCriteria(Criteria<? super HttpResponse<T>> criteria) {
        ofNullable(responseFunction).ifPresent(o -> {
            var clazz = o.getClass();
            if (ResponseSequentialGetSupplier.class.isAssignableFrom(clazz)) {
                ((ResponseSequentialGetSupplier<T>) o).criteria(criteria);
            }
        });

        return (S) this;
    }

    /**
     * Defines criteria that received response should meet.
     *
     * @param description is a description of a criteria
     * @param predicate is a {@link Predicate} that describes the criteria
     * @return self-reference
     */
    protected S responseCriteria(String description, Predicate<? super HttpResponse<T>> predicate) {
        return responseCriteria(condition(description, predicate));
    }

    protected String prepareStepDescription() {
        //TODO use step arguments

        String stepDescription;
        var clazz = from.getClass();

        if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
            stepDescription = ((GetObjectFromArrayBodyStepSupplier<?, ?>) from).prepareStepDescription();
        } else if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
            stepDescription = ((GetObjectFromBodyStepSupplier<?, ?>) from).prepareStepDescription();
        } else if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
            stepDescription = ((GetObjectFromIterableBodyStepSupplier<?, ?>) from).prepareStepDescription();
        } else if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
            stepDescription = ((GetObjectsFromArrayBodyStepSupplier<?, ?>) from).prepareStepDescription();
        } else if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
            stepDescription = ((GetObjectsFromIterableBodyStepSupplier<?, ?, ?>) from).prepareStepDescription();
        } else {
            stepDescription = from.toString();
        }

        if (ResponseSequentialGetSupplier.class.isAssignableFrom(responseFunction.getClass())) {
            return format("%s from %s", stepDescription, ((ResponseSequentialGetSupplier<?>) responseFunction).prepareStepDescription());
        } else {
            return format("%s from %s", stepDescription, responseFunction);
        }
    }

    @Override
    public Function<HttpStepContext, R> get() {
        checkArgument(nonNull(from), "It is not defined how to get data from http response");
        checkArgument(nonNull(responseFunction), "Response to be received is not defined");

        var clazz = responseFunction.getClass();
        if (HttpResponse.class.isAssignableFrom(clazz)) {
            addHowToGetResponse(from, (HttpResponse<T>) responseFunction);
        } else {
            addHowToGetResponse(from, (ResponseSequentialGetSupplier<T>) responseFunction);
        }

        super.from(from.get());
        return super.get();
    }

    /**
     * Builds a step-function extract desired data from the response body. This class has no extension to
     * {@link GetResponseDataStepSupplier}
     *
     * @param <R> is a type of resulted value
     * @param <T> is a type of a response body
     * @param <P> is a type of a value that should be filtered by criteria.
     */
    public static final class Common<R, T, P> extends GetResponseDataStepSupplier<R, T, P, Common<R, T, P>> {
        private Common(String description) {
            super(description);
        }
    }

    /**
     * Builds a step-function that sends http request and then extract desired data from body of received http response.
     * It allows to define criteria that received response should meet.
     *
     * @param <R> is a type of resulted value
     * @param <T> is a type of a response body
     * @param <P> is a type of a value that should be filtered by criteria.
     */
    public static final class Common2<R, T, P> extends GetResponseDataStepSupplier<R, T, P, Common2<R, T, P>> {
        private Common2(String description) {
            super(description);
        }

        @Override
        public Common2<R, T, P> responseCriteria(Criteria<? super HttpResponse<T>> criteria) {
            return super.responseCriteria(criteria);
        }

        @Override
        public Common2<R, T, P> responseCriteria(String description, Predicate<? super HttpResponse<T>> predicate) {
            return super.responseCriteria(description, predicate);
        }
    }

    /**
     * Builds a step-function that sends http request and then extract desired data from body of received http response.
     * It allows to define criteria that received response should meet. Also it allows to define duration of time
     * of the waiting for expected http response that has expected body.
     *
     * @param <R> is a type of resulted value
     * @param <T> is a type of a response body
     * @param <P> is a type of a value that should be filtered by criteria.
     */
    public static final class Retrying<R, T, P> extends GetResponseDataStepSupplier<R, T, P, Retrying<R, T, P>> {

        private Retrying(String description) {
            super(description);
        }

        @Override
        public Retrying<R, T, P> responseCriteria(Criteria<? super HttpResponse<T>> criteria) {
            return super.responseCriteria(criteria);
        }

        @Override
        public Retrying<R, T, P> responseCriteria(String description, Predicate<? super HttpResponse<T>> predicate) {
            return super.responseCriteria(description, predicate);
        }

        @Override
        public Retrying<R, T, P> timeOut(Duration timeOut) {
            ofNullable(from).ifPresent(s -> {
                var clazz = s.getClass();

                if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectFromArrayBodyStepSupplier<?, ?>) s).timeOut(timeOut);
                    return;
                }

                if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectFromBodyStepSupplier<?, ?>) s).timeOut(timeOut);
                    return;
                }

                if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectFromIterableBodyStepSupplier<?, ?>) s).timeOut(timeOut);
                    return;
                }

                if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectsFromArrayBodyStepSupplier<?, ?>) s).timeOut(timeOut);
                    return;
                }

                if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectsFromIterableBodyStepSupplier<?, ?, ?>) s).timeOut(timeOut);
                }
            });
            return this;
        }

        @Override
        public Retrying<R, T, P> pollingInterval(Duration pollingTime) {
            ofNullable(from).ifPresent(s -> {
                var clazz = s.getClass();

                if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectFromArrayBodyStepSupplier<?, ?>) s).pollingInterval(pollingTime);
                    return;
                }

                if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectFromBodyStepSupplier<?, ?>) s).pollingInterval(pollingTime);
                    return;
                }

                if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectFromIterableBodyStepSupplier<?, ?>) s).pollingInterval(pollingTime);
                    return;
                }

                if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectsFromArrayBodyStepSupplier<?, ?>) s).pollingInterval(pollingTime);
                    return;
                }

                if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                    ((GetObjectsFromIterableBodyStepSupplier<?, ?, ?>) s).pollingInterval(pollingTime);
                }
            });
            return this;
        }
    }
}
