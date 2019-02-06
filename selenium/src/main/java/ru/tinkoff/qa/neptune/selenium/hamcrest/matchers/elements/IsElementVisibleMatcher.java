package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import ru.tinkoff.qa.neptune.selenium.api.widget.IsVisible;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import static java.lang.String.format;

public final class IsElementVisibleMatcher extends TypeSafeDiagnosingMatcher<SearchContext> {

    private IsElementVisibleMatcher() {
        super();
    }

    /**
     * Creates an instance of {@link IsElementVisibleMatcher} which checks visibility of some instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link IsVisible} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}
     *
     * @return an instance of {@link IsElementVisibleMatcher}
     */
    public static IsElementVisibleMatcher isVisible() {
        return new IsElementVisibleMatcher();
    }

    @Override
    protected boolean matchesSafely(SearchContext item, Description mismatchDescription) {
        boolean result;
        var clazz = item.getClass();

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = ((WebElement) item).isDisplayed();
        }
        else if (IsVisible.class.isAssignableFrom(clazz)){
            result = ((IsVisible) item).isVisible();
        }
        else if (WrapsElement.class.isAssignableFrom(clazz)) {
            var e = ((WrapsElement) item).getWrappedElement();
            if (e == null) {
                mismatchDescription.appendText(format("Wrapped element is null. It is not possible to get visibility from an instance of %s.",
                        clazz.getName()));
                return false;
            }
            result = e.isDisplayed();
        }
        else {
            mismatchDescription.appendText(format("It is not possible to get visibility from an instance of %s because " +
                    "it does not implement %s, %s or %s", clazz.getName(), WebElement.class.getName(), IsVisible.class.getName(),
                    WrapsElement.class.getName()));
            return false;
        }

        if (!result) {
            mismatchDescription.appendText("element is not visible");
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    @Override
    public String toString() {
        return "element is visible";
    }
}
