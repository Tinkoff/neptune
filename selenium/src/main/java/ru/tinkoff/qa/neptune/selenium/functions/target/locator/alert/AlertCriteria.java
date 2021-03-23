package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;

/**
 * Creates criteria to find an {@link Alert}
 */
public final class AlertCriteria {

    private AlertCriteria() {
        super();
    }

    /**
     * The checking of an alert text.
     *
     * @param text that an alert is supposed to have
     * @return criteria that checks/filters an alert
     */
    @Description("Alert has text '{text}'")
    public static Criteria<Alert> alertText(@DescriptionFragment("text") String text) {
        checkArgument(isNotBlank(text), "Text should be defined");
        return condition(a -> Objects.equals(a.getText(), text));
    }

    /**
     * The checking of an alert text.
     *
     * @param expression is a substring of text that an alert is supposed to have.
     *                   It is possible to pass reg exp pattern that text of an alert should fit.
     * @return criteria that checks/filters an alert
     */
    @Description("Alert has text that contains '{expression}' or fits regExp pattern '{expression}'")
    public static Criteria<Alert> alertTextMatches(@DescriptionFragment("expression") String expression) {
        checkArgument(isNotBlank(expression), "Substring/RegEx pattern should be defined");

        return condition(a ->
                ofNullable(a.getText())
                        .map(s -> {
                            if (s.contains(expression)) {
                                return true;
                            }

                            try {
                                var p = compile(expression);
                                var mather = p.matcher(s);
                                return mather.matches();
                            } catch (Throwable thrown) {
                                thrown.printStackTrace();
                                return false;
                            }
                        })
                        .orElse(false));
    }
}
