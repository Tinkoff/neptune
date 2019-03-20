package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Link;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;

public final class HasReference extends TypeSafeDiagnosingMatcher<Link> {

    private final Matcher<? super String> refMatcher;

    private HasReference(Matcher<? super String> refMatcher) {
        checkArgument(nonNull(refMatcher), "Criteria to match reference should be defined");
        this.refMatcher = refMatcher;
    }

    /**
     * Creates an instance of {@link HasReference} that checks references of {@link Link}. The checked link is
     * expected to have a reference that equals to {@code expected} value.
     *
     * @param expected is expected value of the reference
     * @return an instance of {@link HasReference}
     */
    public static HasReference hasReference(String expected) {
        checkArgument(nonNull(expected), "Expected value of the reference should be defined");
        return new HasReference(equalTo(expected));
    }

    /**
     * Creates an instance of {@link HasReference} that checks references of {@link Link}. The checked link is
     * expected to have a reference that equals to {@code expected} value.
     *
     * @param expected is expected value of the reference
     * @return an instance of {@link HasReference}
     */
    public static HasReference hasReference(URL expected) {
        checkArgument(nonNull(expected), "Expected value of the reference should be defined");
        return hasReference(expected.toString());
    }

    /**
     * Creates an instance of {@link HasReference} that checks references of {@link Link}. The checked link is
     * expected to have a reference that meets the criteria defined by {@code refMatcher}.
     *
     * @param refMatcher is a criteria to check the reference
     * @return an instance of {@link HasReference}
     */
    public static HasReference hasReference(Matcher<? super String> refMatcher) {
        return new HasReference(refMatcher);
    }

    @Override
    protected boolean matchesSafely(Link item, Description mismatchDescription) {
        var reference = item.getReference();
        var result = refMatcher.matches(reference);

        if (!result) {
            mismatchDescription.appendText(format("It was expected that %s %s\n", item, toString()));
            refMatcher.describeMismatch(reference, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("has reference '%s'", refMatcher);
    }
}
