package ru.tinkoff.qa.neptune.core.api.hamcrest.common;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("ANYTHING")
public final class AnyThingMatcher<T> extends NeptuneFeatureMatcher<T> {

    private AnyThingMatcher() {
        super(false);
    }

    public static <T> Matcher<T> anything() {
        return new AnyThingMatcher<>();
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        return true;
    }
}
