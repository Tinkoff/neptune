package ru.tinkoff.qa.neptune.core.api.localization.data.generation.hamcrest;

import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Match something")
public class SomeMatcher<T> extends NeptuneFeatureMatcher<T> {

    protected SomeMatcher(boolean isNullSafe, Class[] expectedTypes) {
        super(isNullSafe, expectedTypes);
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        return false;
    }
}
