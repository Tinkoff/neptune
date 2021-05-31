package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher.concatMatcherDescriptions;

public final class AllParameterValueGetter implements ParameterValueGetter<Matcher<?>[]> {

    @Override
    public String getParameterValue(Matcher<?>[] fieldValue) {
        return concatMatcherDescriptions(", ", fieldValue);
    }
}
