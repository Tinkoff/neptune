package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;
import ru.tinkoff.qa.neptune.http.api.request.body.StringBody;

import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

@Description("Request string body")
public final class StringRequestBodyCaptor extends AbstractRequestBodyCaptor<StringBody, StringBuilder> {

    public StringRequestBodyCaptor() {
        super(loadSPI(CapturedStringInjector.class), of(StringBody.class));
    }

    @Override
    public StringBuilder getData(StringBody caught) {
        return new StringBuilder(caught.body());
    }

    @Override
    StringBody convertTo(RequestBody<?> requestBody) {
        var stringBody = (StringBody) requestBody;

        return ofNullable(stringBody)
                .map(b -> {
                    if (b.body().length() > 50) {
                        return b;
                    }
                    return null;
                }).orElse(null);
    }
}
