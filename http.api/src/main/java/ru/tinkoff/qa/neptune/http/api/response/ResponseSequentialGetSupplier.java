package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.captors.request.AbstractRequestBodyCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectsCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.RequestResponseLogCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.ResponseCaptor;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.*;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.URLEncodedForm;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;

/**
 * Builds a step-function that receives http response
 *
 * @param <T> is a type of response body
 */
@CaptureOnSuccess(by = {ResponseCaptor.class, AbstractResponseBodyObjectCaptor.class, AbstractResponseBodyObjectsCaptor.class})
@SequentialGetStepSupplier.DefineCriteriaParameterName("Response criteria")
@MaxDepthOfReporting(0)
public final class ResponseSequentialGetSupplier<T> extends SequentialGetStepSupplier.GetObjectStepSupplier<HttpStepContext, HttpResponse<T>,
        ResponseSequentialGetSupplier<T>> {

    private final ResponseExecutionInfo info;
    private final HttpRequest request;

    private ResponseSequentialGetSupplier(RequestBuilder requestBuilder,
                                          HttpResponse.BodyHandler<T> bodyHandler,
                                          ResponseExecutionInfo info) {
        super(httpStepContext -> {
            try {
                info.setLastReceived(null);
                info.startExecutionLogging();
                var received = httpStepContext.getCurrentClient().send(requestBuilder.build(), bodyHandler);
                info.setLastReceived(received);
                return new Response<>(received);
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
    @Description("Http Response")
    public static <T> ResponseSequentialGetSupplier<T> response(@DescriptionFragment("request") RequestBuilder requestBuilder,
                                                                HttpResponse.BodyHandler<T> bodyHandler) {
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
    @SuppressWarnings("unchecked")
    protected void onStart(HttpStepContext httpStepContext) {
        if (toReport) {
            catchValue(getRequest().body(), createCaptors(new Class[]{AbstractRequestBodyCaptor.class}));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSuccess(HttpResponse<T> tHttpResponse) {
        if (toReport && catchSuccessEvent()) {
            catchValue(info, createCaptors(new Class[]{RequestResponseLogCaptor.class}));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onFailure(HttpStepContext httpStepContext, Throwable throwable) {
        if (toReport && catchFailureEvent()) {
            catchValue(info, createCaptors(new Class[]{RequestResponseLogCaptor.class}));
        }
    }

    ResponseExecutionInfo getInfo() {
        return info;
    }

    @Override
    public Map<String, String> getParameters() {
        var p = super.getParameters();

        var params = new LinkedHashMap<String, String>();
        params.put("Http endpoint URI", request.uri().toString());
        params.put("Http Method", request.method());

        var headerMap = request.headers().map();
        if (headerMap.size() > 0) {
            params.put("Http request Headers", headerMap.toString());
        }

        request.timeout().ifPresent(d ->
                params.put("Http Timeout", formatDurationHMS(d.toMillis())));

        params.put("Http Expect Continue", valueOf(request.expectContinue()));

        request.version().ifPresent(v ->
                params.put("Http Version", v.toString()));

        ofNullable(getRequest().body())
                .ifPresent(b -> {
                    var cls = b.getClass();
                    if (!cls.equals(JSoupDocumentBody.class)
                            && !cls.equals(SerializedBody.class)
                            && !cls.equals(StringBody.class)
                            && !cls.equals(URLEncodedForm.class)
                            && !cls.equals(W3CDocumentBody.class)
                            && !cls.equals(MultiPartBody.class)) {
                        params.put("Http Request body", b.toString());
                        return;
                    }

                    if (cls.equals(StringBody.class)) {
                        var stringBody = ((StringBody) b).body();
                        if (stringBody.length() <= 50) {
                            params.put("Http Request body", stringBody);
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
