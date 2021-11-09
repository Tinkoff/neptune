package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.alert;

import org.hamcrest.Matcher;
import org.openqa.selenium.Alert;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.equalTo;

@Description("alert text {textMatcher}")
public final class AlertHasTextMatcher extends NeptuneFeatureMatcher<Alert> {

    @DescriptionFragment(value = "textMatcher")
    private final Matcher<String> textMatcher;

    private AlertHasTextMatcher(Matcher<String> textMatcher) {
        super(true);
        checkArgument(nonNull(textMatcher), "Criteria for the matching of a text should be defined");
        this.textMatcher = textMatcher;
    }

    /**
     * Creates an instance of {@link AlertHasTextMatcher} that checks text of an alert.
     *
     * @param textMatcher criteria for a text under the matching.
     * @return instance of {@link AlertHasTextMatcher}
     */
    public static  AlertHasTextMatcher alertHasText(Matcher<String> textMatcher) {
        return new AlertHasTextMatcher(textMatcher);
    }

    /**
     * Creates an instance of {@link AlertHasTextMatcher} that checks text of an alert.
     *
     * @param text expected text of an alert.
     * @return instance of {@link AlertHasTextMatcher}
     */
    public static AlertHasTextMatcher alertHasText(String text) {
        return alertHasText(equalTo(text));
    }

    @Override
    protected boolean featureMatches(Alert toMatch) {
        var text = toMatch.getText();
        var result = textMatcher.matches(text);

        if (!result) {
            appendMismatchDescription(textMatcher, text);
        }
        return result;
    }
}
