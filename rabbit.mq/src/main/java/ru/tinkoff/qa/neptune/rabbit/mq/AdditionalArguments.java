package ru.tinkoff.qa.neptune.rabbit.mq;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameterPojo;

import java.util.HashMap;

public final class AdditionalArguments implements StepParameterPojo {
    @StepParameter(value = "hashMap", doNotReportNullValues = true, makeReadableBy = AdditionalArgumentsGetParameterValue.class)
    private HashMap<String, Object> hashMap;

    public static AdditionalArguments arguments(){
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
