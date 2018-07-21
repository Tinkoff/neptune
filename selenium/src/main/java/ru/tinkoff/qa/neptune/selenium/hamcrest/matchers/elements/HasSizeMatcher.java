package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import ru.tinkoff.qa.neptune.selenium.api.widget.HasSize;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.*;

public final class HasSizeMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<Integer> widthMatcher;
    private final Matcher<Integer> heightMatcher;

    private HasSizeMatcher(Matcher<Integer> widthMatcher, Matcher<Integer> heightMatcher) {
        checkArgument(widthMatcher != null, "Criteria to check width should be defined");
        checkArgument(heightMatcher != null, "Criteria to check height should be defined");
        this.widthMatcher = widthMatcher;
        this.heightMatcher = heightMatcher;
    }

    /**
     * Creates an instance of {@link HasSizeMatcher} which checks size of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasSize} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}.
     *
     * @param width expected width of the element
     * @param height expected height of the element
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasSize}.
     * @return instance of {@link HasSizeMatcher}
     */
    public static <T extends SearchContext> HasSizeMatcher<T> hasDimensionalSize(int width, int height) {
        return hasDimensionalSize(equalTo(width), equalTo(height));
    }

    /**
     * Creates an instance of {@link HasSizeMatcher} which checks size of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasSize} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}.
     *
     * @param width expected width of the element
     * @param heightMatcher height criteria
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasSize}.
     * @return instance of {@link HasSizeMatcher}
     */
    public static <T extends SearchContext> HasSizeMatcher<T> hasDimensionalSize(int width, Matcher<Integer> heightMatcher) {
        return hasDimensionalSize(equalTo(width), heightMatcher);
    }

    /**
     * Creates an instance of {@link HasSizeMatcher} which checks size of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasSize} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}.
     *
     * @param widthMatcher width criteria
     * @param height expected height of the element
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasSize}.
     * @return instance of {@link HasSizeMatcher}
     */
    public static <T extends SearchContext> HasSizeMatcher<T> hasDimensionalSize(Matcher<Integer> widthMatcher, int height) {
        return hasDimensionalSize(widthMatcher, equalTo(height));
    }

    /**
     * Creates an instance of {@link HasSizeMatcher} which checks size of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasSize} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}.
     *
     * @param widthMatcher width criteria
     * @param heightMatcher height criteria
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasSize}.
     * @return instance of {@link HasSizeMatcher}
     */
    public static <T extends SearchContext> HasSizeMatcher<T> hasDimensionalSize(Matcher<Integer> widthMatcher,
                                                                                 Matcher<Integer> heightMatcher) {
        return new HasSizeMatcher<>(widthMatcher, heightMatcher);
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        boolean result;
        Class<? extends SearchContext> clazz = item.getClass();
        Dimension size;

        if (WebElement.class.isAssignableFrom(clazz)) {
            size = WebElement.class.cast(item).getSize();
            result = (widthMatcher.matches(size.getWidth()) && heightMatcher.matches(size.getHeight()));
        }
        else if (HasSize.class.isAssignableFrom(clazz)){
            size = HasSize.class.cast(item).getSize();
            result = (widthMatcher.matches(size.getWidth()) && heightMatcher.matches(size.getHeight()));
        }
        else if (WrapsElement.class.isAssignableFrom(clazz)) {
            WebElement e = WrapsElement.class.cast(item).getWrappedElement();
            if (e == null) {
                mismatchDescription.appendText(format("Wrapped element is null. It is not possible to get size from an instance of %s.",
                        clazz.getName()));
                return false;
            }
            size = e.getSize();
            result = (widthMatcher.matches(size.getWidth()) && heightMatcher.matches(size.getHeight()));
        }
        else {
            mismatchDescription.appendText(format("It is not possible to get size from the instance of %s because " +
                            "it does not implement %s, %s or %s", clazz.getName(), WebElement.class,
                    HasSize.class.getName(),
                    WrapsElement.class.getName()));
            return false;
        }

        if (!result) {
            Description description = new StringDescription();
            if (!widthMatcher.matches(size.getWidth())) {
                widthMatcher.describeMismatch(size.getWidth(), description.appendText("width: "));
            }

            if (!heightMatcher.matches(size.getHeight())) {
                if (!isBlank(description.toString())) {
                    description.appendText(" ");
                }
                heightMatcher.describeMismatch(size.getHeight(), description.appendText("height: "));
            }
            mismatchDescription.appendText(description.toString());
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    public String toString() {
        return format("has width %s and height %s", widthMatcher.toString(),
                heightMatcher.toString());
    }
}
