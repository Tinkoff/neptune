package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.body.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.request.body.SerializedBody;
import ru.tinkoff.qa.neptune.http.api.request.body.StringBody;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.URLEncodedForm;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;

final class RequestParameters implements StepParameterPojo {

    private final NeptuneHttpRequestImpl request;

    public RequestParameters(NeptuneHttpRequestImpl request) {
        this.request = request;
    }

    @Override
    public Map<String, String> getParameters() {
        var params = new LinkedHashMap<String, String>();
        params.put("Http endpoint URI", request.uri().toString());
        params.put("Http Method", request.method());

        var headerMap = request.headers().map();
        if (!headerMap.isEmpty()) {
            params.put("Http request Headers", headerMap.toString());
        }

        request.timeout().ifPresent(d ->
                params.put("Http Timeout", formatDurationHMS(d.toMillis())));

        params.put("Http Expect Continue", valueOf(request.expectContinue()));

        request.version().ifPresent(v ->
                params.put("Http Version", v.toString()));

        ofNullable(request.body())
                .ifPresent(b -> {
                    var cls = b.getClass();
                    if (!cls.equals(SerializedBody.class)
                            && !cls.equals(StringBody.class)
                            && !cls.equals(URLEncodedForm.class)
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

        return params;
    }
}
