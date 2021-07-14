package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.retrofit2.captors.AbstractRequestBodyCaptor;
import ru.tinkoff.qa.neptune.retrofit2.captors.MultipartRequestBodyCaptor;
import ru.tinkoff.qa.neptune.retrofit2.captors.ResponseBodyCaptor;
import ru.tinkoff.qa.neptune.retrofit2.captors.ResponseCaptor;

import java.util.LinkedHashMap;
import java.util.Map;

import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;

final class StepExecutionHook {

    private final GetStepResultFunction<?, ?> f;

    StepExecutionHook(GetStepResultFunction<?, ?> f) {
        this.f = f;
    }

    Map<String, String> getRequestParameters() {
        var r = f.request();
        if (r != null) {
            var result = new LinkedHashMap<String, String>();
            result.put("URL", r.url().toString());
            var h = r.headers();
            var headerMap = h.toMultimap();

            headerMap.forEach((k, v) -> result.put(k, String.join(",", v)));
            return result;
        }

        return null;
    }

    protected void onSuccess() {
        catchRequestsAndResponses(catchSuccessEvent());
    }

    protected void onFailure() {
        catchRequestsAndResponses(catchFailureEvent());
    }

    @SuppressWarnings("unchecked")
    private void catchRequestsAndResponses(boolean condition) {
        var r = f.request();
        if (r != null) {
            catchValue(r.body(), createCaptors(new Class[]{AbstractRequestBodyCaptor.class, MultipartRequestBodyCaptor.class}));
        }

        if (condition) {
            catchValue(f.response(), createCaptors(new Class[]{ResponseCaptor.class}));
            catchValue(f.body(), createCaptors(new Class[]{ResponseBodyCaptor.class}));
        }
    }
}
