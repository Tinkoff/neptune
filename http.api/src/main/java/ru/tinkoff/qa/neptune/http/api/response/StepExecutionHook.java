package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.http.api.captors.request.AbstractRequestBodyCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.AbstractResponseBodyObjectsCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.RequestResponseLogCaptor;
import ru.tinkoff.qa.neptune.http.api.captors.response.ResponseCaptor;

import java.util.LinkedHashMap;
import java.util.Map;

import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.catchValue;
import static ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptorUtil.createCaptors;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchFailureEvent;
import static ru.tinkoff.qa.neptune.core.api.properties.general.events.DoCapturesOf.catchSuccessEvent;

final class StepExecutionHook {

    private final ResponseExecutionInfo info;
    private final ResponseSequentialGetSupplier<?> getResponse;

    StepExecutionHook(ResponseExecutionInfo info, ResponseSequentialGetSupplier<?> getResponse) {
        this.info = info;
        this.getResponse = getResponse;
    }

    public Map<String, String> getParameters() {
        return new LinkedHashMap<>(getResponse.getParameters());
    }

    @SuppressWarnings("unchecked")
    protected void onStart() {
        catchValue(getResponse.getRequest().body(), createCaptors(new Class[]{AbstractRequestBodyCaptor.class}));
    }

    protected void onSuccess() {
        catchRequestsAndResponses(catchSuccessEvent());
    }

    protected void onFailure() {
        catchRequestsAndResponses(catchFailureEvent());
    }

    @SuppressWarnings("unchecked")
    private void catchRequestsAndResponses(boolean condition) {
        if (condition) {
            catchValue(info, createCaptors(new Class[]{RequestResponseLogCaptor.class}));
            catchValue(info.getLastReceived(), createCaptors(new Class[]{ResponseCaptor.class,
                    AbstractResponseBodyObjectCaptor.class,
                    AbstractResponseBodyObjectsCaptor.class}));
        }
    }
}
