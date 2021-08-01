package ru.tinkoff.qa.neptune.rabbit.mq.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Description("Read RabbitMQ messages")
public final class MessagesCaptor extends StringCaptor<List<String>> {

    @Override
    public List<String> getCaptured(Object toBeCaptured) {
        if (!(toBeCaptured instanceof Collection)) {
            return null;
        }

        var result = ((Collection<?>) toBeCaptured)
                .stream()
                .filter(o -> o instanceof String && isNotBlank((String) o))
                .map(o -> (String) o)
                .collect(toList());

        if (result.size() == 0) {
            return null;
        }

        return result;
    }

    @Override
    public StringBuilder getData(List<String> caught) {
        var result = new StringBuilder();

        for (var i = 0; i < caught.size(); i++) {
            if (i > 0) {
                result.append("\r\n")
                        .append("\r\n");
            }
            result.append("#")
                    .append(i + 1)
                    .append("\r\n")
                    .append("\r\n")
                    .append(caught.get(i));
        }

        return result;
    }
}
