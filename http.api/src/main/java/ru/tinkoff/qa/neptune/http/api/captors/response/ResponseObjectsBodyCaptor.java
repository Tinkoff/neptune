package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import java.nio.file.Path;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@Description("Response Body. Objects")
public final class ResponseObjectsBodyCaptor extends CollectionCaptor implements BaseResponseObjectsBodyCaptor<Object> {

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
