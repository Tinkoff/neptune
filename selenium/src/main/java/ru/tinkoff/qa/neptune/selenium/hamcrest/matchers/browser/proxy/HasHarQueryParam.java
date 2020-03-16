package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarQueryParam;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

public final class HasHarQueryParam extends TypeSafeDiagnosingMatcher<HarQueryParam> {

    private final Matcher<? super String> nameMatcher;
    private final Matcher<? super String> valueMatcher;

    private HasHarQueryParam(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        this.nameMatcher = nameMatcher;
        this.valueMatcher = valueMatcher;
    }

    /**
     * Creates matcher that checks {@link HarQueryParam} object
     *
     * @param nameMatcher  criteria that describes field 'name'
     * @param valueMatcher criteria that describes field 'value'
     * @return a new instance of {@link HasHarQueryParam}
     */
    public static HasHarQueryParam hasHarQueryParam(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        return new HasHarQueryParam(nameMatcher, valueMatcher);
    }

    /**
     * Creates matcher that checks {@link HarQueryParam} object
     *
     * @param nameMatcher criteria that describes field 'name'
     * @param value       is the expected value
     * @return a new instance of {@link HasHarQueryParam}
     */
    public static HasHarQueryParam hasHarQueryParam(Matcher<? super String> nameMatcher, String value) {
        return new HasHarQueryParam(nameMatcher, is(value));
    }

    /**
     * Creates matcher that checks {@link HarQueryParam} object
     *
     * @param name         is the expected name
     * @param valueMatcher criteria that describes field 'value'
     * @return a new instance of {@link HasHarQueryParam}
     */
    public static HasHarQueryParam hasHarQueryParam(String name, Matcher<? super String> valueMatcher) {
        return new HasHarQueryParam(is(name), valueMatcher);
    }

    /**
     * Creates matcher that checks {@link HarQueryParam} object
     *
     * @param name  is the expected name
     * @param value is the expected value
     * @return a new instance of {@link HasHarQueryParam}
     */
    public static HasHarQueryParam hasHarQueryParam(String name, String value) {
        return new HasHarQueryParam(is(name), is(value));
    }

    @Override
    protected boolean matchesSafely(HarQueryParam item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("HAR name/value pair is null");
            return false;
        }

        var name = item.getName();
        var value = item.getValue();

        var nameResult = nameMatcher.matches(name);
        var valueResult = valueMatcher.matches(value);

        if (!nameResult) {
            nameMatcher.describeMismatch(name, mismatchDescription);
        }

        if (!valueResult) {
            valueMatcher.describeMismatch(value, mismatchDescription);
        }

        return nameResult && valueResult;
    }

    @Override
    public String toString() {
        return format("HAR request query param has name %s, value %s", nameMatcher, valueMatcher);
    }
}
