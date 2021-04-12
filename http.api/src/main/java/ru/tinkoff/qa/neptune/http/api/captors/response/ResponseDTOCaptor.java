package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.http.api.mapping.MappedObject;

import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

@Description("Response Body. DTO. Described by json-formatted string")
public final class ResponseDTOCaptor extends AbstractResponseBodyObjectCaptor<MappedObject, StringBuilder> {

    public ResponseDTOCaptor() {
        super(loadSPI(CapturedStringInjector.class), MappedObject.class);
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
