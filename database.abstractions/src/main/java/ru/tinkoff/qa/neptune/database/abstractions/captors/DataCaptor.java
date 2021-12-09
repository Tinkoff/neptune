package ru.tinkoff.qa.neptune.database.abstractions.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.collections.IterableCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.util.Arrays.stream;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static org.apache.commons.collections.IteratorUtils.toList;
import static ru.tinkoff.qa.neptune.database.abstractions.data.serializer.DataSerializer.serializeObjects;

@SuppressWarnings("unchecked")
@Description("Resulted data")
public class DataCaptor extends IterableCaptor<List<String>> {

    @Override
    public StringBuilder getData(List<String> caught) {
        var result = super.getData(caught);
        result.append("\r\n")
                .append("\r\n")
                .append("================================")
                .append("\r\n").append("Total:").append(caught.size());

        return result;
    }

    @Override
    public List<String> getCaptured(Object toBeCaptured) {
        if (isNull(toBeCaptured)) {
            return null;
        }

        List<Object> toLog;
        if (toBeCaptured instanceof Iterable) {
            toLog = toList(((Iterable<Object>) toBeCaptured).iterator());
        } else if (toBeCaptured.getClass().isArray()) {
            toLog = stream((Object[]) toBeCaptured).collect(Collectors.toList());
        } else {
            toLog = of(toBeCaptured);
        }

        return serializeObjects(ALWAYS, toLog).collect(Collectors.toList());
    }
}
