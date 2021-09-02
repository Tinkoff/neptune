package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static org.apache.commons.lang3.StringUtils.isBlank;

@UseInjectors(CapturedStringInjector.class)
@Description("Response Body. String")
public final class ResponseStringCaptor extends AbstractResponseBodyObjectCaptor<String, StringBuilder> {

    public ResponseStringCaptor() {
        super(String.class);
    }

    @Override
    public StringBuilder getData(String caught) {
        var stringBuilder = new StringBuilder();

        if (isBlank(caught)) {
            stringBuilder.append("<EMPTY STRING>");
        } else {
            stringBuilder.append(caught);
        }

        return stringBuilder;
    }
}
