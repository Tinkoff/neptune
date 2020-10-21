package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.browser.proxy;

import com.browserup.harreader.model.HarEntry;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;

public final class RequestHasStringBody extends TypeSafeDiagnosingMatcher<HarEntry> {

    private final Matcher<? super String> bodyMatcher;

    private RequestHasStringBody(Matcher<? super String> bodyMatcher) {
        this.bodyMatcher = bodyMatcher;
    }

    /**
     * Creates matcher that checks body of the request.
     *
     * @param bodyMatcher is the criteria that describes request body
     * @return a new instance of {@link RequestHasStringBody}
     */
    public static RequestHasStringBody requestHasStringBody(Matcher<? super String> bodyMatcher) {
        return new RequestHasStringBody(bodyMatcher);
    }

    /**
     * Creates matcher that checks body of the request.
     *
     * @param body is the expected body of the request
     * @return a new instance of {@link RequestHasStringBody}
     */
    public static RequestHasStringBody requestHasStringBody(String body) {
        return new RequestHasStringBody(is(body));
    }

    @Override
    protected boolean matchesSafely(HarEntry item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("Proxied entry is null");
            return false;
        }

        var postData = item.getRequest().getPostData();

        if (postData == null) {
            mismatchDescription.appendText("Request body is null");
            return false;
        }

        var requestBody = postData.getText();
        var result = bodyMatcher.matches(requestBody);

        if (!result) {
            bodyMatcher.describeMismatch(requestBody, mismatchDescription);
        }

        return result;
    }

    @Override
    public String toString() {
        return format("request has body %s", bodyMatcher);
    }
}
