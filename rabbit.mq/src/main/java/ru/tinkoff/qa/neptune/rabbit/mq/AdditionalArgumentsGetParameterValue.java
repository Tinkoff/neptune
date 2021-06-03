package ru.tinkoff.qa.neptune.rabbit.mq;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.rabbit.mq.AdditionalArguments;

public class AdditionalArgumentsGetParameterValue implements ParameterValueGetter<AdditionalArguments> {
    @Override
    public String getParameterValue(AdditionalArguments fieldValue) {
        return fieldValue
                .getHashMap()
                .toString()
                .replace("{","")
                .replace("}","")
                .replace(":","=")
                .replace(",","\r\n")
                .replace(" ", "");
    }
}
