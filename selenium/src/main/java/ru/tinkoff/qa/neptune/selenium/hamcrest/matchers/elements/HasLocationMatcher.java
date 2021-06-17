package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasLocation;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.hamcrest.Matchers.equalTo;

@Description("location x {xMatcher} and y {yMatcher} relatively [{point}]")
public final class HasLocationMatcher extends NeptuneFeatureMatcher<SearchContext> {

    @DescriptionFragment(value = "xMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> xMatcher;
    @DescriptionFragment(value = "yMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> yMatcher;
    @DescriptionFragment(value = "point", makeReadableBy = PointValueGetter.class)
    private final Point relativeTo;

    private HasLocationMatcher(Matcher<Integer> xMatcher, Matcher<Integer> yMatcher, Point relativeTo) {
        super(true, WebElement.class, Widget.class);
        checkArgument(nonNull(xMatcher), "Criteria to check x-value should be defined");
        checkArgument(nonNull(yMatcher), "Criteria to check y-value should be defined");
        checkArgument(nonNull(relativeTo), "Point should be defined");
        this.xMatcher = xMatcher;
        this.yMatcher = yMatcher;
        this.relativeTo = relativeTo;
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation}.
     * Otherwise the matching returns {@code false}.
     *
     * @param x     expected x value
     * @param y     expected y value
     * @param point is considered to be the starting point
     * @return instance of {@link HasLocationMatcher}
     */
    public static Matcher<SearchContext> hasLocation(int x, int y, Point point) {
        return hasLocation(equalTo(x), equalTo(y), point);
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation}.
     * Otherwise the matching returns {@code false}.
     *
     * @param x        expected x value
     * @param yMatcher y-value criteria
     * @param point    is considered to be the starting point
     * @return instance of {@link HasLocationMatcher}
     */
    public static Matcher<SearchContext> hasLocation(int x, Matcher<Integer> yMatcher, Point point) {
        return hasLocation(equalTo(x), yMatcher, point);
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation}.
     * Otherwise the matching returns {@code false}.
     *
     * @param xMatcher x-value criteria
     * @param y        expected y value
     * @param point    is considered to be the starting point
     * @return instance of {@link HasLocationMatcher}
     */
    public static Matcher<SearchContext> hasLocation(Matcher<Integer> xMatcher, int y, Point point) {
        return hasLocation(xMatcher, equalTo(y), point);
    }


    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation}.
     * Otherwise the matching returns {@code false}.
     *
     * @param x expected x value
     * @param y expected y value
     * @return instance of {@link HasLocationMatcher}
     */
    public static Matcher<SearchContext> hasLocation(int x, int y) {
        return hasLocation(equalTo(x), equalTo(y));
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation}.
     * Otherwise the matching returns {@code false}.
     *
     * @param x        expected x value
     * @param yMatcher y-value criteria
     * @return instance of {@link HasLocationMatcher}
     */
    public static Matcher<SearchContext> hasLocation(int x, Matcher<Integer> yMatcher) {
        return hasLocation(equalTo(x), yMatcher);
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation}.
     * Otherwise the matching returns {@code false}.
     *
     * @param xMatcher x-value criteria
     * @param y        expected y value
     * @return instance of {@link HasLocationMatcher}
     */
    public static Matcher<SearchContext> hasLocation(Matcher<Integer> xMatcher, int y) {
        return hasLocation(xMatcher, equalTo(y));
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation}.
     * Otherwise the matching returns {@code false}.
     *
     * @param xMatcher x-value criteria
     * @param yMatcher y-value criteria
     * @param point    is considered to be the starting point
     * @return instance of {@link HasLocationMatcher}
     */
    public static Matcher<SearchContext> hasLocation(Matcher<Integer> xMatcher,
                                                     Matcher<Integer> yMatcher,
                                                     Point point) {
        return new HasLocationMatcher(xMatcher, yMatcher, point);
    }

    /**
     * Creates an instance of {@link HasLocationMatcher} that checks location of an instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link HasLocation}.
     * Otherwise the matching returns {@code false}.
     *
     * @param xMatcher x-value criteria
     * @param yMatcher y-value criteria
     * @return instance of {@link HasLocationMatcher}
     */
    public static Matcher<SearchContext> hasLocation(Matcher<Integer> xMatcher,
                                                     Matcher<Integer> yMatcher) {
        return new HasLocationMatcher(xMatcher, yMatcher, new Point(0, 0));
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        var clazz = toMatch.getClass();

        Point point;
        if (WebElement.class.isAssignableFrom(clazz)) {
            point = ((WebElement) toMatch).getLocation();
        } else {
            point = ((HasLocation) toMatch).getLocation();
        }

        var x = ofNullable(relativeTo).map(p -> point.getX() - p.getX()).orElseGet(point::getX);
        var y = ofNullable(relativeTo).map(p -> point.getY() - p.getY()).orElseGet(point::getY);

        var result = true;
        if (!xMatcher.matches(x)) {
            appendMismatchDescription(new PropertyValueMismatch("x", x, xMatcher));
            result = false;
        }

        if (!yMatcher.matches(y)) {
            appendMismatchDescription(new PropertyValueMismatch("y", y, yMatcher));
            result = false;
        }

        return result;
    }
}
