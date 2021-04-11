package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.http.api.request.body.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;

import static java.util.List.of;

@Description("Multipart request body. Part:")
public class MultiPartRequestBodyCaptor extends AbstractRequestBodyCaptor<MultiPartBody, BodyPart[]> {

    public MultiPartRequestBodyCaptor() {
        super(of(new BodyPartCapturedDataInjector()), of(MultiPartBody.class));
    }

    @Override
    public BodyPart[] getData(MultiPartBody caught) {
        return caught.body();
    }

    @Override
    MultiPartBody convertTo(RequestBody<?> requestBody) {
        return (MultiPartBody) requestBody;
    }
}
