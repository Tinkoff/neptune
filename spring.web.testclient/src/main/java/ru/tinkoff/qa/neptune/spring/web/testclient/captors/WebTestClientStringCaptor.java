package ru.tinkoff.qa.neptune.spring.web.testclient.captors;

import org.springframework.test.web.reactive.server.ExchangeResult;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

public abstract class WebTestClientStringCaptor extends StringCaptor<ExchangeResult> {

    @Override
    public ExchangeResult getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof ExchangeResult) {
            return (ExchangeResult) toBeCaptured;
        }

        return null;
    }
}
