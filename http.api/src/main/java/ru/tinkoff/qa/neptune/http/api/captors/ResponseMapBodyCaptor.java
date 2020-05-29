package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.collections.MapCaptor;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class ResponseMapBodyCaptor extends MapCaptor implements BaseResponseObjectBodyCaptor<Map> {

    public ResponseMapBodyCaptor() {
        super("Response Body. Map");
    }

    @Override
    public Map<?, ?> getCaptured(Object toBeCaptured) {
        return (Map<?, ?>) getCaptured(toBeCaptured, Map.class);
    }
}
