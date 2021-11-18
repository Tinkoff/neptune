package ru.tinkoff.qa.neptune.spring.web.testclient.captors;

import org.springframework.test.web.reactive.server.ExchangeResult;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Request and response")
public class RequestExchangeResultCaptor extends StringCaptor<ExchangeResult> {

    @Override
    public ExchangeResult getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof ExchangeResult) {
            return (ExchangeResult) toBeCaptured;
        }

        return null;
    }

    @Override
    public StringBuilder getData(ExchangeResult caught) {
        return new StringBuilder(caught.toString());
    }
}
