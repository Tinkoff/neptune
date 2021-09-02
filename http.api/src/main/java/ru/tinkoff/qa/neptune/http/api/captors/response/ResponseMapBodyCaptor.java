package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.MapCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.Map;

@UseInjectors(CapturedStringInjector.class)
@SuppressWarnings("rawtypes")
@Description("Response Body. Map")
public final class ResponseMapBodyCaptor extends AbstractResponseBodyObjectCaptor<Map, StringBuilder> {

    public ResponseMapBodyCaptor() {
        super(Map.class);
    }

    @Override
    public StringBuilder getData(Map caught) {
        return new MapCaptor().getData(caught);
    }
}
