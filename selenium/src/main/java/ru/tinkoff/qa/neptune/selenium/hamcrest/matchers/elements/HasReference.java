package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Link;

import java.net.URL;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;

@Description("reference: {refMatcher}")
public final class HasReference extends NeptuneFeatureMatcher<Link> {

    @DescriptionFragment(value = "refMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super String> refMatcher;

    private HasReference(Matcher<? super String> refMatcher) {
        super(true);
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
    public static Matcher<Link> hasReference(String expected) {
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
    public static Matcher<Link> hasReference(URL expected) {
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
    public static Matcher<Link> hasReference(Matcher<? super String> refMatcher) {
        return new HasReference(refMatcher);
    }

    @Override
    protected boolean featureMatches(Link toMatch) {
        var reference = toMatch.getReference();
        var result = refMatcher.matches(reference);

        if (!result) {
            appendMismatchDescription(refMatcher, reference);
        }

        return result;
    }
}
