package com.github.toy.constructor.selenium.hamcrest.matchers.alert;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.openqa.selenium.Alert;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;

public final class AlertHasTextMatcher extends TypeSafeDiagnosingMatcher<Alert> {

    private final Matcher<String> textMatcher;

    private AlertHasTextMatcher(Matcher<String> textMatcher) {
        checkArgument(textMatcher != null, "Criteria for the matching of a text should be defined");
        this.textMatcher = textMatcher;
    }

    /**
     * Creates an instance of {@link AlertHasTextMatcher} which checks text of an alert.
     *
     * @param textMatcher criteria for a text under the matching.
     * @return instance of {@link AlertHasTextMatcher}
     */
    public static  AlertHasTextMatcher alertHasText(Matcher<String> textMatcher) {
        return new AlertHasTextMatcher(textMatcher);
    }

    /**
     * Creates an instance of {@link AlertHasTextMatcher} which checks text of an alert.
     *
     * @param text expected text of an alert.
     * @return instance of {@link AlertHasTextMatcher}
     */
    public static AlertHasTextMatcher alertHasText(String text) {
        return alertHasText(equalTo(text));
    }

    @Override
    protected boolean matchesSafely(Alert item, Description mismatchDescription) {
        String text = item.getText();
        boolean result = textMatcher.matches(text);

        if (!result) {
            textMatcher.describeMismatch(text, mismatchDescription);
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(toString());
    }

    @Override
    public String toString() {
        return format("text of the alert %s", textMatcher.toString());
    }
}
