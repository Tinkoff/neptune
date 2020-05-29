package ru.tinkoff.qa.neptune.http.api.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil;

import java.util.List;

public class ResponseObjectsBodyCaptor extends CollectionCaptor implements BaseResponseObjectsBodyCaptor<Object> {

    public ResponseObjectsBodyCaptor() {
        super("Response Body. Objects");
    }

    @Override
    public List<?> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, Object.class, IsLoggableUtil::isLoggable);
    }
}
