package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.http.api.request.body.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;

import static java.util.List.of;

@Description("Multipart request body. Part:")
public class MultiPartRequestBodyCaptor extends Captor<MultiPartBody, BodyPart[]> implements BaseRequestBodyCaptor {

    public MultiPartRequestBodyCaptor() {
        super(of(new BodyPartCapturedDataInjector()));
    }

    @Override
    public BodyPart[] getData(MultiPartBody caught) {
        return caught.body();
    }

    @Override
    public MultiPartBody getCaptured(Object toBeCaptured) {
        return (MultiPartBody) getCaptured(toBeCaptured, of(MultiPartBody.class));
    }
}
