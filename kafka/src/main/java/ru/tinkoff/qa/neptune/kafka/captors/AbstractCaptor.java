package ru.tinkoff.qa.neptune.kafka.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class AbstractCaptor extends StringCaptor<List<String>> {
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

        if (result.isEmpty()) {
            return null;
        }

        return result;
    }

    @Override
    public StringBuilder getData(List<String> list) {
        var stringBuilder = new StringBuilder();

        list.forEach(item -> {
            stringBuilder
                    .append(item)
                    .append("\r\n")
                    .append("\r\n");
        });

        return stringBuilder;
    }
}
