package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.*;
import org.hamcrest.internal.ReflectiveTypeFinder;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;

/**
 * For matchers that check specific objects which are used/supported by Neptune.
 * This is alternative to {@link TypeSafeDiagnosingMatcher} and {@link org.hamcrest.FeatureMatcher}
 *
 * @param <T> is a type of checked objects
 */
public abstract class NeptuneFeatureMatcher<T> extends BaseMatcher<T> {

    private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("featureMatches", 1, 0);

    private final List<Description> mismatchDescriptions = new LinkedList<>();
    private final Class<?> expectedType;

    protected NeptuneFeatureMatcher() {
        this.expectedType = TYPE_FINDER.findExpectedType(getClass());
    }

    /**
     * Concatenates descriptions of matchers into one using a delimiter string.
     *
     * @param delimiter is a delimiter string
     * @param matchers  are matchers whose string descriptions should be concatenated
     * @return a concatenated string
     */
    public static String concatMatcherDescriptions(String delimiter, Matcher<?>... matchers) {
        return stream(matchers).map(matcher -> {
            if (matcher instanceof NeptuneFeatureMatcher) {
                return matcher.toString();
            }

            return translate(matcher.toString());
        }).collect(joining(delimiter));
    }

    /**
     * Concatenates descriptions of matchers into one using SPACE as delimiter string.
     *
     * @param matchers are matchers whose string descriptions should be concatenated
     * @return a concatenated string
     */
    public static String concatMatcherDescriptions(Matcher<?>... matchers) {
        return concatMatcherDescriptions(SPACE, matchers);
    }

    protected boolean prerequisiteChecking(Object actual) {
        if (actual == null) {
            appendMismatchDescription(new NullValueMismatch());
            return false;
        }

        if (!expectedType.isInstance(actual)) {
            appendMismatchDescription(new TypeMismatch(expectedType, actual.getClass()));
            return false;
        }

        return true;
    }

    @Override
    public boolean matches(Object actual) {
        return prerequisiteChecking(actual) && checkFeature(actual);
    }

    @SuppressWarnings("unchecked")
    protected boolean checkFeature(Object actual) {
        mismatchDescriptions.clear();
        boolean result = false;
        try {
            result = featureMatches((T) actual);
        } catch (Throwable t) {
            appendMismatchDescription(new SomethingWentWrongDescriber(t));
        }
        return result;
    }

    protected abstract boolean featureMatches(T toMatch);

    protected void appendMismatchDescription(MismatchDescriber describer) {
        mismatchDescriptions.add(new StringDescription().appendText(describer.toString()));
    }

    protected void appendMismatchDescription(Description description) {
        mismatchDescriptions.add(description);
    }

    @Override
    public final void describeTo(Description description) {
        description.appendText(toString());
    }

    public String toString() {
        return translate(this);
    }

    @Override
    public final void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriptions.stream().map(Object::toString).collect(joining("\r\n")));
    }
}
