package ru.tinkoff.qa.neptune.spring.web.testclient.captors;

import org.springframework.test.web.reactive.server.ExchangeResult;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Response body")
public final class ResponseBodyWebClientStringCaptor extends WebTestClientStringCaptor {
    @Override
    public StringBuilder getData(ExchangeResult caught) {
        var content = caught.getResponseBodyContent();
        if (content == null || content.length == 0) {
            return null;
        }
        return new StringBuilder(new String(content));
    }
}
