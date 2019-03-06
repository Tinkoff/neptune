package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import ru.tinkoff.qa.neptune.selenium.api.widget.HasLocation;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hamcrest.Matchers.equalTo;

public final class HasLocationMatcher<T extends SearchContext> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<Integer> xMatcher;
    private final Matcher<Integer> yMatcher;
    private Point relativeTo;

    private HasLocationMatcher(Matcher<Integer> xMatcher, Matcher<Integer> yMatcher) {
        checkArgument(nonNull(xMatcher), "Criteria to check x-value should be defined");
        checkArgument(nonNull(yMatcher), "Criteria to check y-value should be defined");
        this.xMatcher = xMatcher;
        this.yMatcher = yMatcher;
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}.
     *
     * @param x expected x value
     * @param y expected y value
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasLocation}.
     * @return instance of {@link HasLocationMatcher}
     */
    public static <T extends SearchContext> HasLocationMatcher<T> hasLoction(int x, int y) {
        return hasLoction(equalTo(x), equalTo(y));
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}.
     *
     * @param x expected x value
     * @param yMatcher y-value criteria
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasLocation}.
     * @return instance of {@link HasLocationMatcher}
     */
    public static <T extends SearchContext> HasLocationMatcher<T> hasLoction(int x, Matcher<Integer> yMatcher) {
        return hasLoction(equalTo(x), yMatcher);
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}.
     *
     * @param xMatcher x-value criteria
     * @param y expected y value
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasLocation}.
     * @return instance of {@link HasLocationMatcher}
     */
    public static <T extends SearchContext> HasLocationMatcher<T> hasLoction(Matcher<Integer> xMatcher, int y) {
        return hasLoction(xMatcher, equalTo(y));
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}.
     *
     * @param xMatcher x-value criteria
     * @param yMatcher y-value criteria
     * @param <T> is a type of a value to be matched. It should extend {@link WebElement} or it should extend both
     *           {@link SearchContext} and {@link HasLocation}.
     * @return instance of {@link HasLocationMatcher}
     */
    public static <T extends SearchContext> HasLocationMatcher<T> hasLoction(Matcher<Integer> xMatcher,
                                                                              Matcher<Integer> yMatcher) {
        return new HasLocationMatcher<>(xMatcher, yMatcher);
    }

    /**
     * Sets the element. Location under the matching is considered relative to upper left corner of the element at this
     * case.
     *
     * @param relativeTo is an element with coordinates. Location under the matching is considered
     *                   relative to upper left corner of the element.
     * @return self-reference
     */
    public HasLocationMatcher<T> relativeTo(WebElement relativeTo) {
        this.relativeTo = relativeTo.getLocation();
        return this;
    }

    /**
     * Sets the item that has coordinates. Location under the matching is considered relative to coordinates of this item at this
     * case.
     *
     * @param relativeTo is some with has coordinates. Location under the matching is considered
     *                   relative to coordinates of this item.
     * @param <Q> type of customized element that provide getting of location
     * @return self-reference
     */
    public <Q extends SearchContext & HasLocation> HasLocationMatcher<T> relativeTo(Q relativeTo) {
        this.relativeTo = relativeTo.getLocation();
        return this;
    }

    @Override
    protected boolean matchesSafely(T item, Description mismatchDescription) {
        var clazz = item.getClass();
        if (!WebElement.class.isAssignableFrom(clazz) && HasLocation.class.isAssignableFrom(clazz)
                && WrapsElement.class.isAssignableFrom(clazz)) {
            mismatchDescription.appendText(format("It is not possible to get position from the instance of %s because " +
                            "it does not implement %s, %s or %s", clazz.getName(), WebElement.class,
                    HasLocation.class.getName(),
                    WrapsElement.class.getName()));
            return false;
        }

        Point point;
        if (WebElement.class.isAssignableFrom(clazz)) {
            point = ((WebElement) item).getLocation();
        }
        else if (HasLocation.class.isAssignableFrom(clazz)) {
            point = ((HasLocation) item).getLocation();
        }
        else {
            var e = ((WrapsElement) item).getWrappedElement();
            if (e == null) {
                mismatchDescription.appendText(format("Wrapped element is null. It is not possible to get position from an instance of %s.",
                        clazz.getName()));
                return false;
            }
            point = e.getLocation();
        }

        var x = ofNullable(relativeTo).map(p -> point.getX() - p.getX()).orElseGet(point::getX);
        var y = ofNullable(relativeTo).map(p -> point.getY() - p.getY()).orElseGet(point::getY);
        var result = (xMatcher.matches(x) && yMatcher.matches(y));

        if (!result) {
            var description = new StringDescription();
            if (!xMatcher.matches(x)) {
                xMatcher.describeMismatch(x, description.appendText("x: "));
            }

            if (!yMatcher.matches(y)) {
                if (!isBlank(description.toString())) {
                    description.appendText(" ");
                }
                yMatcher.describeMismatch(y, description.appendText("y: "));
            }
            mismatchDescription.appendText(description.toString());
        }

        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    @Override
    public String toString() {
        return ofNullable(relativeTo)
                .map(p -> format("has position x %s and y %s relative to %s", xMatcher, yMatcher, p))
                .orElseGet(() -> format("has position x %s and y %s", xMatcher, yMatcher));
    }
}
