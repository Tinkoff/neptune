package ru.tinkoff.qa.neptune.spring.data.select;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.tinkoff.qa.neptune.spring.data.select.by.SelectionByMethod;
import ru.tinkoff.qa.neptune.spring.data.select.dictionary.Argument;
import ru.tinkoff.qa.neptune.spring.data.select.dictionary.HowToSelect;
import ru.tinkoff.qa.neptune.spring.data.select.dictionary.InvokedMethod;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.lang.String.valueOf;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;

final class SelectionAdditionalArgumentsFactory {

    private final Object toRead;

    SelectionAdditionalArgumentsFactory(Object toRead) {
        this.toRead = toRead;
    }

    private static Map<String, String> convertArguments(Object[] args) {
        if (isNull(args) || args.length == 0) {
            return Map.of();
        }

        var result = new LinkedHashMap<String, String>();
        stream(args)
                .map(o -> {
                    if (isLoggable(o)) {
                        return valueOf(o);
                    }

                    try {
                        return new ObjectMapper()
                                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S"))
                                .setSerializationInclusion(NON_NULL)
                                .writeValueAsString(o);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return "could not serialize value";
                    }
                }).forEach(s -> {
                    result.put(new Argument() + " " + result.size(), s);
                });

        return result;
    }

    Map<String, String> getAdditionalParameters() {
        var result = new LinkedHashMap<String, String>();
        if (toRead instanceof SelectionByMethod) {
            var s = ((SelectionByMethod<?, ?, ?, ?>) toRead);
            result.put(new InvokedMethod().toString(), s.getInvoked().toString());
            result.putAll(convertArguments(s.getParameters()));
        } else {
            result.put(new HowToSelect().toString(), valueOf(toRead));
        }

        return result;
    }
}
