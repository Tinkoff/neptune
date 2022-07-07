package ru.tinkoff.qa.neptune.core.api.localization.data.generation.hamcrest;

import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;

public class SomeNotDescribedMatcher<T> extends NeptuneFeatureMatcher<T> {

    protected SomeNotDescribedMatcher(boolean isNullSafe, Class[] expectedTypes) {
        super(isNullSafe, expectedTypes);
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        return false;
    }
}
