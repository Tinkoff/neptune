package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.http.api.dto.DTObject;

import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

public final class ResponseObjectsBodyCaptor extends CollectionCaptor implements BaseResponseObjectsBodyCaptor<Object> {

    public ResponseObjectsBodyCaptor() {
        super("Response Body. Objects");
    }

    @Override
    public List<?> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, Object.class, o -> isLoggable(o)
                &&
                !DTObject.class.isAssignableFrom(o.getClass()));
    }
}
