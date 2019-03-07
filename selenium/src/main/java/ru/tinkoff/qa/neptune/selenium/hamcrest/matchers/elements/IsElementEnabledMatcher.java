package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import ru.tinkoff.qa.neptune.selenium.api.widget.IsEnabled;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;
import org.hamcrest.Description;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import static java.lang.String.format;

public final class IsElementEnabledMatcher extends TypeSafeDiagnosingMatcher<SearchContext> {

    private IsElementEnabledMatcher() {
        super();
    }

    /**
     * Creates an instance of {@link IsElementEnabledMatcher} that checks is some instance of {@link SearchContext} enabled or not.
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
        var clazz = item.getClass();

        if (WebElement.class.isAssignableFrom(clazz)) {
            result = ((WebElement) item).isEnabled();
        }
        else if (IsEnabled.class.isAssignableFrom(clazz)){
            result = ((IsEnabled) item).isEnabled();
        }
        else if (WrapsElement.class.isAssignableFrom(clazz)) {
            var e = ((WrapsElement) item).getWrappedElement();
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
    public String toString() {
        return "element is enabled";
    }
}
