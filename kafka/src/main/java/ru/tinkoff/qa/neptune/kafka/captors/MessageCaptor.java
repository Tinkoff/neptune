package ru.tinkoff.qa.neptune.kafka.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Description("Kafka message")
public final class MessageCaptor extends StringCaptor<String> {

    @Override
    public String getCaptured(Object toBeCaptured) {
        if (toBeCaptured instanceof String && isNotBlank((String) toBeCaptured)) {
            return (String) toBeCaptured;
        }

        return null;
    }

    @Override
    public StringBuilder getData(String caught) {
        return new StringBuilder(caught);
    }
}
