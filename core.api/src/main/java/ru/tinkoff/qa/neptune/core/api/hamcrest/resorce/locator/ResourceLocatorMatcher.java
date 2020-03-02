package ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.net.URI;
import java.net.URL;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Abstract matcher for instances of {@link URL} and {@link URI}
 *
 * @param <T> is a type of an object to be checked. Only {@link URL} and {@link URI} are allowed for this.
 * @param <S> is a type of a part of {@link URL} and {@link URI} to be verified.
 */
public abstract class ResourceLocatorMatcher<T, S> extends TypeSafeDiagnosingMatcher<T> {

    private final String description;
    private final Function<T, S> conversion;
    private final Matcher<? super S> matcher;

    protected ResourceLocatorMatcher(String description, Matcher<? super S> matcher, Function<T, S> conversion) {
        checkArgument(isNotBlank(description), "Description is blank");
        checkNotNull(conversion, "It is not defined how to convert URL/URI");
        checkNotNull(matcher, "Matcher is not defined");
        this.description = description;
        this.conversion = conversion;
        this.matcher = matcher;
    }


    @Override
    public void describeTo(Description description) {
        description.appendText(this.description)
                .appendText(" ")
                .appendDescriptionOf(matcher);
        ;
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        var val = getTarget(item);
        var result = matcher.matches(val);

        if (!result) {
            matcher.describeMismatch(val, mismatchDescription);
        }
        return result;
    }

    private S getTarget(T item) {
        checkNotNull(item, "Value is null");
        var clazz = item.getClass();
        checkArgument(URL.class.isAssignableFrom(clazz) ||
                        URI.class.isAssignableFrom(clazz),
                format("Class of a value should be %s or %s",
                        URL.class.getName(),
                        URI.class.getName()));

        return conversion.apply(item);
    }
}
