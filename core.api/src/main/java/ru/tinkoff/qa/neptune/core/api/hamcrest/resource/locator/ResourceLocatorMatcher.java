package ru.tinkoff.qa.neptune.core.api.hamcrest.resource.locator;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract matcher for instances of {@link URL} and {@link URI}
 *
 * @param <T> is a type of an object to be checked. Only {@link URL} and {@link URI} are allowed for this.
 * @param <S> is a type of a part of {@link URL} and {@link URI} to be verified.
 */
abstract class ResourceLocatorMatcher<T, S> extends NeptuneFeatureMatcher<T> {

    protected final static String MATCHER_FRAGMENT = "matcher";

    private final Function<T, S> conversion;
    @DescriptionFragment(value = MATCHER_FRAGMENT, makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super S> matcher;

    protected ResourceLocatorMatcher(Class<T> cls, Matcher<? super S> matcher, Function<T, S> conversion) {
        super(true, cls);
        checkNotNull(conversion, "It is not defined how to convert URL/URI");
        checkNotNull(matcher, "Matcher is not defined");
        this.conversion = conversion;
        this.matcher = matcher;
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var val = conversion.apply(toMatch);
        var result = matcher.matches(conversion.apply(toMatch));

        if (!result) {
            appendMismatchDescription(matcher, val);
        }
        return result;
    }
}
