package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasSize;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.Height;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.Width;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;

@Description("size width {widthMatcher} and height {heightMatcher}")
public final class HasSizeMatcher extends NeptuneFeatureMatcher<SearchContext> {

    @DescriptionFragment(value = "widthMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> widthMatcher;
    @DescriptionFragment(value = "heightMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> heightMatcher;

    private HasSizeMatcher(Matcher<Integer> widthMatcher, Matcher<Integer> heightMatcher) {
        super(true, WebElement.class, Widget.class);
        checkArgument(nonNull(widthMatcher), "Criteria to check width should be defined");
        checkArgument(nonNull(heightMatcher), "Criteria to check height should be defined");
        this.widthMatcher = widthMatcher;
        this.heightMatcher = heightMatcher;
    }

    /**
     * Creates an instance of {@link HasSizeMatcher} that checks size of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasSize}.
     * Otherwise the matching returns {@code false}.
     *
     * @param width  expected width of the element
     * @param height expected height of the element
     * @return instance of {@link HasSizeMatcher}
     */
    public static Matcher<SearchContext> hasDimensionalSize(int width, int height) {
        return hasDimensionalSize(equalTo(width), equalTo(height));
    }

    /**
     * Creates an instance of {@link HasSizeMatcher} that checks size of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasSize}.
     * Otherwise the matching returns {@code false}.
     *
     * @param width         expected width of the element
     * @param heightMatcher height criteria
     * @return instance of {@link HasSizeMatcher}
     */
    public static Matcher<SearchContext> hasDimensionalSize(int width, Matcher<Integer> heightMatcher) {
        return hasDimensionalSize(equalTo(width), heightMatcher);
    }

    /**
     * Creates an instance of {@link HasSizeMatcher} that checks size of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasSize}.
     * Otherwise the matching returns {@code false}.
     *
     * @param widthMatcher width criteria
     * @param height       expected height of the element
     * @return instance of {@link HasSizeMatcher}
     */
    public static Matcher<SearchContext> hasDimensionalSize(Matcher<Integer> widthMatcher, int height) {
        return hasDimensionalSize(widthMatcher, equalTo(height));
    }

    /**
     * Creates an instance of {@link HasSizeMatcher} that checks size of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasSize}.
     * Otherwise the matching returns {@code false}.
     *
     * @param widthMatcher  width criteria
     * @param heightMatcher height criteria
     * @return instance of {@link HasSizeMatcher}
     */
    public static Matcher<SearchContext> hasDimensionalSize(Matcher<Integer> widthMatcher,
                                                            Matcher<Integer> heightMatcher) {
        return new HasSizeMatcher(widthMatcher, heightMatcher);
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        Dimension size;
        var clazz = toMatch.getClass();

        if (WebElement.class.isAssignableFrom(clazz)) {
            size = ((WebElement) toMatch).getSize();
        } else {
            size = ((HasSize) toMatch).getSize();
        }

        var result = true;

        var width = size.getWidth();
        var height = size.getHeight();

        if (!widthMatcher.matches(width)) {
            appendMismatchDescription(new PropertyValueMismatch(new Width(), width, widthMatcher));
            result = false;
        }

        if (!heightMatcher.matches(height)) {
            appendMismatchDescription(new PropertyValueMismatch(new Height(), height, heightMatcher));
            result = false;
        }

        return result;
    }
}
