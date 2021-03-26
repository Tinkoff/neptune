package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.collections.MapCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;

import java.util.Map;

@SuppressWarnings("rawtypes")
@Description("Response Body. Map")
public final class ResponseMapBodyCaptor extends MapCaptor implements BaseResponseObjectBodyCaptor<Map> {

    @Override
    public Map<?, ?> getCaptured(Object toBeCaptured) {
        return (Map<?, ?>) getCaptured(toBeCaptured, Map.class);
    }
}
