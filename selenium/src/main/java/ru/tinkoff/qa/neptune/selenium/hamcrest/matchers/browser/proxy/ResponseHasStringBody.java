package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

public final class ResponseHasStringBody extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super String> bodyMatcher;

    private ResponseHasStringBody(Matcher<? super String> bodyMatcher) {
        this.bodyMatcher = bodyMatcher;
    }

    /**
     * Creates matcher that checks body of the response.
     *
     * @param bodyMatcher criteria that describes response body
     * @return a new instance of {@link ResponseHasStringBody}
     */
    public static ResponseHasStringBody responseHasStringBody(Matcher<? super String> bodyMatcher) {
        return new ResponseHasStringBody(bodyMatcher);
    }

    /**
     * Creates matcher that checks body of the response.
     *
     * @param body is the expected body of the response
     * @return a new instance of {@link ResponseHasStringBody}
     */
    public static ResponseHasStringBody responseHasStringBody(String body) {
        return new ResponseHasStringBody(is(body));
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var content = item.getResponse().getContent();

        if (content == null) {
            mismatchDescription.appendText("Response body is null");
            return false;
        }

        var responseBody = content.getText();
        var result = bodyMatcher.matches(responseBody);

        if (!result) {
            bodyMatcher.describeMismatch(responseBody, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("response has body %s", bodyMatcher);
    }
}
