package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import java.nio.file.Path;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public final class ResponseObjectsBodyCaptor extends CollectionCaptor implements BaseResponseObjectsBodyCaptor<Object> {

    public ResponseObjectsBodyCaptor() {
        super("Response Body. Objects");
    }

    @Override
    public List<?> getCaptured(Object toBeCaptured) {
        if (toBeCaptured == null) {
            return null;
        }

        return getCaptured(toBeCaptured, Object.class, o -> isLoggable(o)
                && !Path.class.isAssignableFrom(o.getClass())
                && !MappedObject.class.isAssignableFrom(o.getClass()));
    }
}
