package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.CaptorFilterByProducedType;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.*;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.URLEncodedForm;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static java.util.Set.of;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;

/**
 * Builds a step-function that receives http response
 *
 * @param <T> is a type of response body
 */
@MakeStringCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialGetStepSupplier.DefaultParameterNames(
        criteria = "Response criteria"
)
public final class ResponseSequentialGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<HttpStepContext, HttpResponse<T>,
        ResponseSequentialGetSupplier<T>> {

    private final ResponseExecutionInfo info;
    private final HttpRequest request;
    private boolean toReport = true;

    private ResponseSequentialGetSupplier(RequestBuilder requestBuilder,
                                          HttpResponse.BodyHandler<T> bodyHandler,
                                          ResponseExecutionInfo info) {
        super("Http Response", httpStepContext -> {
            try {
                info.setLastReceived(null);
                info.startExecutionLogging();
                var received = httpStepContext.getCurrentClient().send(requestBuilder.build(), bodyHandler);
                info.setLastReceived(received);
                return received;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                info.stopExecutionLogging();
            }
        });
        request = requestBuilder.build();
        this.info = info;

    }

    /**
     * Creates an instance that builds a step-function to send an http request and to receive a response
     * with body.
     *
     * @param requestBuilder is a builder of an http request
     * @param bodyHandler    of a response body
     * @param <T>            is a type of response body
     * @return an instance of {@link ResponseSequentialGetSupplier}
     */
    public static <T> ResponseSequentialGetSupplier<T> response(RequestBuilder requestBuilder, HttpResponse.BodyHandler<T> bodyHandler) {
        return new ResponseSequentialGetSupplier<>(requestBuilder, bodyHandler, new ResponseExecutionInfo());
    }

    @Override
    protected ResponseSequentialGetSupplier<T> criteria(String description, Predicate<? super HttpResponse<T>> predicate) {
        return super.criteria(description, predicate);
    }

    @Override
    protected ResponseSequentialGetSupplier<T> criteria(Criteria<? super HttpResponse<T>> criteria) {
        return super.criteria(criteria);
    }

    @Override
    protected Function<HttpStepContext, HttpResponse<T>> getEndFunction() {
        return httpStepContext -> {
            if (toReport) {
                catchValue(getRequest().body(), of(new CaptorFilterByProducedType(Object.class)));
            }
            boolean success = false;
            try {
                var result = super.getEndFunction().apply(httpStepContext);
                success = true;
                return result;
            } finally {
                if ((toReport && success && catchSuccessEvent()) || (toReport && !success && catchFailureEvent())) {
                    catchValue(info, of(new CaptorFilterByProducedType(Object.class)));
                }
            }
        };
    }

    ResponseExecutionInfo getInfo() {
        return info;
    }

    ResponseSequentialGetSupplier<T> toNotReport() {
        toReport = false;
        return this;
    }

    @Override
    public Function<HttpStepContext, HttpResponse<T>> get() {
        if (!toReport) {
            return getEndFunction();
        }
        return super.get();
    }

    @Override
    protected Map<String, String> getParameters() {
        var p = super.getParameters();

        var params = new LinkedHashMap<String, String>();
        params.put("Endpoint URI", request.uri().toString());
        params.put("Method", request.method());

        var headerMap = request.headers().map();
        if (headerMap.size() > 0) {
            params.put("Headers", headerMap.toString());
        }

        request.timeout().ifPresent(d ->
                params.put("Timeout", formatDurationHMS(d.toMillis())));

        params.put("Expect Continue", valueOf(request.expectContinue()));

        request.version().ifPresent(v ->
                params.put("Version", v.toString()));

        ofNullable(getRequest().body())
                .ifPresent(b -> {
                    var cls = b.getClass();
                    if (!cls.equals(JSoupDocumentBody.class)
                            && !cls.equals(SerializedBody.class)
                            && !cls.equals(StringBody.class)
                            && !cls.equals(URLEncodedForm.class)
                            && !cls.equals(W3CDocumentBody.class)
                            && !cls.equals(MultiPartBody.class)) {
                        params.put("Request body", b.toString());
                        return;
                    }

                    if (cls.equals(StringBody.class)) {
                        var stringBody = ((StringBody) b).body();
                        if (stringBody.length() <= 50) {
                            params.put("Request body", stringBody);
                        }
                    }
                });
        params.putAll(p);
        return params;
    }

    @Override
    protected ResponseSequentialGetSupplier<T> clone() {
        var result = super.clone();
        result.toReport = true;
        return result;
    }

    NeptuneHttpRequestImpl getRequest() {
        return (NeptuneHttpRequestImpl) request;
    }
}
