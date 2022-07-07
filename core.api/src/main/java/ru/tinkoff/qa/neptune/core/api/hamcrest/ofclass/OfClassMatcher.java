package ru.tinkoff.qa.neptune.core.api.hamcrest.ofclass;

import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.is;

@Description("is object of class '{clazz}'")
public final class OfClassMatcher<T> extends NeptuneFeatureMatcher<Object> {

    @DescriptionFragment("clazz")
    private final Class<? extends T> clazz;

    private OfClassMatcher(Class<? extends T> clazz) {
        super(true);
        checkNotNull(clazz);
        this.clazz = clazz;
    }

    public static <T> OfClassMatcher<T> isObjectOfClass(Class<T> clazz) {
        return new OfClassMatcher<>(clazz);
    }

    @Override
    protected boolean featureMatches(Object toMatch) {
        var ofClass = is(clazz);
        var result = ofClass.matches(toMatch.getClass());

        if (!result) {
            appendMismatchDescription(new PropertyValueMismatch(new ClassOfObject(), toMatch.getClass(), ofClass));
        }

        return result;
    }
}
