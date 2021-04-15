package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import java.nio.file.Path;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

@Description("Response Body. Objects")
public final class ResponseObjectsBodyCaptor extends AbstractResponseBodyObjectsCaptor<Object, StringBuilder> {

    public ResponseObjectsBodyCaptor() {
        super(loadSPI(CapturedStringInjector.class), Object.class, o -> isLoggable(o)
                && !Path.class.isAssignableFrom(o.getClass())
                && !MappedObject.class.isAssignableFrom(o.getClass()));
    }

    @Override
    public StringBuilder getData(List<Object> caught) {
        return new CollectionCaptor().getData(caught);
    }
}
