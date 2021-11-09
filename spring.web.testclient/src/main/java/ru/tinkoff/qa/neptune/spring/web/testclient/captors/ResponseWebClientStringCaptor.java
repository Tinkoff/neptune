package ru.tinkoff.qa.neptune.spring.web.testclient.captors;

import org.springframework.test.web.reactive.server.ExchangeResult;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Response")
public final class ResponseWebClientStringCaptor extends WebTestClientStringCaptor {
    @Override
    public StringBuilder getData(ExchangeResult caught) {
        var sb = new StringBuilder("Status: ").append(caught.getRawStatusCode());
        var headers = caught.getResponseHeaders();
        headers.forEach((k, v) -> sb.append("\r\n").append(k).append(": ").append(String.join(";", v)));
        return sb;
    }
}
