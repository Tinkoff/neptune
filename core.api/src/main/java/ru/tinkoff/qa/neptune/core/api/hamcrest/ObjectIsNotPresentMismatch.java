package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Not present {objectName}: {characteristics}")
public final class ObjectIsNotPresentMismatch extends MismatchDescriber {

    @DescriptionFragment("objectName")
    final Object matchObjectName;

    @DescriptionFragment(value = "characteristics")
    final Matcher<?> matcher;

    private ObjectIsNotPresentMismatch(Object matchObjectName, Matcher<?> matcher) {
        this.matchObjectName = matchObjectName;
        this.matcher = matcher;
    }

    public ObjectIsNotPresentMismatch(String matchObjectName, Matcher<?> matcher) {
        this((Object) matchObjectName, matcher);
    }

    public ObjectIsNotPresentMismatch(MatchObjectName matchObjectName, Matcher<?> matcher) {
        this((Object) matchObjectName, matcher);
    }
}
