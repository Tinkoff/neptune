package ru.tinkoff.qa.neptune.core.api.hamcrest.common.only.one;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.Collection;

import static ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher.concatMatcherDescriptions;

public final class CriteriaCollectionValueGetter implements ParameterValueGetter<Collection<Matcher<?>>> {

    @Override
    public String getParameterValue(Collection<Matcher<?>> fieldValue) {
        return concatMatcherDescriptions("\r\n", fieldValue.toArray(new Matcher[]{}));
    }
}
