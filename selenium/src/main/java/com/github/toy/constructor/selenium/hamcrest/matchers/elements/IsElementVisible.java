package com.github.toy.constructor.selenium.hamcrest.matchers.elements;

import com.github.toy.constructor.selenium.api.widget.IsVisible;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static java.lang.String.format;

public final class IsElementVisible extends TypeSafeDiagnosingMatcher<SearchContext> {

    private IsElementVisible() {
        super();
    }

    /**
     * Creates an instance of {@link IsElementVisible} which checks visibility of some instance of {@link SearchContext}.
     * It should be {@link WebElement} or some implementor of {@link IsVisible}. Otherwise the matching returns {@code false}
     * @return an instance of {@link IsElementVisible}
     */
    public static IsElementVisible isVisible() {
        return new IsElementVisible();
    }

    @Override
    protected boolean matchesSafely(SearchContext item, Description mismatchDescription) {
        boolean result;
        Class<? extends SearchContext> clazz = item.getClass();

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = WebElement.class.cast(item).isDisplayed();
        } else if (!IsVisible.class.isAssignableFrom(clazz)) {
            mismatchDescription.appendText(format("It is not possible to get visibility from an instance of %s because " +
                    "it does not implement %s", clazz.getName(), IsVisible.class.getName()));
            return false;
        }
        else {
            result = IsVisible.class.cast(item).isVisible();
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
