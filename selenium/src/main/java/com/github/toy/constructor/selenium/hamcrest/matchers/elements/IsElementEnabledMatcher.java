package com.github.toy.constructor.selenium.hamcrest.matchers.elements;

import com.github.toy.constructor.selenium.api.widget.IsEnabled;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import static java.lang.String.format;

public final class IsElementEnabledMatcher extends TypeSafeDiagnosingMatcher<SearchContext> {

    private IsElementEnabledMatcher() {
        super();
    }

    /**
     * Creates an instance of {@link IsElementEnabledMatcher} which checks is some instance of {@link SearchContext} enabled or not.
     * It should be {@link WebElement} or some implementor of {@link IsEnabled} or {@link WrapsElement}.
     * Otherwise the matching returns {@code false}
     *
     * @return an instance of {@link IsElementEnabledMatcher}
     */
    public static IsElementEnabledMatcher isEnabled() {
        return new IsElementEnabledMatcher();
    }

    @Override
    protected boolean matchesSafely(SearchContext item, Description mismatchDescription) {
        boolean result;
        Class<? extends SearchContext> clazz = item.getClass();

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = WebElement.class.cast(item).isEnabled();
        }
        else if (IsEnabled.class.isAssignableFrom(clazz)){
            result = IsEnabled.class.cast(item).isEnabled();
        }
        else if (WrapsElement.class.isAssignableFrom(clazz)) {
            WebElement e = WrapsElement.class.cast(item).getWrappedElement();
            if (e == null) {
                mismatchDescription.appendText(format("Wrapped element is null. It is not possible to check is instance of %s enabled or not",
                        clazz.getName()));
                return false;
            }
            result = e.isEnabled();
        }
        else {
            mismatchDescription.appendText(format("It is not possible check is instance of %s enabled or not because " +
                    "it does not implement %s, %s or %s", clazz.getName(), WebElement.class,
                    IsEnabled.class.getName(),
                    WrapsElement.class.getName()));
            return false;
        }

        if (!result) {
            mismatchDescription.appendText("element is not enabled");
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    @Override
    public String toString() {
        return "element is enabled";
    }
}
