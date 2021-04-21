package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.http.api.request.body.JSoupDocumentBody;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.request.body.SerializedBody;
import ru.tinkoff.qa.neptune.http.api.request.body.W3CDocumentBody;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.URLEncodedForm;

import static java.util.List.of;
import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

@Description("Request body.")
public class CommonRequestBodyCaptor extends AbstractRequestBodyCaptor<RequestBody<?>, StringBuilder> {

    public CommonRequestBodyCaptor() {
        super(loadSPI(CapturedStringInjector.class), of(JSoupDocumentBody.class,
                SerializedBody.class,
                URLEncodedForm.class,
                W3CDocumentBody.class));
    }

    @Override
    public StringBuilder getData(RequestBody<?> caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    RequestBody<?> convertTo(RequestBody<?> requestBody) {
        return requestBody;
    }
}
