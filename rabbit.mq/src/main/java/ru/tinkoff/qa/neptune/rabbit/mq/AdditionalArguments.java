package ru.tinkoff.qa.neptune.rabbit.mq;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public final class AdditionalArguments implements StepParameterPojo {
    private HashMap<String, Object> hashMap = new LinkedHashMap<>();

    @Override
    public Map<String, String> getParameters() {
        var map = StepParameterPojo.super.getParameters();
        hashMap.forEach((k, v) -> map.put(k, valueOf(v)));
        return map;
    }

    public static AdditionalArguments arguments() {
        return new AdditionalArguments();
    }

    public HashMap<String, Object> getHashMap() {
        return hashMap;
    }

    public AdditionalArguments setArgument(String argumentKey, Object argumentValue) {
        this.hashMap.put(argumentKey, argumentValue);
        return this;
    }
}
