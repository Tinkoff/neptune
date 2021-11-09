package ru.tinkoff.qa.neptune.spring.web.testclient.captors;

import org.springframework.test.web.reactive.server.ExchangeResult;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Request body")
public final class RequestBodyWebClientStringCaptor extends WebTestClientStringCaptor {
    @Override
    public StringBuilder getData(ExchangeResult caught) {
        var content = caught.getRequestBodyContent();
        if (content == null || content.length == 0) {
            return null;
        }
        return new StringBuilder(new String(content));
    }
}
