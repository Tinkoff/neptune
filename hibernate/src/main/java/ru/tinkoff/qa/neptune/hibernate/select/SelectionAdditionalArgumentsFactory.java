package ru.tinkoff.qa.neptune.hibernate.select;

import ru.tinkoff.qa.neptune.database.abstractions.dictionary.Argument;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.InvokedMethod;
import ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByMethod;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Objects.isNull;
import static ru.tinkoff.qa.neptune.database.abstractions.data.serializer.DataSerializer.serializeObjects;

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
        serializeObjects(NON_NULL, args).forEach(s -> result.put(new Argument() + " " + result.size(), s));

        return result;
    }

    Map<String, String> getAdditionalParameters() {
        var result = new LinkedHashMap<String, String>();
        if (toRead instanceof SelectionByMethod) {
            var s = ((SelectionByMethod<?, ?>) toRead);
            result.put(new InvokedMethod().toString(), s.getInvoked().getName());
            result.putAll(convertArguments(s.getParameters()));
        }

        return result;
    }
}
