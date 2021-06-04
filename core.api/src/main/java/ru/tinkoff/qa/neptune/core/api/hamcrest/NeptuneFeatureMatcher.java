package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.*;
import org.hamcrest.internal.ReflectiveTypeFinder;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
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

    protected final List<Description> mismatchDescriptions = new LinkedList<>();
    protected final Class<? extends T>[] expectedTypes;
    protected final boolean isNullSafe;

    @SafeVarargs
    protected NeptuneFeatureMatcher(boolean isNullSafe, Class<? extends T>... expectedTypes) {
        checkNotNull(expectedTypes);
        checkArgument(expectedTypes.length > 0, "At least one expected type should be defined");
        this.isNullSafe = isNullSafe;
        this.expectedTypes = expectedTypes;
    }

    @SuppressWarnings("unchecked")
    protected NeptuneFeatureMatcher(boolean isNullSafe) {
        this.isNullSafe = isNullSafe;
        this.expectedTypes = new Class[]{TYPE_FINDER.findExpectedType(getClass())};
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
                if (matcher.getClass().getAnnotation(UnitesCriteria.class) == null) {
                    return matcher.toString();
                }
                return "(" + matcher + ")";
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
        if (actual == null && isNullSafe) {
            appendMismatchDescription(new NullValueMismatch());
            return false;
        }

        if (actual == null) {
            return true;
        }

        if (stream(expectedTypes).noneMatch(c -> c.isInstance(actual))) {
            appendMismatchDescription(new TypeMismatch(actual.getClass(), expectedTypes));
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
        mismatchDescriptions.add(new StringDescription().appendText(translate(description.toString())));
    }

    protected final void appendMismatchDescription(Matcher<?> matcher, Object o) {
        var d = new StringDescription();
        matcher.describeMismatch(o, d);
        appendMismatchDescription(d);
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
        mismatchDescription
                .appendText(mismatchDescriptions
                        .stream()
                        .map(Object::toString)
                        .distinct()
                        .collect(joining("\r\n")));
    }
}
