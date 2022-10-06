package ru.tinkoff.qa.neptune.allure.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@PropertyDescription(description = {
    "Defines parameters to be masked in Allure report",
    "These parameters are defined by a comma-separated string",
    "Property value may include full parameter names",
    "as well as parts of parameter names or regular expressions"
},
    section = "Allure settings")
@PropertyName("MASKED_PARAMETERS")
public final class MaskedParametersProperty implements PropertySupplier<List<String>, List<String>> {

    public static final MaskedParametersProperty MASKED_PARAMETERS = new MaskedParametersProperty();

    private MaskedParametersProperty() {
        super();
    }

    @Override
    public List<String> parse(String value) {
        return stream(value.split(",")).map(String::trim).collect(toList());
    }

    @Override
    public String readValuesToSet(List<String> value) {
        return value.stream().map(String::trim).collect(joining(","));
    }

    @Override
    public List<String> returnIfNull() {
        return List.of();
    }
}
