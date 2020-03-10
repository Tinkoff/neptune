package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import net.lightbody.bmp.core.har.HarNameValuePair;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

public final class HasHarPair extends TypeSafeDiagnosingMatcher<HarNameValuePair> {

    private final Matcher<? super String> nameMatcher;
    private final Matcher<? super String> valueMatcher;

    private HasHarPair(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        this.nameMatcher = nameMatcher;
        this.valueMatcher = valueMatcher;
    }

    /**
     * Creates matcher that checks {@link HarNameValuePair} object
     *
     * @param nameMatcher  criteria that describes field 'name'
     * @param valueMatcher criteria that describes field 'value'
     * @return a new instance of {@link HasHarPair}
     */
    public static HasHarPair hasHarPair(Matcher<? super String> nameMatcher, Matcher<? super String> valueMatcher) {
        return new HasHarPair(nameMatcher, valueMatcher);
    }

    /**
     * Creates matcher that checks {@link HarNameValuePair} object
     *
     * @param nameMatcher criteria that describes field 'name'
     * @param value       is the expected value
     * @return a new instance of {@link HasHarPair}
     */
    public static HasHarPair hasHarPair(Matcher<? super String> nameMatcher, String value) {
        return new HasHarPair(nameMatcher, is(value));
    }

    /**
     * Creates matcher that checks {@link HarNameValuePair} object
     *
     * @param name         is the expected name
     * @param valueMatcher criteria that describes field 'value'
     * @return a new instance of {@link HasHarPair}
     */
    public static HasHarPair hasHarPair(String name, Matcher<? super String> valueMatcher) {
        return new HasHarPair(is(name), valueMatcher);
    }

    /**
     * Creates matcher that checks {@link HarNameValuePair} object
     *
     * @param name  is the expected name
     * @param value is the expected value
     * @return a new instance of {@link HasHarPair}
     */
    public static HasHarPair hasHarPair(String name, String value) {
        return new HasHarPair(is(name), is(value));
    }

    @Override
    protected boolean matchesSafely(HarNameValuePair item, Description mismatchDescription) {
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
        return format("HAR name/value pair has name %s and value %s", nameMatcher, valueMatcher);
    }
}
