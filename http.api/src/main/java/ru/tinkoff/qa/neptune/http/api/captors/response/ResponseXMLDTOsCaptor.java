package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.collections.CollectionCaptor;
import ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject;

import java.util.List;

public final class ResponseXMLDTOsCaptor extends CollectionCaptor implements BaseResponseObjectsBodyCaptor<XmlDTObject> {

    public ResponseXMLDTOsCaptor() {
        super("Response Body. Dto. Array/collection of XML elements");
    }

    @Override
    public List<XmlDTObject> getCaptured(Object toBeCaptured) {
        return getCaptured(toBeCaptured, XmlDTObject.class);
    }
}
