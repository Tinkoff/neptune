package ru.tinkoff.qa.neptune.retrofit2.steps;

import ru.tinkoff.qa.neptune.retrofit2.captors.AbstractResponseBodyCaptor;
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

            headerMap.forEach((k, v) -> result.put("Header " + k, String.join(",", v)));
            return result;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected void onSuccess() {
        var r = f.request();
        if (r != null) {
            catchValue(r.body(), createCaptors(new Class[]{AbstractResponseBodyCaptor.class}));
        }

        if (catchSuccessEvent()) {
            catchValue(f.response(), createCaptors(new Class[]{ResponseCaptor.class}));
            catchValue(f.response().body(), createCaptors(new Class[]{AbstractResponseBodyCaptor.class}));
        }
    }

    @SuppressWarnings("unchecked")
    protected void onFailure() {
        var r = f.request();
        if (r != null) {
            catchValue(r.body(), createCaptors(new Class[]{AbstractResponseBodyCaptor.class}));
        }

        if (catchFailureEvent()) {
            catchValue(f.response(), createCaptors(new Class[]{ResponseCaptor.class}));
            catchValue(f.response().body(), createCaptors(new Class[]{AbstractResponseBodyCaptor.class}));
        }
    }
}
