package ru.tinkoff.qa.neptune.spring.data.captors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.core.api.event.firing.collections.IterableCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static java.lang.String.valueOf;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static org.apache.commons.collections.IteratorUtils.toList;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

@SuppressWarnings("unchecked")
@Description("Entity(ies)")
public class EntitiesCaptor extends IterableCaptor<List<String>> {

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
        if (!(toBeCaptured instanceof Iterable)) {
            toLog = of(toBeCaptured);
        } else {
            toLog = toList(((Iterable<Object>) toBeCaptured).iterator());
        }

        return toLog
                .stream()
                .map(o -> {
                    if (isLoggable(o)) {
                        return valueOf(o);
                    }

                    try {
                        return new ObjectMapper()
                                .setSerializationInclusion(ALWAYS)
                                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S"))
                                .writeValueAsString(o);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return "could not serialize value";
                    }
                }).collect(Collectors.toList());
    }
}
