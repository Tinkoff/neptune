package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

@Description("Response Body. DTO. Described by json-formatted string")
@UseInjectors(CapturedStringInjector.class)
public final class ResponseDTOCaptor extends AbstractResponseBodyObjectCaptor<MappedObject, StringBuilder> {

    public ResponseDTOCaptor() {
        super(MappedObject.class);
    }

    @Override
    public StringBuilder getData(MappedObject caught) {
        try {
            return new StringBuilder(caught.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
