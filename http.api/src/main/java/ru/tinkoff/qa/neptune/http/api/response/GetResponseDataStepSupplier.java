package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeCaptureOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.DesiredDataHasNotBeenReceivedException;
import ru.tinkoff.qa.neptune.http.api.request.GetRequest;
import ru.tinkoff.qa.neptune.http.api.request.MethodRequest;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.http.api.response.GetObjectFromBodyStepSupplier.object;
import static ru.tinkoff.qa.neptune.http.api.response.ResponseSequentialGetSupplier.response;

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

    public static <R, T> Common<R, T, R> body(HttpResponse<T> response, GetObjectFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyCommon(response, array);
    }

    public static <R, T> Common<R, T, R> body(HttpResponse<T> response, GetObjectFromBodyStepSupplier<T, R> obj) {
        return responseBodyCommon(response, obj);
    }

    public static <R, T> Common<R, T, R> body(HttpResponse<T> response, GetObjectFromIterableBodyStepSupplier<T, R> iterable) {
        return responseBodyCommon(response, iterable);
    }

    public static <R, T> Common<R[], T, R> body(HttpResponse<T> response, GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyCommon(response, array);
    }

    public static <T, R, S extends Iterable<R>> Common<S, T, R> body(HttpResponse<T> response, GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return responseBodyCommon(response, iterable);
    }

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


    public static <R, T> Common2<R, T, R> body(RequestBuilder request,
                                               HttpResponse.BodyHandler<T> bodyHandler,
                                               GetObjectFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyCommon2(request, bodyHandler, array);
    }

    public static <R, T> Common2<R, T, R> body(RequestBuilder request,
                                               HttpResponse.BodyHandler<T> bodyHandler,
                                               GetObjectFromBodyStepSupplier<T, R> obj) {
        return responseBodyCommon2(request, bodyHandler, obj);
    }

    public static <R, T> Common2<R, T, R> body(RequestBuilder request,
                                               HttpResponse.BodyHandler<T> bodyHandler,
                                               GetObjectFromIterableBodyStepSupplier<T, R> iterable) {
        return responseBodyCommon2(request, bodyHandler, iterable);
    }

    public static <R, T> Common2<R[], T, R> body(RequestBuilder request,
                                                 HttpResponse.BodyHandler<T> bodyHandler,
                                                 GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyCommon2(request, bodyHandler, array);
    }

    public static <T, R, S extends Iterable<R>> Common2<S, T, R> body(RequestBuilder request,
                                                                      HttpResponse.BodyHandler<T> bodyHandler,
                                                                      GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return responseBodyCommon2(request, bodyHandler, iterable);
    }

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

    public static <R, T> Retrying<R, T, R> body(GetRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyRetrying(request, bodyHandler, array);
    }

    public static <R, T> Retrying<R, T, R> body(GetRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromBodyStepSupplier<T, R> obj) {
        return responseBodyRetrying(request, bodyHandler, obj);
    }

    public static <R, T> Retrying<R, T, R> body(GetRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromIterableBodyStepSupplier<T, R> iterable) {
        return responseBodyRetrying(request, bodyHandler, iterable);
    }

    public static <R, T> Retrying<R[], T, R> body(GetRequest request,
                                                  HttpResponse.BodyHandler<T> bodyHandler,
                                                  GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyRetrying(request, bodyHandler, array);
    }

    public static <T, R, S extends Iterable<R>> Retrying<S, T, R> body(GetRequest request,
                                                                       HttpResponse.BodyHandler<T> bodyHandler,
                                                                       GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return responseBodyRetrying(request, bodyHandler, iterable);
    }

    public static <T> Retrying<T, T, T> body(GetRequest request,
                                             HttpResponse.BodyHandler<T> bodyHandler) {
        return responseBodyRetrying(request, bodyHandler,
                GetObjectFromBodyStepSupplier.<T, T>object("Body", t -> t));
    }

    public static <R, T> Retrying<R, T, R> body(MethodRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyRetrying(request, bodyHandler, array);
    }

    public static <R, T> Retrying<R, T, R> body(MethodRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromBodyStepSupplier<T, R> obj) {
        return responseBodyRetrying(request, bodyHandler, obj);
    }

    public static <R, T> Retrying<R, T, R> body(MethodRequest request,
                                                HttpResponse.BodyHandler<T> bodyHandler,
                                                GetObjectFromIterableBodyStepSupplier<T, R> iterable) {
        return responseBodyRetrying(request, bodyHandler, iterable);
    }

    public static <R, T> Retrying<R[], T, R> body(MethodRequest request,
                                                  HttpResponse.BodyHandler<T> bodyHandler,
                                                  GetObjectsFromArrayBodyStepSupplier<T, R> array) {
        return responseBodyRetrying(request, bodyHandler, array);
    }

    public static <T, R, S extends Iterable<R>> Retrying<S, T, R> body(MethodRequest request,
                                                                       HttpResponse.BodyHandler<T> bodyHandler,
                                                                       GetObjectsFromIterableBodyStepSupplier<T, R, S> iterable) {
        return responseBodyRetrying(request, bodyHandler, iterable);
    }

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

    public S throwWhenNothing(String exceptionMessage) {
        ofNullable(from).ifPresent(s -> {
            var toThrow = ((Supplier<DesiredDataHasNotBeenReceivedException>) () -> new DesiredDataHasNotBeenReceivedException(exceptionMessage));
            var clazz = s.getClass();

            if (GetObjectFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromArrayBodyStepSupplier<?, ?>) s).throwOnEmptyResult(toThrow);
                return;
            }

            if (GetObjectFromBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromBodyStepSupplier<?, ?>) s).throwOnEmptyResult(toThrow);
                return;
            }

            if (GetObjectFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectFromIterableBodyStepSupplier<?, ?>) s).throwOnEmptyResult(toThrow);
                return;
            }

            if (GetObjectsFromArrayBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectsFromArrayBodyStepSupplier<?, ?>) s).throwOnEmptyResult(toThrow);
                return;
            }

            if (GetObjectsFromIterableBodyStepSupplier.class.isAssignableFrom(clazz)) {
                ((GetObjectsFromIterableBodyStepSupplier<?, ?, ?>) s).throwOnEmptyResult(toThrow);
            }
        });
        return (S) this;
    }

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

    public S dataCriteria(String description, Predicate<? super P> predicate) {
        return dataCriteria(condition(description, predicate));
    }

    protected S responseCriteria(Criteria<? super HttpResponse<T>> criteria) {
        ofNullable(responseFunction).ifPresent(o -> {
            var clazz = o.getClass();
            if (ResponseSequentialGetSupplier.class.isAssignableFrom(clazz)) {
                ((ResponseSequentialGetSupplier<T>) o).criteria(criteria);
            }
        });

        return (S) this;
    }

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

    public static final class Common<R, T, P> extends GetResponseDataStepSupplier<R, T, P, Common<R, T, P>> {
        private Common(String description) {
            super(description);
        }
    }

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
