package ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions;

import org.apache.commons.lang3.ArrayUtils;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

public class ArgParameterValueGetter implements ParameterValueGetter<Object[]> {

    @Override
    public String getParameterValue(Object[] fieldValue) {
        return ArrayUtils.toString(fieldValue);
    }
}
